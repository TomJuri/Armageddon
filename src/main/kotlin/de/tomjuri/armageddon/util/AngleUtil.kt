package de.tomjuri.armageddon.util

import de.tomjuri.macroframework.util.RotationUtil
import de.tomjuri.macroframework.util.player
import net.minecraft.util.BlockPos
import net.minecraft.util.MathHelper
import kotlin.math.atan2
import kotlin.math.sqrt

object AngleUtil {
    fun getRotationForBlock(targetPos: BlockPos): RotationUtil.Rotation {
        val deltaX: Double = targetPos.x + 0.5 - player.posX
        val deltaY: Double = targetPos.y + 0.5 - player.posY - 1.62 - 0.08 + 0.5
        val deltaZ: Double = targetPos.z + 0.5 - player.posZ
        val dist = sqrt(deltaX * deltaX + deltaZ * deltaZ)
        var pitch = (-atan2(dist, deltaY)).toFloat()
        var yaw = atan2(deltaZ, deltaX).toFloat()
        pitch = MathHelper.wrapAngleTo180_double((pitch * 180f / Math.PI + 90) * -1).toFloat()
        yaw = MathHelper.wrapAngleTo180_double(yaw * 180 / Math.PI - 90).toFloat()
        return RotationUtil.Rotation(yaw, pitch)
    }
}