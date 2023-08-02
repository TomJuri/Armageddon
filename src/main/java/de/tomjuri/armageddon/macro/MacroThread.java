package de.tomjuri.armageddon.macro;

import de.tomjuri.armageddon.config.ArmageddonConfig;
import de.tomjuri.armageddon.util.*;
import lombok.AllArgsConstructor;

public class MacroThread extends Thread {

    @Override
    public void run() {
        while (true) {
            // Switch to rod
            Macro.state = Macro.State.SWITCH_TO_ROD;
            Ref.player().inventory.currentItem = ArmageddonConfig.rodSlot - 1;
            SleepUtil.sleep(100);

            // Summon dillo
            Macro.state = Macro.State.SUMMON_DILLO;
            KeyBindUtil.rightClick();
            SleepUtil.sleep(100);

            // Switch to drill
            Macro.state = Macro.State.SWITCH_TO_DRILL;
            Ref.player().inventory.currentItem = ArmageddonConfig.drillSlot - 1;
            SleepUtil.sleep(100);

            // Normalize pitch
            Macro.state = Macro.State.NORMALIZE_PITCH;
            RotationUtil.ease(new RotationUtil.Rotation(Ref.player().rotationYaw, -3f), 100);
            SleepUtil.sleep(100);

            // Mount dillo
            Macro.state = Macro.State.MOUNT_DILLO;
            KeyBindUtil.rightClick();
            SleepUtil.sleep(200);
            if(!Ref.player().isRiding()) {
                Logger.error("Playing is not riding the armadillo!");
                Macro.stop();
                break;
            }

            // Mine
            Macro.state = Macro.State.MINE;
            KeyBindUtil.setKey(Ref.mc().gameSettings.keyBindJump.getKeyCode(), true);
            RotationUtil.easeCertain(new RotationUtil.Rotation(ArmageddonConfig.swipeRange, Ref.player().rotationPitch), ArmageddonConfig.swipeTime);
            SleepUtil.sleep(100);
            KeyBindUtil.setKey(Ref.mc().gameSettings.keyBindJump.getKeyCode(), false);
            SleepUtil.sleep(ArmageddonConfig.swipeTime - 100);

            // Switch to rod again
            Macro.state = Macro.State.SWITCH_TO_ROD_AGAIN;
            Ref.player().inventory.currentItem = ArmageddonConfig.rodSlot - 1;
            SleepUtil.sleep(100);

            // Desummon dillo
            Macro.state = Macro.State.DESUMMON_DILLO;
            KeyBindUtil.rightClick();
            SleepUtil.sleep(100);
            if(Ref.player().isRiding()) {
                Logger.error("Playing is still riding the armadillo!");
                Macro.stop();
                break;
            }

            // Switch to drill again
            Macro.state = Macro.State.SWITCH_TO_DRILL_AGAIN;
            Ref.player().inventory.currentItem = ArmageddonConfig.drillSlot - 1;
            SleepUtil.sleep(100);

            // Switch to AOTV
            Macro.state = Macro.State.SWITCH_TO_AOTV;
            Ref.player().inventory.currentItem = ArmageddonConfig.aotvSlot - 1;
            SleepUtil.sleep(100);

            // Sneak
            Macro.state = Macro.State.SNEAK;
            KeyBindUtil.setKey(Ref.mc().gameSettings.keyBindSneak.getKeyCode(), true);
            SleepUtil.sleep(100);

            // Look at block
            Macro.state = Macro.State.LOOK_AT_BLOCK;
            if (RouteManager.getNext() == null) {
                Logger.error("Something went really wrong...");
                Macro.stop();
                break;
            }
            RotationUtil.Rotation rotation = AngleUtil.getRoationToLookAt(RouteManager.getNext());
            RotationUtil.ease(rotation, ArmageddonConfig.lookAtBlockTime);
            SleepUtil.sleep(ArmageddonConfig.lookAtBlockTime);

            // Teleport
            Macro.state = Macro.State.TELEPORT;
            KeyBindUtil.rightClick();
            SleepUtil.sleep(100);
            RouteManager.current++;
            if(RouteManager.getStandingOn() != RouteManager.current) {
                Logger.error("Did not arrive at next waypoint!");
                Macro.stop();
                break;
            }

            // Unsneak
            Macro.state = Macro.State.UNSNEAK;
            KeyBindUtil.setKey(Ref.mc().gameSettings.keyBindSneak.getKeyCode(), false);
            SleepUtil.sleep(100);
        }
    }
}
