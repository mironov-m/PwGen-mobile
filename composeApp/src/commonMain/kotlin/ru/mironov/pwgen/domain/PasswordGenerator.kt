package ru.mironov.pwgen.domain

import ru.mironov.pwgen.domain.models.PasswordGenerationSettings

interface PasswordGenerator {

    suspend fun generatePassword(generationSettings: PasswordGenerationSettings): String
}