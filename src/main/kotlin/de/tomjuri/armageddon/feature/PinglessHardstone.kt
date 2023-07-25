package de.tomjuri.armageddon.feature

import de.tomjuri.armageddon.util.world
import net.minecraft.init.Blocks
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class PinglessHardstone {
    @SubscribeEvent
    fun onStartBreakingBlock(event: PlayerEvent.BreakSpeed) {
        if (event.state.block != Blocks.stone) return
        world.setBlockToAir(event.pos)
    }
}