## Problem

Crash occurs when attempting to:
- use an Objective-C library (bugsnag-cocoa)
- from a Kotlin Multiplatform library (`./lib`)
- from a pure Kotlin iOS application (`./app`)

## Run & reproduce

### Requirements

- MacOS
- Java
- XCode 11
- Carthage

### Build & run

Compile and publish the `com.example.lib` library locally:
```bash
cd lib
./gradlew publishToMavenLocal
```
This will run `carthage` to get and use the `bugsnag-cocoa` Objective-C library.

Open the app in `app` using XCode (tested with XCode 11.3) and try to run it.
This will run `./gradlew xcode` in `./app` which will also run `carthage` to get and link the `bugsnag-cocoa` Objective-C library.

### Expected result

The app should start by showing the `LaunchScreen`, which is red, then configure Bugsnag, then start the `StartupViewController` which shows a yellow view.

### Result

The app starts with a red screen and crashes on the call to Bugsnag. We can reach the yellow screen if we comment the call to Bugsnag (line 24 in AppDelegate.kt).
