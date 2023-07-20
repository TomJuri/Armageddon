package de.tomjuri.gemdigger

import de.tomjuri.gemdigger.util.Logger
import de.tomjuri.gemdigger.util.RenderUtil
import de.tomjuri.gemdigger.util.RouteParser
import de.tomjuri.gemdigger.util.world
import net.minecraft.init.Blocks
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

class RouteManager {

    var current = 0
    var route = RouteParser.parseRoute(GemDigger.config.route)

    fun reloadRoute() {
        current = 0
        route = RouteParser.parseRoute(GemDigger.config.route)
        if(route.isEmpty()) {
            Logger.error("Unable to parse route!")
            return
        }
        Logger.info("Route was reloaded")
    }

    @SubscribeEvent
    fun onRenderWorldLast(event: RenderWorldLastEvent) {
        if (route.isEmpty()) return
        for (i in -1..1) {
            if(current + i < 0 || current + i > route.size - 1) continue
            val block = route[current + i]
            if (world.getBlockState(block).block == Blocks.cobblestone)
                RenderUtil.drawBlockBox(block, Color.GREEN)
            else
                RenderUtil.drawBlockBox(block, Color.RED)
            RenderUtil.renderText(block, (current + i).toString())
        }
    }

}