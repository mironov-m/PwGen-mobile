package ru.mironov.pwgen.domain

import kotlin.random.Random

class PasswordGenerator constructor(
    private val seed: Long,
) {

    private val random = Random(seed)

    fun generatePassword(length: Int): String {
        TODO("Secure password generation")
    }
}