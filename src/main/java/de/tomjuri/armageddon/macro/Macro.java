package de.tomjuri.armageddon.macro;

import de.tomjuri.armageddon.util.Logger;
import lombok.Getter;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Macro {

    @Getter
    private static boolean enabled = false;
    public State state = State.SWITCH_TO_ROD;

    public static void start() {
        Logger.info("Starting macro.");
        enabled = true;
    }

    public static void stop() {
        Logger.info("Stopping macro.");
        enabled = false;
    }

    public static void toggle() {
        if(enabled) {
            stop();
        } else {
            start();
        }
    }

    public enum State {
        SWITCH_TO_ROD,
        SUMMON_DILLO,
        SWITCH_TO_DRILL,
        NORMALIZE_PITCH,
        MOUNT_DILLO,
        MINE,
        SWITCH_TO_ROD_AGAIN,
        DESUMMON_DILLO,
        SWITCH_TO_DRILL_AGAIN,
        SWITCH_TO_AOTV,
        SNEAK,
        LOOK_AT_BLOCK,
        TELEPORT,
        UNSNEAK
    }
}