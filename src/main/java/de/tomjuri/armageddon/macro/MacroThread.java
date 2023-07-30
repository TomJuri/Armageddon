package de.tomjuri.armageddon.macro;

import de.tomjuri.armageddon.Armageddon;
import de.tomjuri.armageddon.config.ArmageddonConfig;
import de.tomjuri.armageddon.util.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MacroThread extends Thread {

    private final Macro macro;

    @Override
    public void run() {
        while (true) {
            // Switch to rod
            macro.state = Macro.State.SWITCH_TO_ROD;
            Armageddon.INSTANCE.getPlayer().inventory.currentItem = ArmageddonConfig.rodSlot - 1;
            SleepUtil.sleep(100);

            // Summon dillo
            macro.state = Macro.State.SUMMON_DILLO;
            KeyBindUtil.rightClick();
            SleepUtil.sleep(100);

            // Switch to drill
            macro.state = Macro.State.SWITCH_TO_DRILL;
            Armageddon.INSTANCE.getPlayer().inventory.currentItem = ArmageddonConfig.drillSlot - 1;
            SleepUtil.sleep(200);

            // Normalize pitch
            macro.state = Macro.State.NORMALIZE_PITCH;
            RotationUtil.ease(new RotationUtil.Rotation(Armageddon.INSTANCE.getPlayer().rotationYaw, -3f), 100);
            SleepUtil.sleep(100);

            // Mount dillo
            macro.state = Macro.State.MOUNT_DILLO;
            KeyBindUtil.rightClick();
            SleepUtil.sleep(200);
            if(!Armageddon.INSTANCE.getPlayer().isRiding()) {
                Logger.error("Playing is not riding the armadillo!");
                Macro.stop();
                break;
            }

            // Mine
            macro.state = Macro.State.MINE;
            KeyBindUtil.setKey(Armageddon.INSTANCE.getMinecraft().gameSettings.keyBindJump.getKeyCode(), true);
            RotationUtil.easeCertain(new RotationUtil.Rotation(ArmageddonConfig.swipeRange, Armageddon.INSTANCE.getPlayer().rotationPitch), ArmageddonConfig.swipeTime);
            SleepUtil.sleep(100);
            KeyBindUtil.setKey(Armageddon.INSTANCE.getMinecraft().gameSettings.keyBindJump.getKeyCode(), false);
            SleepUtil.sleep(ArmageddonConfig.swipeTime - 100);

            // Switch to rod again
            macro.state = Macro.State.SWITCH_TO_ROD_AGAIN;
            Armageddon.INSTANCE.getPlayer().inventory.currentItem = ArmageddonConfig.rodSlot - 1;
            SleepUtil.sleep(100);

            // Desummon dillo
            macro.state = Macro.State.DESUMMON_DILLO;
            KeyBindUtil.rightClick();
            SleepUtil.sleep(100);
            if(Armageddon.INSTANCE.getPlayer().isRiding()) {
                Logger.error("Playing is still riding the armadillo!");
                Macro.stop();
                break;
            }

            // Switch to drill again
            macro.state = Macro.State.SWITCH_TO_DRILL_AGAIN;
            Armageddon.INSTANCE.getPlayer().inventory.currentItem = ArmageddonConfig.drillSlot - 1;
            SleepUtil.sleep(100);

            // Switch to AOTV
            macro.state = Macro.State.SWITCH_TO_AOTV;
            Armageddon.INSTANCE.getPlayer().inventory.currentItem = ArmageddonConfig.aotvSlot - 1;
            SleepUtil.sleep(100);

            // Sneak
            macro.state = Macro.State.SNEAK;
            KeyBindUtil.setKey(Armageddon.INSTANCE.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), true);
            SleepUtil.sleep(100);

            // Look at block
            macro.state = Macro.State.LOOK_AT_BLOCK;
            if (RouteManager.getNext() == null) {
                Logger.error("Something went really wrong...");
                Macro.stop();
                break;
            }
            RotationUtil.Rotation rotation = AngleUtil.getRoationToLookAt(RouteManager.getNext());
            RotationUtil.ease(rotation, ArmageddonConfig.lookAtBlockTime);
            SleepUtil.sleep(ArmageddonConfig.lookAtBlockTime);

            // Teleport
            macro.state = Macro.State.TELEPORT;
            KeyBindUtil.rightClick();
            SleepUtil.sleep(100);

            // Unsneak
            macro.state = Macro.State.UNSNEAK;
            KeyBindUtil.setKey(Armageddon.INSTANCE.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), false);
            SleepUtil.sleep(100);
        }
    }
}
