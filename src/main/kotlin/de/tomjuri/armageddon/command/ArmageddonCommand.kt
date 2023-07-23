package de.tomjuri.armageddon.command

import cc.polyfrost.oneconfig.utils.commands.annotations.Command
import cc.polyfrost.oneconfig.utils.commands.annotations.Main
import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand
import de.tomjuri.armageddon.Armageddon
import de.tomjuri.armageddon.util.Logger

@Command(value = "armageddon")
class ArmageddonCommand {

    @Main(description = "Opens the GUI")
    private fun main() {
        Armageddon.config.openGui()
    }

    @SubCommand(description = "Reloads the route")
    private fun reload() {
        Armageddon.routeManager.reloadRoute()
    }

    @SubCommand(description = "Toggles the waypoints")
    private fun waypoints() {
        if(Armageddon.config.showWaypoints) {
            Armageddon.config.showWaypoints = false
            Logger.info("Waypoints are now disabled.")
        } else {
            Armageddon.config.showWaypoints = true
            Logger.info("Waypoints are now enabled.")
        }
    }
}