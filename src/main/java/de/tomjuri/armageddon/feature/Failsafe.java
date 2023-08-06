package de.tomjuri.armageddon.feature;

import de.tomjuri.armageddon.config.ArmageddonConfig;
import de.tomjuri.armageddon.event.PacketEvent;
import de.tomjuri.armageddon.macro.Macro;
import de.tomjuri.armageddon.util.*;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.Arrays;
import java.util.List;

public class Failsafe {
    private final String failsafeMovement = "";
    private final String failsafeMovementNoMovement = "";
    private final List<String> reactionMessages = Arrays.asList("lol wtf", "wtf?", "huh?", "ok?", "admin?", "show urself demon", "check??", "ok wtf??", "tf??");
    private String movement = "";
    private final Timer timer = new Timer();
    private int reactionIndex = 0;

    private void emergency(String message, String movement) {
        Logger.error(message);
        new Thread(() -> {
            for (int i = 0; i < 7; i++) {
                SoundUtil.playSound("pipe.wav");
                SleepUtil.sleep(2600);
            }
        }).start();
        timer.start(2000);
        this.movement = movement;
        reactionIndex = 0;
    }

    @SubscribeEvent
    public void onReceivePacket(PacketEvent event) {
        if (!Macro.isEnabled())
            return;
        if (!(event.getPacket() instanceof S08PacketPlayerPosLook))
            return;
        S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
        if (Macro.getState() == Macro.State.MOUNT_DILLO || Macro.getState() == Macro.State.TELEPORT)
            return;
        float deltaYaw = Math.max(Math.abs(packet.getYaw()), Math.abs(Ref.player().rotationYaw)) - Math.min(Math.abs(packet.getYaw()), Math.abs(Ref.player().rotationYaw));
        float deltaPitch = Math.max(Math.abs(packet.getPitch()), Math.abs(Ref.player().rotationPitch)) - Math.min(Math.abs(packet.getPitch()), Math.abs(Ref.player().rotationPitch));
        if (deltaYaw > ArmageddonConfig.rotationCheckThreshold || deltaPitch > ArmageddonConfig.rotationCheckThreshold)
            emergency("You probably have been rotation checked!", failsafeMovementNoMovement);
    }

    @SubscribeEvent
    public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        if (!Macro.isEnabled())
            return;
        emergency("You probably have been kicked from the server!", "");
    }

    @SubscribeEvent
    public void onUnloadWorld(WorldEvent.Unload event) {
        if (!Macro.isEnabled())
            return;
        emergency("You probably have been kicked from the server!", "");
    }

    @SubscribeEvent
    public void onTick0(TickEvent.ClientTickEvent event) {
        if (!Macro.isEnabled())
            return;
        BlockPos loc = Ref.player().getPosition();
        Block top = Ref.world().getBlockState(loc.add(0, 2, 0)).getBlock();
        Block bottom = Ref.world().getBlockState(loc.add(0, -1, 0)).getBlock();
        Block leftBottom = Ref.world().getBlockState(loc.add(-1, 0, 0)).getBlock();
        Block leftTop = Ref.world().getBlockState(loc.add(-1, 1, 0)).getBlock();
        Block rightBottom = Ref.world().getBlockState(loc.add(1, 0, 0)).getBlock();
        Block rightTop = Ref.world().getBlockState(loc.add(1, 1, 0)).getBlock();
        Block frontBottom = Ref.world().getBlockState(loc.add(0, 0, -1)).getBlock();
        Block frontTop = Ref.world().getBlockState(loc.add(0, 1, -1)).getBlock();
        Block backBottom = Ref.world().getBlockState(loc.add(0, 0, 1)).getBlock();
        Block backTop = Ref.world().getBlockState(loc.add(0, 1, 1)).getBlock();

        if (top == Blocks.stone
                && bottom == Blocks.stone
                && leftBottom == Blocks.stone
                && leftTop == Blocks.stone
                && rightBottom == Blocks.stone
                && rightTop == Blocks.stone
                && frontBottom == Blocks.stone
                && frontTop == Blocks.stone
                && backBottom == Blocks.stone
                && backTop == Blocks.stone) {
            emergency("You probably have been hardstone box checked!", failsafeMovementNoMovement);
        }
    }

    @SubscribeEvent
    public void onTick1(TickEvent.ClientTickEvent event) {
        if (!Macro.isEnabled())
            return;
        int radius = 20;
        int bedrock = 0;
        boolean oak = false;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (Ref.world().getBlockState(Ref.player().getPosition().add(x, y, z)) == Blocks.bedrock)
                        bedrock++;
                    if (Ref.world().getBlockState(Ref.player().getPosition().add(x, y, z)) == Blocks.log)
                        oak = true;
                }
            }
        }
        if (bedrock > 2 && oak)
            emergency("You probably have been bedrock box checked!", failsafeMovement);
    }

    @SubscribeEvent
    public void onTickReaction(TickEvent.ClientTickEvent event) {
        if (movement.isEmpty()) return;
        if (!timer.isDone()) return;
        String[] split = movement.split("\\|");
        String[] split2 = split[reactionIndex].split(";");
        boolean forward = split2[1].equals("1");
        boolean left = split2[2].equals("1");
        boolean backward = split2[3].equals("1");
        boolean right = split2[4].equals("1");
        boolean sneak = split2[5].equals("1");
        boolean jump = split2[6].equals("1");
        boolean attack = split2[7].equals("1");
        float yaw = Float.parseFloat(split2[8]);
        float pitch = Float.parseFloat(split2[9]);
        if (forward) {
            KeyBindUtil.setKey(Ref.mc().gameSettings.keyBindForward.getKeyCode(), true);
        }
        if (left) {
            KeyBindUtil.setKey(Ref.mc().gameSettings.keyBindLeft.getKeyCode(), true);
        }
        if (backward) {
            KeyBindUtil.setKey(Ref.mc().gameSettings.keyBindBack.getKeyCode(), true);
        }
        if (right) {
            KeyBindUtil.setKey(Ref.mc().gameSettings.keyBindRight.getKeyCode(), true);
        }
        if (sneak) {
            KeyBindUtil.setKey(Ref.mc().gameSettings.keyBindSneak.getKeyCode(), true);
        }
        if (jump) {
            KeyBindUtil.setKey(Ref.mc().gameSettings.keyBindJump.getKeyCode(), true);
        }
        if (attack) {
            KeyBindUtil.leftClick();
        }
        Ref.player().rotationYaw = yaw;
        Ref.player().rotationPitch = pitch;
        reactionIndex++;
        if (reactionIndex >= split.length) {
            reactionIndex = 0;
            movement = "";
        }
    }
}
