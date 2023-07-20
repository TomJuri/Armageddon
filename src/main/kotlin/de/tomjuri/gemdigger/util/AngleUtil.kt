package de.tomjuri.gemdigger.util

import net.minecraft.util.BlockPos
import kotlin.math.atan2

object AngleUtil {
    fun getLookAtAngles(targetPos: BlockPos, playerX: Double, playerY: Double, playerZ: Double): Pair<Float, Float> {
        val deltaX = targetPos.x + 0.5 - playerX
        val deltaY = targetPos.y + 0.5 - playerY
        val deltaZ = targetPos.z + 0.5 - playerZ
        val distanceXZ = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ)
        val yaw = Math.toDegrees(atan2(deltaZ, deltaX)).toFloat() - 90.0f
        val pitch = -Math.toDegrees(atan2(deltaY, distanceXZ)).toFloat()
        return Pair(yaw, pitch)
    }
}