package de.tomjuri.gemdigger

import de.tomjuri.gemdigger.util.*
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

class Macro {

    var running = false
    var state = State.SWITCH_TO_ROD
    var nextState = State.SWITCH_TO_ROD
    var current = 0

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (!running) return
        when (state) {
            State.DELAY -> {
                if(TimerUtil.isDone() && RotationUtil.done) {
                    state = nextState
                }
            }

            State.SWITCH_TO_ROD -> {
                Logger.info("Current: $current")
                GemDigger.routeManager.current = current
                player.inventory.currentItem = GemDigger.config.rodSlot
                TimerUtil.startTimer(100)
                nextState = State.SUMMON_DILLO
                state = State.DELAY
            }

            State.SUMMON_DILLO -> {
                KeyBindUtil.rightClick()
                TimerUtil.startTimer(100)
                nextState = State.SWITCH_TO_DRILL
                state = State.DELAY
            }

            State.SWITCH_TO_DRILL -> {
                player.inventory.currentItem = GemDigger.config.drillSlot
                TimerUtil.startTimer(100)
                nextState = State.MOUNT_DILLO
                state = State.DELAY
            }

            State.MOUNT_DILLO -> {
                KeyBindUtil.rightClick()
                TimerUtil.startTimer(100)
                nextState = State.MINE
                state = State.DELAY
            }

            State.MINE -> {
                RotationUtil.ease(player.rotationYaw, -55.5f, 500, true)
                nextState = State.MINE_2
                state = State.DELAY
            }

            State.MINE_2 -> {
                RotationUtil.ease(player.rotationYaw + 360, -45f, 900, false)
                player.jump()
                nextState = State.SWITCH_TO_ROD_2
                state = State.DELAY
            }

            State.SWITCH_TO_ROD_2 -> {
                player.inventory.currentItem = GemDigger.config.rodSlot
                TimerUtil.startTimer(100)
                nextState = State.DESUMMON_DILLO
                state = State.DELAY
            }

            State.DESUMMON_DILLO -> {
                KeyBindUtil.rightClick()
                TimerUtil.startTimer(100)
                nextState = State.SWITCH_TO_AOTV
                state = State.DELAY
            }

            State.SWITCH_TO_AOTV -> {
                player.inventory.currentItem = GemDigger.config.aotvSlot
                TimerUtil.startTimer(100)
                nextState = State.LOOK_AT_BLOCK
                state = State.DELAY
            }

            State.LOOK_AT_BLOCK -> {
                val rotation = AngleUtil.getLookAtAngles(GemDigger.routeManager.route[current + 1])
                RotationUtil.ease(rotation.first, rotation.second, 900, true)
                TimerUtil.startTimer(900)
                nextState = State.TELEPORT_SNEAK
                state = State.DELAY
            }

            State.TELEPORT_SNEAK -> {
                KeyBindUtil.setKey(mc.gameSettings.keyBindSneak.keyCode, true)
                TimerUtil.startTimer(500)
                nextState = State.TELEPORT
                state = State.DELAY
            }

            State.TELEPORT -> {
                KeyBindUtil.rightClick()
                TimerUtil.startTimer(500)
                nextState = State.TELEPORT_UNSNEAK
                state = State.DELAY
            }

            State.TELEPORT_UNSNEAK -> {
                KeyBindUtil.setKey(mc.gameSettings.keyBindSneak.keyCode, false)
                TimerUtil.startTimer(500)
                nextState = State.SWITCH_TO_ROD
                state = State.DELAY
                current++
            }
        }
    }

    fun toggle() {
        running = !running
        if(running) {
            Logger.info("Starting macro.")
            current = 0
            state = State.SWITCH_TO_ROD
        } else {
            Logger.info("Stopping macro.")
        }
    }

    enum class State {
        DELAY,
        SWITCH_TO_ROD,
        SUMMON_DILLO,
        SWITCH_TO_DRILL,
        MOUNT_DILLO,
        MINE,
        MINE_2,
        SWITCH_TO_ROD_2,
        DESUMMON_DILLO,
        SWITCH_TO_AOTV,
        LOOK_AT_BLOCK,
        TELEPORT_SNEAK,
        TELEPORT,
        TELEPORT_UNSNEAK
    }
}