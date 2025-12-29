package ru.mironov.pwgen.ui

import platform.UIKit.UIPasteboard

class IOSClipboardManager : ClipboardManager {
    override fun copyToClipboard(text: String) {
        UIPasteboard.generalPasteboard.string = text
    }
}

actual fun createClipboardManager(): ClipboardManager = IOSClipboardManager()