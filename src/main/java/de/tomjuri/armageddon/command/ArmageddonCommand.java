package de.tomjuri.armageddon.command;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand;
import de.tomjuri.armageddon.Armageddon;
import de.tomjuri.armageddon.config.ArmageddonConfig;
import de.tomjuri.armageddon.macro.RouteManager;
import de.tomjuri.armageddon.util.Logger;
import de.tomjuri.armageddon.util.SoundUtil;

@Command(value = "armageddon")
public class ArmageddonCommand {

    @Main(description = "Opens click GUI.")
    private void main() {
        Armageddon.getInstance().getConfig().openGui();
    }

    @SubCommand(description = "Reloads the route.")
    private void reload() {
        Armageddon.getInstance().getRouteManager().reloadRoute();
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

    @SubCommand(description = "Test the failsafe.")
    private void testfailsafe() {
        Armageddon.getInstance().getFailsafe().emergency("This is a test failsafe message.", Armageddon.getInstance().getFailsafe().failsafeMovement);
    }

    @SubCommand(description = "Test the refuel.")
    private void testrefuel() {
        Armageddon.getInstance().getAbiphoneRefuel().shouldRefuel = true;
    }
}