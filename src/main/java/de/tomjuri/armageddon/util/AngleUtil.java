package de.tomjuri.armageddon.util;

import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class AngleUtil {
    public static RotationUtil.Rotation getRotationForBlock(BlockPos targetPos) {
        double diffX = to.xCoord - from.xCoord;
        double diffY = to.yCoord - from.yCoord;
        double diffZ = to.zCoord - from.zCoord;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float pitch = (float) -Math.atan2(dist, diffY);
        float yaw = (float) Math.atan2(diffZ, diffX);
        pitch = (float) wrapAngleTo180((pitch * 180F / Math.PI + 90) * -1);
        yaw = (float) wrapAngleTo180((yaw * 180 / Math.PI) - 90);

        return new Rotation(pitch, yaw);
    }
}
