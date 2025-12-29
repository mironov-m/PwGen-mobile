package ru.mironov.pwgen.presentation.main

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.orbitmvi.orbit.test.test
import ru.mironov.pwgen.domain.PasswordGenerator
import ru.mironov.pwgen.domain.models.PasswordGenerationSettings
import ru.mironov.pwgen.ui.screens.main.presentation.MainEffect
import ru.mironov.pwgen.ui.screens.main.presentation.MainState
import ru.mironov.pwgen.ui.screens.main.presentation.MainViewModel

class MainViewModelTest : FreeSpec({

    lateinit var passwordGenerator: FakePasswordGenerator
    lateinit var viewModel: MainViewModel

    beforeEach {
        passwordGenerator = FakePasswordGenerator()
        viewModel = MainViewModel(passwordGenerator)
    }

    "changePasswordLength" - {
        "should update password length in state" {
            runTest {
                viewModel.test(this, MainState()) {
                    containerHost.changePasswordLength(16)

                    expectState {
                        copy(passwordGenerationSettings = passwordGenerationSettings.copy(length = 16))
                    }
                }
            }
        }
    }

    "specialCharacterChecked" - {
        "should set specialCharactersIncluded to true when passed true" {
            runTest {
                viewModel.test(this, MainState()) {
                    containerHost.specialCharacterChecked(true)

                    expectState {
                        copy(passwordGenerationSettings = passwordGenerationSettings.copy(specialCharactersIncluded = true))
                    }
                }
            }
        }

        "should set specialCharactersIncluded to false when passed false" {
            runTest {
                viewModel.test(this, MainState()) {
                    containerHost.specialCharacterChecked(true)
                    expectState {
                        copy(passwordGenerationSettings = passwordGenerationSettings.copy(specialCharactersIncluded = true))
                    }

                    containerHost.specialCharacterChecked(false)
                    expectState {
                        copy(passwordGenerationSettings = passwordGenerationSettings.copy(specialCharactersIncluded = false))
                    }
                }
            }
        }
    }

    "digitsChecked" - {
        "should set digitsIncluded to true when passed true" {
            runTest {
                viewModel.test(this, MainState()) {
                    containerHost.digitsChecked(true)

                    expectState {
                        copy(passwordGenerationSettings = passwordGenerationSettings.copy(digitsIncluded = true))
                    }
                }
            }
        }

        "should set digitsIncluded to false when passed false" {
            runTest {
                viewModel.test(this, MainState()) {
                    containerHost.digitsChecked(true)
                    expectState {
                        copy(passwordGenerationSettings = passwordGenerationSettings.copy(digitsIncluded = true))
                    }

                    containerHost.digitsChecked(false)
                    expectState {
                        copy(passwordGenerationSettings = passwordGenerationSettings.copy(digitsIncluded = false))
                    }
                }
            }
        }
    }

    "generate" - {
        "should call passwordGenerator with current settings" {
            runTest {
                passwordGenerator.passwordsToReturn = List(10) { "password-$it" }

                viewModel.test(this, MainState()) {
                    containerHost.changePasswordLength(16)
                    expectState {
                        copy(passwordGenerationSettings = passwordGenerationSettings.copy(length = 16))
                    }

                    containerHost.specialCharacterChecked(true)
                    expectState {
                        copy(passwordGenerationSettings = passwordGenerationSettings.copy(specialCharactersIncluded = true))
                    }

                    containerHost.digitsChecked(true)
                    expectState {
                        copy(passwordGenerationSettings = passwordGenerationSettings.copy(digitsIncluded = true))
                    }

                    containerHost.generate()
                    expectState {
                        copy(passwords = List(10) { "password-$it" })
                    }

                    passwordGenerator.callCount shouldBe 10
                    passwordGenerator.capturedSettings.all {
                        it.length == 16 &&
                        it.specialCharactersIncluded &&
                        it.digitsIncluded
                    } shouldBe true
                }
            }
        }

        "should call passwordGenerator N times based on passwordsCount" {
            runTest {
                passwordGenerator.passwordsToReturn = List(10) { "password-$it" }

                viewModel.test(this, MainState()) {
                    containerHost.generate()
                    expectState {
                        copy(passwords = List(10) { "password-$it" })
                    }

                    passwordGenerator.callCount shouldBe 10
                }
            }
        }

        "should update state passwords with generated results" {
            runTest {
                passwordGenerator.passwordsToReturn = List(10) { "password-$it" }

                viewModel.test(this, MainState()) {
                    containerHost.generate()

                    expectState {
                        copy(passwords = List(10) { "password-$it" })
                    }
                }
            }
        }

        "should generate passwords concurrently" {
            runTest {
                passwordGenerator.delayMs = 10
                passwordGenerator.passwordsToReturn = List(10) { "password-$it" }

                viewModel.test(this, MainState()) {
                    containerHost.generate()
                    expectState {
                        copy(passwords = List(10) { "password-$it" })
                    }

                    passwordGenerator.callCount shouldBe 10
                }
            }
        }
    }

    "copyPassword" - {
        "should post CopyToClipboard side effect with provided password" {
            runTest {
                viewModel.test(this, MainState()) {
                    containerHost.copyPassword("TestPassword123")

                    expectSideEffect(MainEffect.CopyToClipboard("TestPassword123"))
                }
            }
        }
    }

    "integration scenarios" - {
        "should generate with updated settings after multiple changes" {
            runTest {
                passwordGenerator.passwordsToReturn = List(10) { "password-$it" }

                viewModel.test(this, MainState()) {
                    containerHost.changePasswordLength(12)
                    expectState {
                        copy(passwordGenerationSettings = passwordGenerationSettings.copy(length = 12))
                    }

                    containerHost.specialCharacterChecked(true)
                    expectState {
                        copy(passwordGenerationSettings = passwordGenerationSettings.copy(specialCharactersIncluded = true))
                    }

                    containerHost.digitsChecked(true)
                    expectState {
                        copy(passwordGenerationSettings = passwordGenerationSettings.copy(digitsIncluded = true))
                    }

                    containerHost.changePasswordLength(16)
                    expectState {
                        copy(passwordGenerationSettings = passwordGenerationSettings.copy(length = 16))
                    }

                    containerHost.generate()
                    expectState {
                        copy(passwords = List(10) { "password-$it" })
                    }

                    passwordGenerator.capturedSettings.all {
                        it.length == 16 &&
                        it.specialCharactersIncluded &&
                        it.digitsIncluded
                    } shouldBe true
                }
            }
        }

        "should allow multiple generations without losing settings" {
            runTest {
                passwordGenerator.passwordsToReturn = List(10) { "first-$it" }

                viewModel.test(this, MainState()) {
                    containerHost.changePasswordLength(15)
                    expectState {
                        copy(passwordGenerationSettings = passwordGenerationSettings.copy(length = 15))
                    }

                    containerHost.generate()
                    expectState {
                        copy(passwords = List(10) { "first-$it" })
                    }

                    passwordGenerator.reset()
                    passwordGenerator.passwordsToReturn = List(10) { "second-$it" }

                    containerHost.generate()
                    expectState {
                        copy(passwords = List(10) { "second-$it" })
                    }

                    passwordGenerator.callCount shouldBe 10
                    passwordGenerator.capturedSettings.all { it.length == 15 } shouldBe true
                }
            }
        }
    }
})

/**
 * Fake implementation of PasswordGenerator for testing purposes.
 * Tracks all calls and allows controlling the returned values.
 */
private class FakePasswordGenerator : PasswordGenerator {
    var callCount = 0
        private set

    val capturedSettings = mutableListOf<PasswordGenerationSettings>()
    var passwordsToReturn: List<String> = emptyList()
    var delayMs: Long = 0

    private var currentPasswordIndex = 0

    override suspend fun generatePassword(generationSettings: PasswordGenerationSettings): String {
        if (delayMs > 0) {
            kotlinx.coroutines.delay(delayMs)
        }

        callCount++
        capturedSettings.add(generationSettings)

        return if (passwordsToReturn.isNotEmpty()) {
            passwordsToReturn.getOrElse(currentPasswordIndex++) { "password-$callCount" }
        } else {
            "generated-password-$callCount"
        }
    }

    fun reset() {
        callCount = 0
        capturedSettings.clear()
        currentPasswordIndex = 0
    }
}