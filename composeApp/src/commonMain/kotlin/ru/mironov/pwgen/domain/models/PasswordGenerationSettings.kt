package ru.mironov.pwgen.domain.models

/**
 * Represents the configuration settings for generating a new password.
 *
 * This entity defines the character sets that should be
 * considered by the password generation engine.
 *
 * @property length The desired number of characters in the generated password. Defaults to 8.
 * @property digitsIncluded Whether numeric characters (0-9) should be included in the password.
 * Defaults to false.
 * @property specialCharactersIncluded Whether symbols or special characters should be
 * included in the password. Defaults to false.
 */
data class PasswordGenerationSettings(
    val length: Int = 8,
    val digitsIncluded: Boolean = false,
    val specialCharactersIncluded: Boolean = false,
)
