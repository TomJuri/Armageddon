package de.tomjuri.armageddon.command

import cc.polyfrost.oneconfig.utils.commands.annotations.Command
import cc.polyfrost.oneconfig.utils.commands.annotations.Main
import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand
import de.tomjuri.armageddon.util.config
import de.tomjuri.armageddon.util.failsafe
import de.tomjuri.armageddon.util.routeManager

@Command(value = "armageddon")
class ArmageddonCommand {

    @Main(description = "Opens click GUI.")
    private fun main() {
        config.openGui()
    }

    @SubCommand(description = "Reloads the route.")
    private fun reload() {
        routeManager.reloadRoute()
    }

    @SubCommand(description = "Toggles waypoints.")
    private fun waypoints() {
        if (config.showWaypoints) {
            config.showWaypoints = false
            de.tomjuri.armageddon.util.Logger.info("Waypoints are now disabled.")
        } else {
            config.showWaypoints = true
            de.tomjuri.armageddon.util.Logger.info("Waypoints are now enabled.")
        }
    }

    @SubCommand(description = "Test the failsafe.")
    private fun testfailsafe() {
        failsafe.emergency("This is a test failsafe message.", failsafe.failsafeMovement)
    }
}