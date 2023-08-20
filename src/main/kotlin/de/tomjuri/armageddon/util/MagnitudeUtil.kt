package de.tomjuri.armageddon.util

object MagnitudeUtil {
    fun magnitudify(num: Long): String =
        if(num < 1_000) num.toString()
        else if(num < 1_000_000) "${num / 1000}K"
        else if(num < 1_000_000_000) "${num / 1000000}M"
        else if(num < 1_000_000_000_000) "${num / 1000000000}B"
        else "error"
    fun msToTime(ms: Long): String {
        val seconds = ms / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        return "${hours % 24}:${minutes % 60}:${seconds % 60}"
    }
}