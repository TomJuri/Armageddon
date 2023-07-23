package de.tomjuri.armageddon.macro

import de.tomjuri.armageddon.Armageddon
import de.tomjuri.armageddon.util.*
import net.minecraft.init.Blocks
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

class RouteManager {

    var route = RouteParser.parseRoute(Armageddon.config.route)

    fun reloadRoute() {
        route = RouteParser.parseRoute(Armageddon.config.route)
        if (route.isEmpty()) {
            Logger.error("Unable to parse route!")
            return
        }
        Logger.info("Route was reloaded")
    }

    fun findStandingOn(): Int {
        route.forEach {
            if (player.posX == it.x + 0.5 && player.posZ == it.z + 0.5)
                return route.indexOf(it)
        }
        return -1
    }

    @SubscribeEvent
    fun onRenderWorldLast(event: RenderWorldLastEvent) {
        if (route.isEmpty() || !Armageddon.config.showWaypoints) return
        route.forEach {
            if (world.getBlockState(it).block == Blocks.cobblestone)
                RenderUtil.drawBlockBox(event, it, Color.GREEN)
            else
                RenderUtil.drawBlockBox(event, it, Color.MAGENTA)
            RenderUtil.renderText(it, route.indexOf(it).toString())

            var next = route.getOrNull(route.indexOf(it) + 1)
            if (next == null)
                next = route[0]

            val hit: MovingObjectPosition? = player.worldObj.rayTraceBlocks(Vec3(it.x + .5, it.y + 2.54, it.z + .5), Vec3(next.x + 0.5, next.y + 0.5, next.z + 0.5))
            if (hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && hit.blockPos != next)
                RenderUtil.drawBlockBox(event, hit.blockPos, Color.RED)
        }
    }
}