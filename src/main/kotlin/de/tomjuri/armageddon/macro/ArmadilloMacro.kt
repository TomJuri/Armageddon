package de.tomjuri.armageddon.macro

import de.tomjuri.armageddon.Armageddon
import de.tomjuri.armageddon.config.ArmageddonConfig
import de.tomjuri.armageddon.util.AngleUtil
import de.tomjuri.armageddon.util.Logger
import de.tomjuri.macroframework.macro.ConditionalState
import de.tomjuri.macroframework.macro.Macro
import de.tomjuri.macroframework.macro.TimedState
import de.tomjuri.macroframework.util.*
import net.minecraft.entity.monster.EntityZombie
import java.util.*

class ArmadilloMacro : Macro(listOf(SWITCH_TO_ROD, SUMMON_DILLO, SWITCH_TO_DRILL, NORMALIZE_PITCH, MOUNT_DILLO, MINE, SWITCH_TO_ROD_AGAIN, DESUMMON_DILLO, SWITCH_TO_AOTV, SNEAK, LOOK_AT_BLOCK, TELEPORT, UNSNEAK)) {

    companion object {
        private val SWITCH_TO_ROD = TimedState(
            { player.inventory.currentItem = ArmageddonConfig.rodSlot - 1 },
            100
        )

        private val SUMMON_DILLO = TimedState(
            { KeyBindUtil.rightClick() },
            100
        )

        private val SWITCH_TO_DRILL = TimedState(
            { player.inventory.currentItem = ArmageddonConfig.drillSlot - 1 },
            100
        )

        private val NORMALIZE_PITCH = ConditionalState(
            { RotationUtil.ease(RotationUtil.Rotation(player.rotationYaw, -3f), 100) },
            {
                world.loadedEntityList.stream().filter { entity ->
                    entity is EntityZombie && entity.isInvisible() && player.getDistanceSqToEntity(entity) < 10
                }.findAny().orElse(null) != null;
            },
            1000,
            { Logger.error("Armadillo did not spawn in time!") }
        )

        private val MOUNT_DILLO = ConditionalState(
            { KeyBindUtil.rightClick() },
            { player.ridingEntity != null },
            1000,
            { Logger.error("Armadillo did not mount in time!") }
        )

        private val MINE = TimedState(
            {
                KeyBindUtil.jump()
                RotationUtil.easeDirection(
                    RotationUtil.Rotation(ArmageddonConfig.swipeRange, player.rotationPitch),
                    ArmageddonConfig.swipeTime,
                    RotationUtil.Direction.entries[Random().nextInt(RotationUtil.Direction.entries.size)]
                )
            },
            ArmageddonConfig.swipeTime
        )

        private val SWITCH_TO_ROD_AGAIN = TimedState(
            { player.inventory.currentItem = ArmageddonConfig.rodSlot - 1 },
            100
        )

        private val DESUMMON_DILLO = ConditionalState(
            { KeyBindUtil.rightClick() },
            { player.ridingEntity == null },
            1000,
            { Logger.error("Armadillo did not desummon in time!") }
        )

        private val SWITCH_TO_AOTV = TimedState(
            { player.inventory.currentItem = ArmageddonConfig.aotvSlot - 1 },
            100
        )

        private val SNEAK = ConditionalState(
            { KeyBindUtil.setPressed(gameSettings.keyBindSneak, true) },
            { player.onGround },
            1000,
            { Logger.error("Play is not on ground!") }
        )

        private val LOOK_AT_BLOCK = TimedState(
            {
                val next = Armageddon.instance.routeManager.getNext()
                if(next == null) {
                    Logger.error("No next block found!")
                }
                RotationUtil.ease(AngleUtil.getRotationForBlock(), ArmageddonConfig.lookAtBlockTime)
            },
            ArmageddonConfig.lookAtBlockTime
        )

        private var i = -1
        private val TELEPORT = ConditionalState(
            {
                i = Armageddon.instance.routeManager.getStandingOn() + 1
                this.disable()
                KeyBindUtil.rightClick()
            },
            { Armageddon.instance.routeManager.getStandingOn() != i },
            2500,
            { Logger.error("Player did not teleport in time!") }
        )

        private val UNSNEAK = TimedState(
            { KeyBindUtil.setPressed(gameSettings.keyBindSneak, false) },
            100,
        )
    }

    override fun onEnable(): Boolean {
        if (Armageddon.instance.routeManager.getStandingOn() == -1) {
            Logger.error("You are not standing on a block on the route. Make sure to AOTV on it, so you are centered.")
            return false
        }
        Logger.info("Starting macro.")
        return true
    }

    override fun onDisable() {
        Logger.info("Stopping macro.")
    }
}