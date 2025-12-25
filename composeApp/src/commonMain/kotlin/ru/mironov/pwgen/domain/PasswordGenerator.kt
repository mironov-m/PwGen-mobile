package ru.mironov.pwgen.domain

interface PasswordGenerator {

    suspend fun generatePassword(length: Int): String
}