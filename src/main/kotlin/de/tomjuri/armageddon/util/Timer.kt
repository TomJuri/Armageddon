package de.tomjuri.armageddon.util

class Timer(millis: Long) {
    private val endTime = System.currentTimeMillis() + millis
    fun isOver() = System.currentTimeMillis() >= endTime
}