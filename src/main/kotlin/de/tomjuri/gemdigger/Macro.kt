package de.tomjuri.gemdigger

import cc.polyfrost.oneconfig.config.annotations.KeyBind
import de.tomjuri.gemdigger.util.*
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

class Macro {

    var running = false
    var state = State.SWITCH_TO_ROD
    var nextState = State.SWITCH_TO_ROD
    val route = RouteParser.parseRoute(ExampleMod.config.route)

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (!running) return
        when (state) {
            State.DELAY -> {
                if(TimerUtil.isDone()) {
                    state = nextState
                }
            }

            State.SWITCH_TO_ROD -> {
                player.inventory.currentItem = ExampleMod.config.rodSlot.toInt()
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
                player.inventory.currentItem = ExampleMod.config.drillSlot.toInt()
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
                player.jump()
                RotationUtil.ease(player.rotationYaw + 180, -45f, 900)
                TimerUtil.startTimer(900)
                nextState = State.SWITCH_TO_ROD_2
                state = State.DELAY
            }

            State.SWITCH_TO_ROD_2 -> {
                player.inventory.currentItem = ExampleMod.config.rodSlot.toInt()
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
                player.inventory.currentItem = ExampleMod.config.aotvSlot.toInt()
                TimerUtil.startTimer(100)
                nextState = State.LOOK_AT_BLOCK
                state = State.DELAY
            }

            State.LOOK_AT_BLOCK -> {
                val r = AngleUtil.getLookAtAngles()
                RotationUtil.ease(player.rotationYaw + 180, -45f, 900)
                TimerUtil.startTimer(900)
                nextState = State.TELEPORT
                state = State.DELAY
            }
        }
    }

    enum class State {
        DELAY,
        SWITCH_TO_ROD,
        SUMMON_DILLO,
        SWITCH_TO_DRILL,
        MOUNT_DILLO,
        MINE,
        SWITCH_TO_ROD_2,
        DESUMMON_DILLO,
        SWITCH_TO_AOTV,
        LOOK_AT_BLOCK,
        TELEPORT
    }
}