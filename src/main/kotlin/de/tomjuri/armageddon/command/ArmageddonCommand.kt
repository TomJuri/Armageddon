package de.tomjuri.armageddon.command

import cc.polyfrost.oneconfig.utils.commands.annotations.Command
import cc.polyfrost.oneconfig.utils.commands.annotations.Main
import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand
import de.tomjuri.armageddon.Armageddon
import de.tomjuri.armageddon.config.ArmageddonConfig

@Command(value = "armageddon")
class ArmageddonCommand {

    @Main(description = "Opens click GUI.")
    private fun main() {
        Armageddon.instance.config.openGui()
    }

    @SubCommand(description = "Reloads the route.")
    private fun reload() {
        Armageddon.instance.routeManager.reloadRoute()
    }

    @SubCommand(description = "Toggles waypoints.")
    private fun waypoints() {
        if (Armageddon.instance.config.showWaypoints) {
            Armageddon.instance.config.showWaypoints = false
            de.tomjuri.armageddon.util.Logger.info("Waypoints are now disabled.")
        } else {
            Armageddon.instance.config.showWaypoints = true
            de.tomjuri.armageddon.util.Logger.info("Waypoints are now enabled.")
        }
    }

    @SubCommand(description = "Test the failsafe.")
    private fun testfailsafe() {
        Armageddon.instance.failsafe.emergency("This is a test failsafe message.", Armageddon.instance.failsafe.failsafeMovement)
    }
}