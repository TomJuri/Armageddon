package de.tomjuri.armageddon.util

import net.minecraft.util.MathHelper
import kotlin.math.pow

// Inspired by skyskipped
object RotationUtil {

    private var startRotation = Rotation(0f, 0f)
    private var endRotation = Rotation(0f, 0f)
    private var startTime = 0L
    private var endTime = 0L
    var done = true;

    fun ease(rotation: Rotation, durationMillis: Long) {
        if (!done) return
        done = false
        startRotation = Rotation(player.rotationYaw, player.rotationPitch)
        val neededChange = getNeededChange(startRotation, rotation)
        endRotation = Rotation(startRotation.yaw + neededChange.yaw, startRotation.pitch + neededChange.pitch)
        startTime = System.currentTimeMillis()
        endTime = startTime + durationMillis
    }

    fun ease360(rotation: Rotation, durationMillis: Long) {
        if (!done) return
        done = false
        val currentRotation = Rotation(player.rotationYaw, player.rotationPitch)
        val endRotationYaw = currentRotation.yaw + 360.0f
        startRotation = currentRotation
        endRotation = Rotation(endRotationYaw, rotation.pitch)
        startTime = System.currentTimeMillis()
        endTime = startTime + durationMillis
    }


    fun onRenderWorldLast() {
        if (done) return
        if (System.currentTimeMillis() <= endTime) {
            mc.thePlayer.rotationYaw = interpolate(startRotation.yaw, endRotation.yaw)
            mc.thePlayer.rotationPitch = interpolate(startRotation.pitch, endRotation.pitch)
            return
        }
        mc.thePlayer.rotationYaw = endRotation.yaw
        mc.thePlayer.rotationPitch = endRotation.pitch
        done = true
    }

    private fun interpolate(start: Float, end: Float): Float {
        val spentMillis = (System.currentTimeMillis() - startTime).toFloat()
        val relativeProgress = spentMillis / (endTime - startTime).toFloat()
        return (end - start) * easeOutCubic(relativeProgress.toDouble()) + start
    }

    private fun easeOutCubic(number: Double): Float {
        return (1.0 - (1.0 - number).pow(3.0)).toFloat()
    }

    private fun getNeededChange(startRot: Rotation, endRot: Rotation): Rotation {
        var yawChange = MathHelper.wrapAngleTo180_float(endRot.yaw) - MathHelper.wrapAngleTo180_float(startRot.yaw)
        if (yawChange <= -180.0f) yawChange += 360.0f
        else if (yawChange > 180.0f) yawChange += -360.0f
        return Rotation(yawChange, endRot.pitch - startRot.pitch)
    }

    data class Rotation(var yaw: Float, var pitch: Float)
}