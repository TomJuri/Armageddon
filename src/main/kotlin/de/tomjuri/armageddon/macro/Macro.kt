package de.tomjuri.armageddon.macro

import de.tomjuri.armageddon.Armageddon
import de.tomjuri.armageddon.util.*
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

class Macro {

    private var running = false
    private var state = State.SWITCH_TO_ROD
    private var nextState = State.SWITCH_TO_ROD
    private var current = -999
    private val timer = Timer()

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (!running) return

        val standingOn = Armageddon.routeManager.findStandingOn()
        if (current < 0) {
            current = standingOn
        }
        if (current < 0 && standingOn == -1) {
            Logger.error("Not standing on a block in the route! You need to AOTV in the middle of the block.")
            stop()
            return
        }

        when (state) {
            State.SWITCH_TO_ROD -> {
                player.inventory.currentItem = Armageddon.config.rodSlot - 1
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
                player.inventory.currentItem = Armageddon.config.drillSlot - 1
                timer.startTimer(200)
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
                timer.startTimer(200)
                nextState = State.MINE
                state = State.DELAY
            }

            State.MINE -> {
                KeyBindUtil.setKey(mc.gameSettings.keyBindJump.keyCode, true)
                RotationUtil.easeCertain(
                        RotationUtil.Rotation(
                                Armageddon.config.swipeRange.toFloat(),
                                player.rotationPitch),
                        Armageddon.config.swipeTime.toLong())
                nextState = State.JUMP
                state = State.DELAY
            }

            State.JUMP -> {
                timer.startTimer(100)
                nextState = State.JUMP_2
                state = State.DELAY
            }

            State.JUMP_2 -> {
                KeyBindUtil.setKey(mc.gameSettings.keyBindJump.keyCode, false)
                timer.startTimer(310)
                nextState = State.SWITCH_TO_ROD_2
                state = State.DELAY
            }

            State.SWITCH_TO_ROD_2 -> {
                player.inventory.currentItem = Armageddon.config.rodSlot - 1
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
                player.inventory.currentItem = Armageddon.config.drillSlot - 1
                nextState = State.SWITCH_TO_AOTV
                state = State.DELAY
            }

            State.SWITCH_TO_AOTV -> {
                player.inventory.currentItem = Armageddon.config.aotvSlot - 1
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
                val block = Armageddon.routeManager.route[current + 1]
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
                current++
            }

            State.DELAY -> {
                if (nextState == State.SWITCH_TO_AOTV && !player.onGround)
                    return
                if (nextState == State.UNSNEAK && timer.isDone()) {
                    Logger.error("Teleport failed, did not arrive at location!")
                    stop()
                    return
                }
                if (nextState == State.UNSNEAK
                        && player.posX == Armageddon.routeManager.route[current + 1].x + 0.5
                        && player.posZ == Armageddon.routeManager.route[current + 1].z + 0.5) {
                    state = nextState
                    return
                }
                if (nextState == State.MINE && !player.isRiding) return
                if (timer.isDone())
                    state = nextState
            }
        }
    }

    @SubscribeEvent
    fun onRenderWorldLast(event: RenderWorldLastEvent) {
        RotationUtil.onRenderWorldLast()
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
        JUMP,
        JUMP_2,
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