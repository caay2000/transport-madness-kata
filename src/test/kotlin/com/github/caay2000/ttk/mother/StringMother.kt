package com.github.caay2000.ttk.mother

object StringMother {

    fun random(): String = randomString

    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z')

    private val randomString = (1..24)
        .map { kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}
