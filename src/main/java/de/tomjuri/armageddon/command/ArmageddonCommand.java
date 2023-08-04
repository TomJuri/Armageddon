package de.tomjuri.armageddon.command;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand;
import de.tomjuri.armageddon.Armageddon;
import de.tomjuri.armageddon.config.ArmageddonConfig;
import de.tomjuri.armageddon.macro.RouteManager;
import de.tomjuri.armageddon.util.Logger;

@Command(value = "armageddon")
public class ArmageddonCommand {

    @Main(description = "Opens click GUI.")
    private void main() {
        Armageddon.INSTANCE.openGUI();
    }

    @SubCommand(description = "Reloads the route.")
    private void reload() {
        RouteManager.reloadRoute();
    }

    @SubCommand(description = "Toggles waypoints.")
    private void waypoints() {
        if(ArmageddonConfig.showWaypoints) {
            ArmageddonConfig.showWaypoints = false;
            Logger.info("Waypoints are now disabled.");
        } else {
            ArmageddonConfig.showWaypoints = true;
            Logger.info("Waypoints are now enabled.");
        }
    }
}