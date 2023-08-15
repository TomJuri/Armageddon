package de.tomjuri.armageddon.macro

import de.tomjuri.armageddon.config.ArmageddonConfig
import de.tomjuri.armageddon.util.Logger
import de.tomjuri.armageddon.util.RenderUtil
import de.tomjuri.armageddon.util.RouteParser
import de.tomjuri.macroframework.util.player
import de.tomjuri.macroframework.util.world
import net.minecraft.init.Blocks
import net.minecraft.util.BlockPos
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color
import kotlin.math.min

class RouteManager {
    private var route: List<BlockPos> = ArrayList()

    fun reloadRoute() {
        route = RouteParser.parseRoute(ArmageddonConfig.route)
        if (route.isEmpty()) {
            Logger.error("Unable to parse route!")
            return
        }
        Logger.info("Route was reloaded!")
    }

    fun getStandingOn(): Int {
        for (pos in route) {
            if (player.posX == pos.x + 0.5 && player.posZ == pos.z + 0.5)
                return route.indexOf(pos)
        }
        return -1
    }

    fun getNext(): BlockPos? {
        val standingOn = getStandingOn()
        if (standingOn == -1) return null
        var next = standingOn + 1
        if (next >= route.size) next = 0
        return route[next]
    }

    @SubscribeEvent
    fun onRenderWorldLast(event: RenderWorldLastEvent) {
        if (route.isEmpty() || !ArmageddonConfig.showWaypoints) return
        for (pos in route) {
            if (world.getBlockState(pos).block === Blocks.cobblestone)
                RenderUtil.drawBlockBox(event, pos, Color.GREEN)
            else
                RenderUtil.drawBlockBox(event, pos, Color.MAGENTA)
            RenderUtil.renderText(pos, route.indexOf(pos).toString())
            val next = route[min(route.size - 1, route.indexOf(pos) + 1)]
            val hit: MovingObjectPosition = player.worldObj.rayTraceBlocks(
                Vec3(pos.x + 0.5, pos.y + 2.54, pos.z + 0.5),
                Vec3(next.x + 0.5, next.y + 0.5, next.z + 0.5)
            )
            if (hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && hit.blockPos !== next)
                RenderUtil.drawBlockBox(event, hit.blockPos, Color.RED)
        }
    }
}