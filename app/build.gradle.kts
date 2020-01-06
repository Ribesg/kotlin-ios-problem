import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetPreset
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.tasks.CInteropProcess
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink

group = "com.example"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenLocal()
    jcenter()
}

plugins {
    kotlin("multiplatform") version "1.3.61"
}

val configuration: String? = System.getenv("CONFIGURATION")
val executablePath: String? = System.getenv("EXECUTABLE_PATH")
val sdkName: String? = System.getenv("SDK_NAME")
val targetBuildDir: String? = System.getenv("TARGET_BUILD_DIR")

val buildType = configuration?.run {
    NativeBuildType.valueOf(toUpperCase())
} ?: NativeBuildType.DEBUG
val targetName = "ios"
val targetArch = if (sdkName?.startsWith("iphoneos") == true) "arm64" else "x64"

kotlin {

    targetFromPreset(presets.getByName<KotlinNativeTargetPreset>("ios${targetArch.capitalize()}"), targetName) {
        binaries.executable(listOf(buildType)) {
            baseName = "app"
            linkerOpts("-Fcarthage/Carthage/Build/iOS")
        }
    }

}

dependencies {
    "commonMainApi"(kotlin("stdlib-common"))
    "commonMainApi"("com.example:lib:0.0.1-SNAPSHOT")
}

val target = kotlin.targets[targetName] as KotlinNativeTarget
val kotlinBinary = target.binaries.getExecutable(buildType)

val xcodeTaskName = "xcode"
if (executablePath != null && sdkName != null && targetBuildDir != null) {
    tasks.create<Copy>(xcodeTaskName) {
        dependsOn(kotlinBinary.linkTask)
        group = "xcode"
        destinationDir = file(targetBuildDir)
        from(kotlinBinary.outputFile)
        rename { executablePath }
    }
} else {
    tasks.create(xcodeTaskName) {
        group = "xcode"
        doLast {
            throw IllegalStateException("Please run the '$xcodeTaskName' task from XCode")
        }
    }
}

// Create Carthage tasks
listOf("bootstrap", "update").forEach { type ->
    task<Exec>("carthage${type.capitalize()}") {
        group = "carthage"
        workingDir = file("carthage")
        executable = "carthage"
        args(
            type,
            "--platform", "iOS",
            "--no-use-binaries",
            "--cache-builds"
        )
    }
}

// Make CInterop and Native Link tasks depend on Carthage
afterEvaluate {
    tasks.filter { it is CInteropProcess || it is KotlinNativeLink }.forEach {
        it.dependsOn("carthageBootstrap")
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "6.0.1"
    distributionType = Wrapper.DistributionType.ALL
}
