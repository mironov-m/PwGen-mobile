package ru.mironov.pwgen.ui.screens.main.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.mironov.pwgen.domain.PasswordGenerator

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

    fun specialCharacterChecked(checked: Boolean) = intent {
        reduce {
            val newSettings = state.passwordGenerationSettings.copy(specialCharactersIncluded = checked)
            state.copy(passwordGenerationSettings = newSettings)
        }
    }

    fun digitsChecked(checked: Boolean) = intent {
        reduce {
            val newSettings = state.passwordGenerationSettings.copy(digitsIncluded = checked)
            state.copy(passwordGenerationSettings = newSettings)
        }
    }

    fun generate() = intent {
        val passwords = coroutineScope {
            List(state.passwordsCount) {
                async {
                    passwordGenerator.generatePassword(state.passwordGenerationSettings)
                }

            }.awaitAll()
        }
        reduce {
            state.copy(passwords = passwords)
        }
    }

    fun copyPassword(password: String) = intent {
        postSideEffect(MainEffect.CopyToClipboard(password))
    }
}