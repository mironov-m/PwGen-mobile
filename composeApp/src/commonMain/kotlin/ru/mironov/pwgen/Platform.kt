package ru.mironov.pwgen

interface Platform {

    val name: String
}

expect fun getPlatform(): Platform