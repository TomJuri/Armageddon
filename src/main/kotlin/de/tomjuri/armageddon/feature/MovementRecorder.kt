package de.tomjuri.armageddon.feature

import de.tomjuri.armageddon.util.KeyBindUtil
import de.tomjuri.armageddon.util.mc
import de.tomjuri.armageddon.util.player
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

class MovementRecorder {

    private val movements = mutableListOf<Movement>()
    private var playing = false
    private var playingIndex = 0

    @SubscribeEvent
    fun onTickPlayer(event: ClientTickEvent) {
        if(!playing) return
        val movement = movements[playingIndex]
        KeyBindUtil.setKey(mc.gameSettings.keyBindForward.keyCode, movement.forward)
        KeyBindUtil.setKey(mc.gameSettings.keyBindLeft.keyCode, movement.left)
        KeyBindUtil.setKey(mc.gameSettings.keyBindBack.keyCode, movement.backwards)
        KeyBindUtil.setKey(mc.gameSettings.keyBindRight.keyCode, movement.right)
        KeyBindUtil.setKey(mc.gameSettings.keyBindSneak.keyCode, movement.sneak)
        KeyBindUtil.setKey(mc.gameSettings.keyBindJump.keyCode, movement.jump)
        KeyBindUtil.setKey(mc.gameSettings.keyBindAttack.keyCode, movement.attack)
        player.rotationYaw = movement.yaw
        player.rotationPitch = movement.pitch
        playingIndex++
        if(playingIndex == movements.size) playing = false
    }

    fun play(movement: String) {
        movements.clear()
        playingIndex = 0
        playing = true
        movement.lines().forEach {
            val split = it.split(";")
            movements.add(Movement(
                forward = split[1].toBoolean(),
                left = split[2].toBoolean(),
                backwards = split[3].toBoolean(),
                right = split[4].toBoolean(),
                sneak = split[5].toBoolean(),
                jump = split[6].toBoolean(),
                attack = split[7].toBoolean(),
                yaw = split[8].toFloat(),
                pitch = split[9].toFloat()
            ))
        }
    }

    data class Movement(
        val forward: Boolean,
        val left: Boolean,
        val backwards: Boolean,
        val right: Boolean,
        val sneak: Boolean,
        val jump: Boolean,
        val attack: Boolean,
        val yaw: Float,
        val pitch: Float
    )
}