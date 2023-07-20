package de.tomjuri.gemdigger.util

import net.minecraft.client.Minecraft
import kotlin.math.abs

object RotationUtil {

    var done = true;

    fun ease(targetYaw: Float, targetPitch: Float, durationMillis: Long) {
        if(!done) return
        done = false
        Thread {
            val player = Minecraft.getMinecraft().thePlayer
            val startTime = System.currentTimeMillis()
            val startYaw = player.rotationYaw
            val startPitch = player.rotationPitch
            while (System.currentTimeMillis() - startTime < durationMillis) {
                val currentTime = System.currentTimeMillis() - startTime
                val progress = currentTime.toFloat() / durationMillis
                val currentYaw: Float = interpolateYaw(startYaw, targetYaw, progress)
                val currentPitch: Float = interpolate(startPitch, targetPitch, progress)
                player.rotationYaw = currentYaw
                player.rotationPitch = currentPitch
                Thread.sleep(16)
            }
            player.rotationYaw = targetYaw
            player.rotationPitch = targetPitch
            done = true
        }.start()
    }

    private fun interpolate(start: Float, end: Float, progress: Float): Float {
        return start + progress * (end - start)
    }

    private fun interpolateYaw(startYaw: Float, targetYaw: Float, progress: Float): Float {
        var yaw = normalizeAngle(startYaw)
        var target = normalizeAngle(targetYaw)
        val diff = target - yaw
        if (abs(diff) > 180) {
            if (diff > 0) {
                yaw += 360f
            } else {
                target += 360f
            }
        }
        return normalizeAngle(interpolate(yaw, target, progress))
    }

    private fun normalizeAngle(ang: Float): Float {
        var angle = ang
        angle %= 360f
        if (angle >= 180) {
            angle -= 360f
        }
        if (angle < -180) {
            angle += 360f
        }
        return angle
    }
}