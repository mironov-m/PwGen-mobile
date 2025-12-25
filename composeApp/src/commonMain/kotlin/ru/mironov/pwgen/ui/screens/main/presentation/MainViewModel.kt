package ru.mironov.pwgen.ui.screens.main.presentation

import androidx.lifecycle.ViewModel
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
}