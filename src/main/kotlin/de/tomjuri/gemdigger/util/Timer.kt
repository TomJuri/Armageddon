package de.tomjuri.gemdigger.util

class Timer {

    private var endTime: Long = System.currentTimeMillis()

    fun startTimer(millis: Long) {
        endTime = System.currentTimeMillis() + millis
    }
    fun isDone(): Boolean {
        return System.currentTimeMillis() >= endTime
    }
}