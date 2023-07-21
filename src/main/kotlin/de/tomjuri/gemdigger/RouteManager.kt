package de.tomjuri.gemdigger

import de.tomjuri.gemdigger.util.*
import net.minecraft.init.Blocks
import net.minecraft.util.BlockPos
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

class RouteManager {

    var route = RouteParser.parseRoute(GemDigger.config.route)

    fun reloadRoute() {
        route = RouteParser.parseRoute(GemDigger.config.route)
        if (route.isEmpty()) {
            Logger.error("Unable to parse route!")
            return
        }
        Logger.info("Route was reloaded")
    }

    @SubscribeEvent
    fun onRenderWorldLast(event: RenderWorldLastEvent) {
        if (route.isEmpty()) return
        route.forEach {
            if (world.getBlockState(it).block == Blocks.cobblestone)
                RenderUtil.drawBlockBox(it, Color.GREEN)
            else
                RenderUtil.drawBlockBox(it, Color.RED)
            RenderUtil.renderText(it, route.indexOf(it).toString())
        }
    }
}