import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.CInteropProcess
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink

group = "com.example"
version = "0.0.1-SNAPSHOT"

repositories {
    jcenter()
}

plugins {
    kotlin("multiplatform") version "1.3.61"
    `maven-publish`
}

kotlin {

    iosArm64("ios") {
        mavenPublication {
            artifactId = "lib-ios-arm64"
        }
    }

    iosX64("iosSim") {
        mavenPublication {
            artifactId = "lib-ios-x64"
        }
    }

    targets.withType<KotlinNativeTarget> {
        compilations.getByName("main").cinterops.create("Bugsnag") {
            defFile("src/iosMain/cinterop/Bugsnag.def")
            includeDirs.allHeaders("carthage/Carthage/Build/iOS/Bugsnag.framework/Headers")
        }
    }

}

dependencies {
    "commonMainImplementation"(kotlin("stdlib-common"))
}

// Carthage tasks
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
