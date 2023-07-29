package de.tomjuri.armageddon.feature

import de.tomjuri.armageddon.util.KeyBindUtil
import de.tomjuri.armageddon.util.mc
import de.tomjuri.armageddon.util.player
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

class MovementRecorder {



    private val movements = mutableListOf<Movement>()
    private var recording = false
    private var recordingIndex = 0
    private var playing = false
    private var playingIndex = 0

    @SubscribeEvent
    fun onTickRecord(event: ClientTickEvent) {
        println(recordingIndex)
        if(!recording) return
        movements.add(recordingIndex, Movement(
            forward = mc.gameSettings.keyBindForward.isKeyDown,
            left = mc.gameSettings.keyBindLeft.isKeyDown,
            backwards = mc.gameSettings.keyBindBack.isKeyDown,
            right = mc.gameSettings.keyBindRight.isKeyDown,
            sneak = mc.gameSettings.keyBindSneak.isKeyDown,
            jump = mc.gameSettings.keyBindJump.isKeyDown,
            attack = mc.gameSettings.keyBindAttack.isKeyDown,
            yaw = player.rotationYaw,
            pitch = player.rotationPitch
        ))
        recordingIndex++
    }

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

    fun startRecording() {
        movements.clear()
        recordingIndex = 0
        playing = false
        playingIndex = 0
        recording = true
    }

    fun stopRecording(name: String) {
        recording = false
        recordingIndex = 0
        playing = false
        playingIndex = 0
        val pw = PrintWriter(FileWriter("armageddon/$name.movement"))
        movements.forEach {
            pw.println("$recordingIndex;${it.forward};${it.left};${it.backwards};${it.right};${it.sneak};${it.jump};${it.attack};${it.yaw};${it.pitch}")
            recordingIndex++
        }
    }

    fun readRecording(name: String) {
        movements.clear()
        recording = false
        recordingIndex = 0
        playing = false
        playingIndex = 0
        File("armageddon/$name.movement").readLines().forEach {
            val split = it.split(";")
            val movement = Movement(
                forward = split[1].toBoolean(),
                left = split[2].toBoolean(),
                backwards = split[3].toBoolean(),
                right = split[4].toBoolean(),
                sneak = split[5].toBoolean(),
                jump = split[6].toBoolean(),
                attack = split[7].toBoolean(),
                yaw = split[8].toFloat(),
                pitch = split[9].toFloat()
            )
            movements.add(movement)
        }
    }

    fun play() {
        playingIndex = 0
        recording = false
        recordingIndex = 0
        playingIndex = 0
        playing = true
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