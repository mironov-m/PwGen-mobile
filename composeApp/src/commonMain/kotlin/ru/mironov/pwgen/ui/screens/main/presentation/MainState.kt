package ru.mironov.pwgen.ui.screens.main.presentation

import ru.mironov.pwgen.domain.models.PasswordGenerationSettings

data class MainState(
    val passwordGenerationSettings: PasswordGenerationSettings = PasswordGenerationSettings(),
)
