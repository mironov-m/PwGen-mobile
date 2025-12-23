# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

PwGen is a password generator mobile application built with Kotlin Multiplatform and Compose Multiplatform, targeting Android and iOS platforms.

**Package**: `ru.mironov.pwgen`

## Architecture

### Multiplatform Structure

The project follows Kotlin Multiplatform conventions with platform-specific and shared code:

- **composeApp/src/commonMain**: Shared Kotlin code and Compose UI for all platforms
  - `ru/mironov/pwgen/domain`: Business logic
  - `ru/mironov/pwgen`: Shared UI components

- **composeApp/src/androidMain**: Android-specific code
  - `MainActivity.kt`: Android entry point using `ComponentActivity`
  - `Platform.android.kt`: Android platform implementations

- **composeApp/src/iosMain**: iOS-specific Kotlin code
  - `MainViewController.kt`: Creates Compose UI for iOS
  - `Platform.ios.kt`: iOS platform implementations

- **iosApp**: Native iOS application wrapper
  - SwiftUI app that embeds Compose UI via `UIViewControllerRepresentable`
  - The Kotlin framework is named `ComposeApp` (static framework)

### Key Architectural Points

1. The iOS app uses a SwiftUI wrapper (`ContentView.swift`) that hosts the Compose UI through `ComposeView: UIViewControllerRepresentable`
2. The Kotlin framework for iOS is configured as static (`isStatic = true`) and supports both `iosArm64` and `iosSimulatorArm64`
3. Shared business logic lives in `commonMain/kotlin/ru/mironov/pwgen/domain`
4. Platform-specific implementations use the expect/actual pattern via `Platform.kt` files

## Build Commands

### Android

On Windows:
```bash
gradlew.bat :composeApp:assembleDebug
```

On macOS/Linux:
```bash
./gradlew :composeApp:assembleDebug
```

For release builds:
```bash
gradlew.bat :composeApp:assembleRelease  # Windows
./gradlew :composeApp:assembleRelease     # macOS/Linux
```

### iOS

Open `iosApp/iosApp.xcodeproj` in Xcode and build from there, or use IDE run configurations.

The iOS framework must be built before running the iOS app. The Gradle build automatically generates the `ComposeApp.framework`.

### Testing

Common tests are located in `composeApp/src/commonTest/kotlin/ru/mironov/pwgen/`

Run tests:
```bash
./gradlew :composeApp:test             # All tests
./gradlew :composeApp:testDebugUnitTest  # Android unit tests
```

## Configuration

- **compileSdk/targetSdk/minSdk**: Defined in version catalog (`libs.versions.toml`)
- **JVM target**: Java 11 for Android
- **Application ID**: `ru.mironov.pwgen`
- **Namespace**: `ru.mironov.pwgen`

## Development Notes

- Material 3 is used for UI components
- The project uses Compose resources for cross-platform assets (see `composeApp/src/commonMain/composeResources`)
- Platform-specific resources: Android uses standard `res/` folders, iOS uses Xcode asset catalogs
- The main app composable is `App()` in `App.kt`, which is called from both Android (`MainActivity`) and iOS (`MainViewController`)