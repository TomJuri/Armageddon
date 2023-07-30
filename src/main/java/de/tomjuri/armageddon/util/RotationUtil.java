package de.tomjuri.armageddon.util;

import de.tomjuri.armageddon.Armageddon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.MathHelper;

public class RotationUtil {
    private static Rotation startRotation = new Rotation(0f, 0f);
    private static Rotation endRotation = new Rotation(0f, 0f);
    private static long startTime = 0L;
    private static long endTime = 0L;
    private static boolean done = true;

    public static void ease(Rotation rotation, long durationMillis) {
        if (!done) return;
        done = false;
        startRotation = new Rotation(Armageddon.INSTANCE.getPlayer().rotationYaw, Armageddon.INSTANCE.getPlayer().rotationPitch);
        Rotation neededChange = getNeededChange(startRotation, rotation);
        endRotation = new Rotation(startRotation.getYaw() + neededChange.getYaw(), startRotation.getPitch() + neededChange.getPitch());
        startTime = System.currentTimeMillis();
        endTime = startTime + durationMillis;
    }

    public static void easeCertain(Rotation rotation, long durationMillis) {
        if (!done) return;
        done = false;
        Rotation currentRotation = new Rotation(Armageddon.INSTANCE.getPlayer().rotationYaw, Armageddon.INSTANCE.getPlayer().rotationPitch);
        float endRotationYaw = currentRotation.getYaw() + rotation.getYaw();
        startRotation = currentRotation;
        endRotation = new Rotation(endRotationYaw, rotation.getPitch());
        startTime = System.currentTimeMillis();
        endTime = startTime + durationMillis;
    }

    public static void onRenderWorldLast() {
        if (done) return;
        if (System.currentTimeMillis() <= endTime) {
            Armageddon.INSTANCE.getPlayer().rotationYaw = interpolate(startRotation.getYaw(), endRotation.getYaw());
            Armageddon.INSTANCE.getPlayer().rotationPitch = interpolate(startRotation.getPitch(), endRotation.getPitch());
            return;
        }
        Armageddon.INSTANCE.getPlayer().rotationYaw = endRotation.getYaw();
        Armageddon.INSTANCE.getPlayer().rotationPitch = endRotation.getPitch();
        done = true;
    }

    private static float interpolate(float start, float end) {
        float spentMillis = (float) (System.currentTimeMillis() - startTime);
        float relativeProgress = spentMillis / (endTime - startTime);
        return (end - start) * easeOutCubic(relativeProgress) + start;
    }

    private static float easeOutCubic(float number) {
        return (float) (1.0 - Math.pow(1.0 - number, 3.0));
    }

    private static Rotation getNeededChange(Rotation startRot, Rotation endRot) {
        float yawChange = MathHelper.wrapAngleTo180_float(endRot.getYaw()) - MathHelper.wrapAngleTo180_float(startRot.getYaw());
        if (yawChange <= -180.0f) yawChange += 360.0f;
        else if (yawChange > 180.0f) yawChange += -360.0f;
        return new Rotation(yawChange, endRot.getPitch() - startRot.getPitch());
    }

    @Getter
    @AllArgsConstructor
    public static class Rotation {
        private float yaw;
        private float pitch;
    }
}
