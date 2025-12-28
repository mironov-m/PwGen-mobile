package ru.mironov.pwgen.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldHaveLength
import ru.mironov.pwgen.domain.models.PasswordGenerationSettings

class PasswordGeneratorSecureRandomImplTest : StringSpec({

    val generator = PasswordGeneratorSecureRandomImpl()

    val uppercaseLetters = ('A'..'Z').toSet()
    val lowercaseLetters = ('a'..'z').toSet()
    val digits = ('0'..'9').toSet()
    val specialCharacters = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~".toSet()

    // Length tests
    "generates password with correct length for default settings" {
        val settings = PasswordGenerationSettings(length = 12)
        val password = generator.generatePassword(settings)

        password shouldHaveLength 12
    }

    "generates single character password when length is one" {
        val settings = PasswordGenerationSettings(length = 1)
        val password = generator.generatePassword(settings)

        password shouldHaveLength 1
    }

    "handles large password length correctly" {
        val settings = PasswordGenerationSettings(length = 1000)
        val password = generator.generatePassword(settings)

        password shouldHaveLength 1000
    }

    // Validation tests
    "throws exception when length is negative" {
        val settings = PasswordGenerationSettings(length = -1)

        shouldThrow<IllegalArgumentException> {
            generator.generatePassword(settings)
        }
    }

    "throws exception when length is zero" {
        val settings = PasswordGenerationSettings(length = 0)

        shouldThrow<IllegalArgumentException> {
            generator.generatePassword(settings)
        }
    }

    // Character set tests - only letters (default)
    "contains only letters with default settings" {
        val settings = PasswordGenerationSettings(
            length = 100,
            digitsIncluded = false,
            specialCharactersIncluded = false,
        )
        val password = generator.generatePassword(settings)

        password.all { it in uppercaseLetters || it in lowercaseLetters } shouldBe true
    }

    // Tests with digits enabled
    "contains only letters and digits when digits enabled" {
        val settings = PasswordGenerationSettings(
            length = 100,
            digitsIncluded = true
        )
        val password = generator.generatePassword(settings)

        password.all { it in uppercaseLetters || it in lowercaseLetters || it in digits } shouldBe true
    }

    "eventually contains digit when digits enabled" {
        val settings = PasswordGenerationSettings(
            length = 100,
            digitsIncluded = true
        )
        val password = generator.generatePassword(settings)

        password.any { it in digits } shouldBe true
    }

    "does not contain special characters when digits enabled but special chars disabled" {
        val settings = PasswordGenerationSettings(
            length = 100,
            digitsIncluded = true
        )
        val password = generator.generatePassword(settings)

        password.any { it in specialCharacters } shouldBe false
    }

    // Tests with special characters enabled
    "contains only letters and special chars when special chars enabled" {
        val settings = PasswordGenerationSettings(
            length = 100,
            specialCharactersIncluded = true
        )
        val password = generator.generatePassword(settings)

        password.all { it in uppercaseLetters || it in lowercaseLetters || it in specialCharacters } shouldBe true
    }

    "eventually contains special char when special chars enabled" {
        val settings = PasswordGenerationSettings(
            length = 100,
            specialCharactersIncluded = true
        )
        val password = generator.generatePassword(settings)

        password.any { it in specialCharacters } shouldBe true
    }

    "does not contain digits when special chars enabled but digits disabled" {
        val settings = PasswordGenerationSettings(
            length = 100,
            specialCharactersIncluded = true
        )
        val password = generator.generatePassword(settings)

        password.any { it in digits } shouldBe false
    }

    // Tests with all character types enabled
    "contains all character types when all types enabled" {
        val settings = PasswordGenerationSettings(
            length = 100,
            digitsIncluded = true,
            specialCharactersIncluded = true
        )
        val password = generator.generatePassword(settings)

        password.all {
            it in uppercaseLetters || it in lowercaseLetters ||
            it in digits || it in specialCharacters
        } shouldBe true
    }

    "eventually contains all types when all types enabled" {
        val settings = PasswordGenerationSettings(
            length = 200,
            digitsIncluded = true,
            specialCharactersIncluded = true
        )
        val password = generator.generatePassword(settings)

        password.any { it in uppercaseLetters } shouldBe true
        password.any { it in lowercaseLetters } shouldBe true
        password.any { it in digits } shouldBe true
        password.any { it in specialCharacters } shouldBe true
    }

    // Randomness tests
    "multiple calls produce different passwords" {
        val settings = PasswordGenerationSettings(length = 20)
        val password1 = generator.generatePassword(settings)
        val password2 = generator.generatePassword(settings)
        val password3 = generator.generatePassword(settings)

        password1 shouldNotBe password2
        password2 shouldNotBe password3
    }
})