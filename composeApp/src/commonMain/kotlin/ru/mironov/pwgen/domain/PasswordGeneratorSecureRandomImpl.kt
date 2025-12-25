package ru.mironov.pwgen.domain

class PasswordGeneratorSecureRandomImpl : PasswordGenerator  {

    override suspend fun generatePassword(length: Int): String {
        TODO("Secure password generation")
    }
}