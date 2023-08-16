package de.tomjuri.armageddon.macro

import de.tomjuri.armageddon.Armageddon
import de.tomjuri.armageddon.util.AngleUtil
import de.tomjuri.armageddon.util.Logger
import de.tomjuri.macroframework.util.*
import net.minecraft.entity.monster.EntityZombie
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

class Macro {
    private var enabled = false
    private var state = State.SWITCH_TO_ROD
    private var timer = Timer(0)
    private var condition = { true }
    private var conditionTimeout = Timer(0)
    private var i = -999

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!enabled) return
        if (!timer.isOver()) return
        if (!condition() && conditionTimeout.isOver()) {
            Logger.info("Condition timed out! State: $state Build: ${Armageddon.COMMIT}/${Armageddon.BRANCH}")
            enabled = false
            return
        }
        when (state) {
            State.SWITCH_TO_ROD -> {
                player.inventory.currentItem = Armageddon.instance.config.rodSlot - 1
                timer = Timer(100)
            }

            State.SUMMON_DILLO -> {
                KeyBindUtil.rightClick()
                timer = Timer(100)
            }

            State.SWITCH_TO_DRILL -> {
                player.inventory.currentItem = Armageddon.instance.config.drillSlot - 1
                timer = Timer(100)
            }

            State.NORMALIZE_PITCH -> {
                RotationUtil.ease(RotationUtil.Rotation(player.rotationYaw, -3f), 100)
                condition = {
                    world.loadedEntityList.stream().filter { entity ->
                        entity is EntityZombie && entity.isInvisible && player.getDistanceSqToEntity(entity) < 10
                    }.findAny().orElse(null) != null
                }
                conditionTimeout = Timer(2500)
            }

            State.MOUNT_DILLO -> {
                KeyBindUtil.rightClick()
                condition = { player.ridingEntity != null }
                conditionTimeout = Timer(2500)
            }

            State.MINE -> {
                KeyBindUtil.jump()
                RotationUtil.easeDirection(
                    RotationUtil.Rotation(Armageddon.instance.config.swipeRange, player.rotationPitch),
                    Armageddon.instance.config.swipeTime.toLong(),
                    RotationUtil.Direction.LEFT
                )
                timer = Timer(Armageddon.instance.config.swipeTime.toLong())
            }

            State.SWITCH_TO_ROD_AGAIN -> {
                player.inventory.currentItem = Armageddon.instance.config.rodSlot - 1
                timer = Timer(100)
            }

            State.DESUMMON_DILLO -> {
                KeyBindUtil.rightClick()
                timer = Timer(100)
            }

            State.SWITCH_TO_AOTV -> {
                player.inventory.currentItem = Armageddon.instance.config.aotvSlot - 1
                condition = { player.ridingEntity == null }
                conditionTimeout = Timer(2500)
            }

            State.SNEAK -> {
                KeyBindUtil.setPressed(gameSettings.keyBindSneak, true)
                timer = Timer(100)
                condition = { player.onGround }
                conditionTimeout = Timer(2500)
            }

            State.LOOK_AT_BLOCK -> {
                val next = Armageddon.instance.routeManager.getNext()
                if(next != null)
                    RotationUtil.ease(AngleUtil.getRotationForBlock(next), Armageddon.instance.config.lookAtBlockTime.toLong())
                timer = Timer(Armageddon.instance.config.lookAtBlockTime.toLong())
            }

            State.TELEPORT -> {
                i = Armageddon.instance.routeManager.getStandingOn() + 1
                if(!Armageddon.instance.failsafe.nextBlockMissing())
                    KeyBindUtil.rightClick()
            }

            State.UNSNEAK -> {
                KeyBindUtil.setPressed(gameSettings.keyBindSneak, false)
                timer = Timer(100)
                condition = { Armageddon.instance.routeManager.getStandingOn() == i }
                conditionTimeout = Timer(4000)
            }
        }
        state = State.entries[(state.ordinal + 1) % State.entries.size]
    }

    private fun start() {
        if (enabled) return
        Logger.info("Starting macro.")
        enabled = true
    }

    fun stop() {
        if (!enabled) return
        Logger.info("Stopping macro.")
        enabled = false
    }

    fun toggle() {
        if (enabled) {
            stop()
        } else {
            start()
        }
    }

    fun isEnabled() = enabled

    enum class State {
        SWITCH_TO_ROD,
        SUMMON_DILLO,
        SWITCH_TO_DRILL,
        NORMALIZE_PITCH,
        MOUNT_DILLO,
        MINE,
        SWITCH_TO_ROD_AGAIN,
        DESUMMON_DILLO,
        SWITCH_TO_AOTV,
        SNEAK,
        LOOK_AT_BLOCK,
        TELEPORT,
        UNSNEAK
    }
}