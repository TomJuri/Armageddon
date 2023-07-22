package de.tomjuri.armageddon.command

import cc.polyfrost.oneconfig.utils.commands.annotations.Command
import cc.polyfrost.oneconfig.utils.commands.annotations.Main
import de.tomjuri.armageddon.Armageddon

@Command(value = "reloadroute")
class ReloadRouteCommand {

    @Main
    fun main() {
        Armageddon.routeManager.reloadRoute()
    }
}