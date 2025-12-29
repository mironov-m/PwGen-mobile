package ru.mironov.pwgen.ui.screens.main.presentation

sealed interface MainEffect {

    data class CopyToClipboard(val text: String) : MainEffect
}