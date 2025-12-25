package ru.mironov.pwgen.domain

import kotlinx.coroutines.test.runTest
import ru.mironov.pwgen.domain.models.PasswordGenerationSettings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class PasswordGeneratorSecureRandomImplTest {

    private val generator = PasswordGeneratorSecureRandomImpl()

    private val uppercaseLetters = ('A'..'Z').toSet()
    private val lowercaseLetters = ('a'..'z').toSet()
    private val digits = ('0'..'9').toSet()
    private val specialCharacters = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~".toSet()

    // Length tests
    @Test
    fun generatePassword_withDefaultSettings_returnsPasswordWithCorrectLength() = runTest {
        val settings = PasswordGenerationSettings(length = 12)
        val password = generator.generatePassword(settings)
        assertEquals(12, password.length, "Password length should match the requested length")
    }

    @Test
    fun generatePassword_withLengthOne_returnsSingleCharacter() = runTest {
        val settings = PasswordGenerationSettings(length = 1)
        val password = generator.generatePassword(settings)
        assertEquals(1, password.length, "Password should have exactly 1 character")
    }

    @Test
    fun generatePassword_withLargeLength_handlesCorrectly() = runTest {
        val settings = PasswordGenerationSettings(length = 1000)
        val password = generator.generatePassword(settings)
        assertEquals(1000, password.length, "Should handle large password length")
    }

    // Validation tests
    @Test
    fun generatePassword_withNegativeLength_throwsException() = runTest {
        val settings = PasswordGenerationSettings(length = -1)
        assertFailsWith<IllegalArgumentException> {
            generator.generatePassword(settings)
        }
    }

    @Test
    fun generatePassword_withZeroLength_throwsException() = runTest {
        val settings = PasswordGenerationSettings(length = 0)
        assertFailsWith<IllegalArgumentException> {
            generator.generatePassword(settings)
        }
    }

    // Character set tests - only letters (default)
    @Test
    fun generatePassword_withDefaultSettings_containsOnlyLetters() = runTest {
        val settings = PasswordGenerationSettings(
            length = 100,
            digitsIncluded = false,
            specialCharactersIncluded = false,
        )
        val password = generator.generatePassword(settings)

        password.forEach { char ->
            assertTrue(
                char in uppercaseLetters || char in lowercaseLetters,
                "Character '$char' should be a letter when digits and special chars are disabled"
            )
        }
    }

    // Tests with digits enabled
    @Test
    fun generatePassword_withDigitsEnabled_containsOnlyLettersAndDigits() = runTest {
        val settings = PasswordGenerationSettings(
            length = 100,
            digitsIncluded = true
        )
        val password = generator.generatePassword(settings)

        password.forEach { char ->
            assertTrue(
                char in uppercaseLetters || char in lowercaseLetters || char in digits,
                "Character '$char' should be a letter or digit"
            )
        }
    }

    @Test
    fun generatePassword_withDigitsEnabled_eventuallyContainsDigit() = runTest {
        val settings = PasswordGenerationSettings(
            length = 100,
            digitsIncluded = true
        )
        val password = generator.generatePassword(settings)

        assertTrue(
            password.any { it in digits },
            "Password should contain at least one digit with high probability for length 100"
        )
    }

    @Test
    fun generatePassword_withDigitsEnabled_doesNotContainSpecialCharacters() = runTest {
        val settings = PasswordGenerationSettings(
            length = 100,
            digitsIncluded = true
        )
        val password = generator.generatePassword(settings)

        assertFalse(
            password.any { it in specialCharacters },
            "Password should not contain special characters when specialCharactersIncluded is false"
        )
    }

    // Tests with special characters enabled
    @Test
    fun generatePassword_withSpecialCharsEnabled_containsOnlyLettersAndSpecialChars() = runTest {
        val settings = PasswordGenerationSettings(
            length = 100,
            specialCharactersIncluded = true
        )
        val password = generator.generatePassword(settings)

        password.forEach { char ->
            assertTrue(
                char in uppercaseLetters || char in lowercaseLetters || char in specialCharacters,
                "Character '$char' should be a letter or special character"
            )
        }
    }

    @Test
    fun generatePassword_withSpecialCharsEnabled_eventuallyContainsSpecialChar() = runTest {
        val settings = PasswordGenerationSettings(
            length = 100,
            specialCharactersIncluded = true
        )
        val password = generator.generatePassword(settings)

        assertTrue(
            password.any { it in specialCharacters },
            "Password should contain at least one special character with high probability for length 100"
        )
    }

    @Test
    fun generatePassword_withSpecialCharsEnabled_doesNotContainDigits() = runTest {
        val settings = PasswordGenerationSettings(
            length = 100,
            specialCharactersIncluded = true
        )
        val password = generator.generatePassword(settings)

        assertFalse(
            password.any { it in digits },
            "Password should not contain digits when digitsIncluded is false"
        )
    }

    // Tests with all character types enabled
    @Test
    fun generatePassword_withAllTypesEnabled_containsAllCharacterTypes() = runTest {
        val settings = PasswordGenerationSettings(
            length = 100,
            digitsIncluded = true,
            specialCharactersIncluded = true
        )
        val password = generator.generatePassword(settings)

        password.forEach { char ->
            assertTrue(
                char in uppercaseLetters || char in lowercaseLetters ||
                char in digits || char in specialCharacters,
                "Character '$char' should be from one of the enabled character sets"
            )
        }
    }

    @Test
    fun generatePassword_withAllTypesEnabled_eventuallyContainsAllTypes() = runTest {
        val settings = PasswordGenerationSettings(
            length = 200,
            digitsIncluded = true,
            specialCharactersIncluded = true
        )
        val password = generator.generatePassword(settings)

        assertTrue(
            password.any { it in uppercaseLetters },
            "Password should contain uppercase letters"
        )
        assertTrue(
            password.any { it in lowercaseLetters },
            "Password should contain lowercase letters"
        )
        assertTrue(
            password.any { it in digits },
            "Password should contain digits with high probability for length 200"
        )
        assertTrue(
            password.any { it in specialCharacters },
            "Password should contain special characters with high probability for length 200"
        )
    }

    // Randomness tests
    @Test
    fun generatePassword_multipleCallsProduceDifferentPasswords() = runTest {
        val settings = PasswordGenerationSettings(length = 20)
        val password1 = generator.generatePassword(settings)
        val password2 = generator.generatePassword(settings)
        val password3 = generator.generatePassword(settings)

        assertNotEquals(
            password1,
            password2,
            "Multiple calls should generate different passwords (randomness check)"
        )
        assertNotEquals(
            password2,
            password3,
            "Multiple calls should generate different passwords (randomness check)"
        )
    }
}