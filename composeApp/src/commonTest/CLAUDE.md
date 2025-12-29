# Testing Guide for PwGen

This file provides detailed testing guidelines for the PwGen project. For general project information, see main [CLAUDE.md](/CLAUDE.md).

## Testing Framework

**Framework**: [Kotest](https://kotest.io/docs/framework/framework.html) for pure kotlin classes.
Kotest + [orbit-test](https://orbit-mvi.org/Test/)


**Location**: `composeApp/src/commonTest/kotlin/ru/mironov/pwgen/`

## Running Tests

### All Tests
```bash
# Windows
gradlew.bat :composeApp:test

# macOS/Linux
./gradlew :composeApp:test
```

### Platform-Specific Tests
```bash
# Android unit tests
./gradlew :composeApp:testDebugUnitTest

# iOS tests (on macOS)
./gradlew :composeApp:iosSimulatorArm64Test
```

## Writing Tests

### Test Structure (StringSpec)

Use StringSpec for BDD-style tests with natural language descriptions:

```kotlin
class PasswordGeneratorTest : StringSpec({
    "should generate password with specified length" {
        val generator = PasswordGeneratorSecureRandomImpl()
        val password = generator.generate(10)

        password shouldHaveLength 10
    }
})
```

### Kotest Assertions

Common assertion patterns:

```kotlin
// Equality
value shouldBe expected
value shouldNotBe unexpected

// Strings
string shouldHaveLength 10
string shouldContain "substring"
string shouldStartWith "prefix"
string shouldEndWith "suffix"
string shouldBeEmpty()
string shouldNotBeEmpty()

// Collections
list shouldContain element
list shouldContainAll listOf(elem1, elem2)
list shouldHaveSize 5
list.shouldBeEmpty()

// Numbers
number shouldBeGreaterThan 5
number shouldBeLessThan 10
number shouldBeInRange 1..100

// Exceptions
shouldThrow<IllegalArgumentException> {
    // code that should throw
}

// Nullability
value.shouldNotBeNull()
value.shouldBeNull()
```

## Testing ViewModels (MVI with Orbit)

The project uses [orbit-test](https://orbit-mvi.org/Test/) which builds on [Turbine](https://github.com/cashapp/turbine) and Kotlin's coroutine testing APIs.

### Basic ViewModel Test

Put your ContainerHost into test mode using the `test()` function. Initial state is automatically verified:

```kotlin
import org.orbitmvi.orbit.test
import kotlinx.coroutines.test.runTest

class MainViewModelTest : StringSpec({

    "should update password length in state" {
        runTest {
            val viewModel = MainViewModel(mockGenerator)

            viewModel.test(this, MainState()) {
                containerHost.changePasswordLength(15)

                expectState { copy(passwordGenerationSettings = passwordGenerationSettings.copy(length = 15)) }
            }
        }
    }
})
```

**Note**: You can disable automatic initial state verification with `settings = TestSettings(autoCheckInitialState = false)`.

### Testing State Changes

Use `expectState()` to verify state transformations. Prefer using `copy()` for immutable state updates:

```kotlin
"should update state when intent is called" {
    runTest {
        val viewModel = MainViewModel(generator)

        viewModel.test(this, MainState()) {
            containerHost.changePasswordLength(20)

            expectState { copy(passwordGenerationSettings = passwordGenerationSettings.copy(length = 20)) }
        }
    }
}
```

**Alternatives**:
```kotlin
// With Kotest assertions
expectState { state -> state.length shouldBe 20 }

// With awaitState
val state = awaitState()
state.length shouldBe 20

// For sealed class states
expectStateOn<State.Ready> { copy(data = "updated") }
```

### Testing Side Effects

```kotlin
"should post side effect" {
    runTest {
        val viewModel = MainViewModel(mockGenerator)

        viewModel.test(this, MainState()) {
            containerHost.generatePassword()

            expectSideEffect(MainEffect.ShowError("Error message"))
        }
    }
}
```

### Testing onCreate Lambda

For ViewModels with onCreate lambda, manually invoke with `runOnCreate()`:

```kotlin
"should load data on create" {
    runTest {
        val viewModel = MyViewModel()

        viewModel.test(this) {
            runOnCreate()  // Must be called first!

            expectState { copy(isLoading = false, data = expectedData) }
        }
    }
}
```

**Note**: Only use `runOnCreate()` when testing onCreate lambda intents. Otherwise, set initial state directly.

### Testing Intent Jobs

For intents without state/side effects, use `join()`:

```kotlin
"should complete work" {
    runTest {
        viewModel.test(this, State()) {
            val job = containerHost.doWork()
            job.join()

            dependency.value shouldBe expected
        }
    }
}
```

### Orbit Test Validation Rules

**Important**: orbit-test performs strict validation to catch bugs early:

1. **All states and side effects must be consumed** - Any unconsumed items will cause test failure
2. **All intents must complete** - Unfinished intents fail the test (use `join()` or cancel them)
3. **Initial state is automatically verified** - No need to assert it explicitly

If you need to skip unconsumed items:

```kotlin
// Skip specific number of items
skip(2)

// Cancel test and ignore remaining items (use as last resort)
cancelAndIgnoreRemainingItems()
```

**Best Practice**: Be explicit about all assertions. Don't ignore unconsumed items unless absolutely necessary—this prevents unexpected bugs.

### Testing Infinite Flows

Replace infinite flows with finite test flows:

```kotlin
val testFlow = flowOf(item1, item2, item3)
val viewModel = MyViewModel(testFlow)

viewModel.test(this, State()) {
    val job = containerHost.observeData()
    job.join()

    expectState { copy(items = listOf(item1, item2, item3)) }
}
```

### Virtual Time Control

For time-based logic (debouncing, throttling), use separate `TestScope`:

```kotlin
"should process time-based events" {
    val scope = TestScope()

    viewModel.test(scope, State()) {
        val job = containerHost.startPeriodicUpdates()
        scope.advanceTimeBy(30_000)

        expectState { copy(count = 3) }
        job.cancel()
    }
}
```

## Testing with Koin

### Setting Up Test DI

```kotlin
beforeTest {
    startKoin { modules(testModule) }
}

afterTest {
    stopKoin()
}

"should inject dependencies" {
    val service = get<PasswordGenerator>()
    service.shouldNotBeNull()
}
```

## Testing Coroutines

```kotlin
"should handle async operations" {
    runTest {
        val result = async { performAsyncWork() }.await()

        result shouldBe expected
    }
}
```

## Best Practices

### General Testing

1. **Use descriptive test names**: Write test names in natural language that describe the behavior being tested
2. **Arrange-Act-Assert**: Structure tests with clear setup, execution, and verification phases
3. **Test one thing per test**: Each test should verify a single behavior
4. **Use shouldBe instead of assertEquals**: Kotest assertions provide better error messages
5. **Mock external dependencies**: Use mockk or test doubles for dependencies
6. **Test edge cases**: Include tests for boundary conditions and error scenarios
7. **Keep tests fast**: Avoid unnecessary delays or heavy operations

### Orbit-Test Specific

1. **Always wrap tests in runTest**: Pass the TestScope to `test()` function: `viewModel.test(this, State()) { }`
2. **Avoid calling runOnCreate() unnecessarily**: Only use it when testing onCreate lambda intents. Set correct initial state for other cases
3. **Be explicit about all assertions**: Consume all emitted states and side effects—this prevents unexpected bugs
4. **Use copy() for state verification**: Prefer `expectState { copy(field = newValue) }` for better immutability tracking
5. **Use expectStateOn<T>() for sealed classes**: Avoid type-casting by using this helper for sealed class hierarchies
6. **Join intent jobs**: If intents don't emit state/effects, use `job.join()` to ensure completion
7. **Handle infinite flows**: Replace with finite test flows or use virtual time control
8. **Don't ignore unconsumed items**: Use `cancelAndIgnoreRemainingItems()` only as a last resort

## Common Test Patterns

### Testing Domain Logic

```kotlin
"should generate password with constraints" {
    val password = generator.generate(settings)

    password shouldHaveLength 16
    password.any { it.isUpperCase() } shouldBe true
    password.any { it.isDigit() } shouldBe true
}
```

### Testing Validation

```kotlin
"should reject invalid input" {
    shouldThrow<IllegalArgumentException> {
        generator.generate(invalidSettings)
    }
}
```

## Test Organization

Organize tests by feature or component:

```
composeApp/src/commonTest/kotlin/ru/mironov/pwgen/
├── domain/
│   └── PasswordGeneratorTest.kt
├── presentation/
│   └── main/
│       └── MainViewModelTest.kt
└── utils/
    └── ValidationUtilsTest.kt
```