package de.tomjuri.gemdigger

import cc.polyfrost.oneconfig.utils.commands.annotations.Command
import cc.polyfrost.oneconfig.utils.commands.annotations.Main

@Command(value = "reloadroute")
class ResetRouteCommand {

    @Main
    fun main() {
        GemDigger.routeManager.reloadRoute()
    }
}