package de.tomjuri.gemdigger.macro

import de.tomjuri.gemdigger.GemDigger
import de.tomjuri.gemdigger.util.*
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import java.awt.Color

class Macro {

    private var running = false
    private var state = State.SWITCH_TO_ROD
    private var nextState = State.SWITCH_TO_ROD
    private var current = -999
    private val timer = Timer()

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (!running) return

        val standinOn = GemDigger.routeManager.findStandingOn()
        Logger.info(standinOn)
        if (current < 0 && standinOn == -1) {
            Logger.error("Not standing on a block in the route! " +
                         "You need to AOTV in the middle of the block.")
            stop()
            return
        } else {
            current = standinOn
        }

        Logger.info(state)

        when (state) {
            State.SWITCH_TO_ROD -> {
                player.inventory.currentItem = GemDigger.config.rodSlot - 1
                timer.startTimer(100)
                nextState = State.SUMMON_DILLO
                state = State.DELAY
            }

            State.SUMMON_DILLO -> {
                KeyBindUtil.rightClick()
                timer.startTimer(100)
                nextState = State.SWITCH_TO_DRILL
                state = State.DELAY
            }

            State.SWITCH_TO_DRILL -> {
                player.inventory.currentItem = GemDigger.config.drillSlot - 1
                timer.startTimer(100)
                nextState = State.NORMALIZE_PITCH
                state = State.DELAY
            }

            State.NORMALIZE_PITCH -> {
                RotationUtil.ease(RotationUtil.Rotation(player.rotationYaw, -3f), 100)
                timer.startTimer(100)
                nextState = State.MOUNT_DILLO
                state = State.DELAY
            }

            State.MOUNT_DILLO -> {
                KeyBindUtil.rightClick()
                timer.startTimer(100)
                nextState = State.MINE
                state = State.DELAY
            }

            State.MINE -> {
                RotationUtil.ease360(RotationUtil.Rotation(-1f, player.rotationPitch), 900)
                timer.startTimer(700)
                nextState = State.MINE_JUMP
                state = State.DELAY
            }

            State.MINE_JUMP -> {
                player.jump()
                timer.startTimer(200)
                nextState = State.SWITCH_TO_ROD_2
                state = State.DELAY
            }

            State.SWITCH_TO_ROD_2 -> {
                player.inventory.currentItem = GemDigger.config.rodSlot - 1
                timer.startTimer(100)
                nextState = State.DESUMMON_DILLO
                state = State.DELAY
            }

            State.DESUMMON_DILLO -> {
                KeyBindUtil.rightClick()
                timer.startTimer(100)
                nextState = State.SWITCH_TO_DRILL_2
                state = State.DELAY
            }

            State.SWITCH_TO_DRILL_2 -> {
                player.inventory.currentItem = GemDigger.config.drillSlot - 1
                // sleep until landed is hardcoded in the delay state
                nextState = State.SWITCH_TO_AOTV
                state = State.DELAY
            }

            State.SWITCH_TO_AOTV -> {
                player.inventory.currentItem = GemDigger.config.aotvSlot - 1
                timer.startTimer(100)
                nextState = State.SNEAK
                state = State.DELAY
            }

            State.SNEAK -> {
                KeyBindUtil.setKey(mc.gameSettings.keyBindSneak.keyCode, true)
                timer.startTimer(100)
                nextState = State.LOOK_AT_BLOCK
                state = State.DELAY
            }

            State.LOOK_AT_BLOCK -> {
                val block = GemDigger.routeManager.route[current + 1]
                val rotation = AngleUtil.getRotationToLookAt(block)
                RotationUtil.ease(rotation, 250)
                timer.startTimer(250)
                nextState = State.TELEPORT
                state = State.DELAY
            }

            State.TELEPORT -> {
                KeyBindUtil.rightClick()
                timer.startTimer(2000)
                nextState = State.UNSNEAK
                state = State.DELAY
            }

            State.UNSNEAK -> {
                KeyBindUtil.setKey(mc.gameSettings.keyBindSneak.keyCode, false)
                timer.startTimer(100)
                nextState = State.SWITCH_TO_ROD
                state = State.DELAY
                if(player.posX != GemDigger.routeManager.route[current + 1].x + 0.5 ||
                   player.posZ != GemDigger.routeManager.route[current + 1].z + 0.5) {
                    Logger.error("Teleport failed, did not arrive at location!")
                    stop()
                    return
                }
                current++
            }

            State.DELAY -> {
                if (nextState == State.SWITCH_TO_AOTV && !player.onGround)
                    return
                if (timer.isDone())
                    state = nextState
            }
        }
    }

    @SubscribeEvent
    fun onRenderWorldLast(event: RenderWorldLastEvent) {
        RotationUtil.onRenderWorldLast()
        if (!running || !GemDigger.config.showWaypoints) return
        val next = GemDigger.routeManager.route[current + 1]
        RenderUtil.drawLine(event, Vec3(player.posX, player.posY + player.getEyeHeight(), player.posZ), Vec3(next.x + 0.5, next.y - 0.5, next.z + 0.5), 0.5f, Color.green)
    }

    fun toggle() {
        if (running) stop()
        else start()
    }

    fun start() {
        Logger.info("Starting macro.")
        running = true
        current = -999
        state = State.SWITCH_TO_ROD
        nextState = State.SWITCH_TO_ROD
    }

    fun stop() {
        Logger.info("Stopping macro.")
        running = false
    }

    fun isRunning() = running

    enum class State {
        SWITCH_TO_ROD,
        SUMMON_DILLO,
        SWITCH_TO_DRILL,
        NORMALIZE_PITCH,
        MOUNT_DILLO,
        MINE,
        MINE_JUMP,
        SWITCH_TO_ROD_2,
        DESUMMON_DILLO,
        SWITCH_TO_DRILL_2,
        SWITCH_TO_AOTV,
        SNEAK,
        LOOK_AT_BLOCK,
        TELEPORT,
        UNSNEAK,
        DELAY
    }
}