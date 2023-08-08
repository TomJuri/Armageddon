package de.tomjuri.armageddon.macro;

import de.tomjuri.armageddon.Armageddon;
import de.tomjuri.armageddon.config.ArmageddonConfig;
import de.tomjuri.armageddon.util.*;
import lombok.Getter;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Macro {

    @Getter private boolean enabled = false;
    @Getter private State state = State.SWITCH_TO_ROD;
    private final Timer timer = new Timer();

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(!enabled) return;
        if(!timer.isDone()) return;
        switch (state) {
            case SWITCH_TO_ROD:
                Ref.player().inventory.currentItem = ArmageddonConfig.rodSlot - 1;
                timer.start(100);
                break;
            case SUMMON_DILLO:
                KeyBindUtil.rightClick();
                timer.start(100);
                break;
            case SWITCH_TO_DRILL:
                Ref.player().inventory.currentItem = ArmageddonConfig.drillSlot - 1;
                timer.start(100);
                break;
            case NORMALIZE_PITCH:
                RotationUtil.ease(new RotationUtil.Rotation(Ref.player().rotationYaw, -3f), 100);
                timer.start(100);
                break;
            case MOUNT_DILLO:
                KeyBindUtil.rightClick();
                timer.start(ArmageddonConfig.delayAfterMountingDillo);
                break;
            case MINE:
                KeyBindUtil.jump();
                RotationUtil.easeCertain(new RotationUtil.Rotation(ArmageddonConfig.swipeRange, Ref.player().rotationPitch), ArmageddonConfig.swipeTime);
                timer.start(ArmageddonConfig.swipeTime);
                break;
            case SWITCH_TO_ROD_AGAIN:
                Ref.player().inventory.currentItem = ArmageddonConfig.rodSlot - 1;
                timer.start(101);
                break;
            case DESUMMON_DILLO:
                KeyBindUtil.rightClick();
                timer.start(101);
                break;
            case SWITCH_TO_DRILL_AGAIN:
                Ref.player().inventory.currentItem = ArmageddonConfig.drillSlot - 1;
                timer.start(101);
                break;
            case SWITCH_TO_AOTV:
                Ref.player().inventory.currentItem = ArmageddonConfig.aotvSlot - 1;
                timer.start(100);
                break;
            case SNEAK:
                KeyBindUtil.setKey(Ref.mc().gameSettings.keyBindSneak.getKeyCode(), true);
                timer.start(100);
                break;
            case LOOK_AT_BLOCK:
                RotationUtil.ease(AngleUtil.getRotationForBlock(Armageddon.getInstance().getRouteManager().getNext()), ArmageddonConfig.lookAtBlockTime);
                timer.start(ArmageddonConfig.lookAtBlockTime);
                break;
            case TELEPORT:
                KeyBindUtil.rightClick();
                timer.start(ArmageddonConfig.delayAfterTeleporting);
                break;
            case UNSNEAK:
                KeyBindUtil.setKey(Ref.mc().gameSettings.keyBindSneak.getKeyCode(), false);
                timer.start(100);
                break;
        }
        state = State.values()[(state.ordinal() + 1) % State.values().length];
    }

    @SubscribeEvent public void onWorldRenderLast(RenderWorldLastEvent event) { RotationUtil.onRenderWorldLast(); }

    public void start() {
        if(enabled) return;
        if(Armageddon.getInstance().getRouteManager().getStandingOn() == -1) {
            Logger.error("You are not standing on a block on the route. Make sure to AOTV on it, so you are centered.");
            return;
        }
        Logger.info("Starting macro.");
        enabled = true;
    }

    public void stop() {
        if(!enabled) return;
        state = State.SWITCH_TO_ROD;
        Logger.info("Stopping macro.");
        enabled = false;
    }

    public void toggle() {
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