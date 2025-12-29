package ru.mironov.pwgen.ui

interface ClipboardManager {
    fun copyToClipboard(text: String)
}

expect fun createClipboardManager(): ClipboardManager