package de.tomjuri.gemdigger.command

import cc.polyfrost.oneconfig.utils.commands.annotations.Command
import cc.polyfrost.oneconfig.utils.commands.annotations.Main
import de.tomjuri.gemdigger.GemDigger

@Command(value = "reloadroute")
class ResetRouteCommand {

    @Main
    fun main() {
        GemDigger.routeManager.reloadRoute()
    }
}