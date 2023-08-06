package de.tomjuri.armageddon.util;

import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class AngleUtil {
    public static RotationUtil.Rotation getRoationToLookAt(BlockPos targetPos) {
        double deltaX = targetPos.getX() + 0.5 - Ref.player().posX;
        double deltaY = targetPos.getY() + 0.5 - Ref.player().posY - 1.62 - 0.08 + 0.5;
        double deltaZ = targetPos.getZ() + 0.5 - Ref.player().posZ;
        double dist = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        float pitch = (float) -Math.atan2(dist, deltaY);
        float yaw = (float) Math.atan2(deltaZ, deltaX);
        pitch = (float) MathHelper.wrapAngleTo180_double((pitch * 180f / Math.PI + 90) * -1);
        yaw = (float) MathHelper.wrapAngleTo180_double((yaw * 180 / Math.PI - 90));
        return new RotationUtil.Rotation(yaw, pitch);
    }
}
