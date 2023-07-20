package de.tomjuri.gemdigger

import cc.polyfrost.oneconfig.utils.commands.annotations.Command
import cc.polyfrost.oneconfig.utils.commands.annotations.Main

@Command(value = "gdreset")
class ResetCommand {

    @Main
    fun main() {
        GemDigger.macro.current = 0
    }
}