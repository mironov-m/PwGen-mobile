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

### MVI Architecture with Orbit

The app follows the MVI (Model-View-Intent) pattern using **Orbit MVI**:

**ViewModel Structure:**
```kotlin
class MainViewModel(
    private val passwordGenerator: PasswordGenerator,
) : ContainerHost<MainState, MainEffect>, ViewModel() {

    override val container = container<MainState, MainEffect>(MainState())

    fun changePasswordLength(length: Int) = intent {
        reduce {
            val newSettings = state.passwordGenerationSettings.copy(length = length)
            state.copy(passwordGenerationSettings = newSettings)
        }
    }
}
```

**Key Files:**
- `ui/screens/[screen]/presentation/[Screen]ViewModel.kt`: MVI container with intents
- `ui/screens/[screen]/presentation/[Screen]State.kt`: Immutable presentation state
- `ui/screens/[screen]/presentation/[Screen]Effect.kt`: Side effects (navigation, toasts, etc.)

**State Flow:**
1. User action triggers an intent method
2. Intent uses `reduce { }` to transform state immutably
3. Container automatically notifies observers
4. UI recomposes with new state via `viewModel.collectAsState()`

**Dependencies:**
- `orbit-core`: Core MVI functionality
- `orbit-compose`: Compose integration
- `orbit-viewmodel`: ViewModel integration

### Navigation with Voyager

Navigation is handled by **Voyager**, a type-safe KMP navigation library.

**Screen Definition:**
```kotlin
class MainScreen : Screen {
    @Composable
    override fun Content() {
        ...
    }
}
```

**Navigation Setup:**
- **Android**: `Navigator(MainScreen())` in `MainActivity.kt`
- **iOS**: `Navigator(MainScreen())` in `MainViewController.kt` with `ProvideNavigatorLifecycleKMPSupport`

**Screen Location**: `composeApp/src/commonMain/kotlin/ru/mironov/pwgen/ui/screens/`

**Navigation Features:**
- Type-safe screen navigation
- Built-in lifecycle management
- Stack-based navigation (LIFO)
- Integration with Koin DI via `koinViewModel()`

**Adding New Screens:**
1. Create screen class implementing `Screen` interface
2. Implement `Content()` composable
3. Register ViewModel in `di/PresentationModule.kt`
4. Navigate using `LocalNavigator.current.push(NewScreen())`

### Dependency Injection with Koin

The app uses **Koin** for dependency injection.

**Module Structure:**
- `di/AppModule.kt`: Domain layer dependencies (PasswordGenerator, etc.)
- `di/PresentationModule.kt`: ViewModels registration
- `di/KoinInitializer.kt`: Initialization logic

**Example Registration:**
```kotlin
// Domain service (singleton)
val appModule = module {
    single<PasswordGenerator> { PasswordGeneratorSecureRandomImpl() }
}

// ViewModel
val presentationModule = module {
    viewModel { MainViewModel(get()) }
}
```

**Initialization:**
- Android: Initialized in androidMain/kotlin/ru/mironov/pwgen/PwGenApplication.kt
- iOS: Initialized in `MainViewController.kt` via `koinInitializer`

### Theme and Colors

The app uses **Material 3** theming with custom color schemes.

**Theme Files:**
- `ui/theme/Colors.kt`: Light and dark color schemes
- `ui/theme/AppTheme.kt`: MaterialTheme wrapper with automatic dark mode detection

**Color Schemes:**
- **Light Theme**
- **Dark Theme**

**Theme Application:**
```kotlin
AppTheme(darkTheme: Boolean = isSystemInDarkTheme()) {
    // UI content
}
```

**Automatic Dark Mode:**
- Theme automatically follows system settings via `isSystemInDarkTheme()`
- Can be overridden with explicit `darkTheme` parameter

**Accessing Theme Colors:**
```kotlin
@Composable
fun MyComponent() {
    val backgroundColor = MaterialTheme.colorScheme.background
    val primaryColor = MaterialTheme.colorScheme.primary
    // Use colors from MaterialTheme.colorScheme.*
}
```

**Material 3 Color Roles:**
- `primary`, `onPrimary`, `primaryContainer`, `onPrimaryContainer`
- `secondary`, `onSecondary`, `secondaryContainer`, `onSecondaryContainer`
- `tertiary`, `onTertiary`, `tertiaryContainer`, `onTertiaryContainer`
- `background`, `onBackground`
- `surface`, `onSurface`, `surfaceVariant`, `onSurfaceVariant`
- `error`, `onError`, `errorContainer`, `onErrorContainer`
- `outline`, `outlineVariant`
- `inverseSurface`, `inverseOnSurface`, `inversePrimary`

**Best Practices:**
- Always use `MaterialTheme.colorScheme.*` instead of hardcoded colors
- Use semantic color roles (e.g., `error` for error states, `primaryContainer` for less prominent buttons)
- Ensure sufficient contrast ratios for accessibility

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