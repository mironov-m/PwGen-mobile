package ru.mironov.pwgen.domain

import kotlinx.coroutines.coroutineScope
import ru.mironov.pwgen.domain.models.PasswordGenerationSettings

class PasswordGeneratorSecureRandomImpl : PasswordGenerator  {

    private val uppercaseLetters = ('A'..'Z').toList()
    private val lowercaseLetters = ('a'..'z').toList()
    private val digits = ('0'..'9').toList()
    private val specialCharacters = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~".toList()

    override suspend fun generatePassword(generationSettings: PasswordGenerationSettings): String {
        if (generationSettings.length <= 0) {
            throw IllegalArgumentException("Length must be positive")
        }

        val availableCharacters = getAvailableCharacters(generationSettings)

        val sb = StringBuilder(generationSettings.length)
        sb.append(" ".repeat(generationSettings.length))
        coroutineScope {
            repeat(generationSettings.length) { index ->
                // TODO: replace random by secure random
                sb[index] = availableCharacters.random()
            }
        }
        return sb.toString()
    }

    private fun getAvailableCharacters(generationSettings: PasswordGenerationSettings): List<Char> = buildList {
        addAll(uppercaseLetters)
        addAll(lowercaseLetters)
        if (generationSettings.digitsIncluded) {
            addAll(digits)
        }
        if (generationSettings.specialCharactersIncluded) {
            addAll(specialCharacters)
        }
    }
}