package ru.mironov.pwgen.ui

import android.content.ClipData
import android.content.ClipboardManager as AndroidClipboardManager
import android.content.Context

class AndroidClipboardManager(private val context: Context) : ClipboardManager {
    override fun copyToClipboard(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as AndroidClipboardManager
        val clip = ClipData.newPlainText("password", text)
        clipboard.setPrimaryClip(clip)
    }
}

actual fun createClipboardManager(): ClipboardManager {
    throw IllegalStateException("ClipboardManager should be created via DI")
}