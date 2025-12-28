package ru.mironov.pwgen.ui.screens.main.presentation

import ru.mironov.pwgen.domain.models.PasswordGenerationSettings

data class MainState(
    val passwordGenerationSettings: PasswordGenerationSettings = PasswordGenerationSettings(),
    val minLength: Int = 6,
    val maxLength: Int = 20,
    val passwordsCount: Int = 10,
    val passwords: List<String> = emptyList(),
) {

    val lengthDelta = maxLength - minLength
}
