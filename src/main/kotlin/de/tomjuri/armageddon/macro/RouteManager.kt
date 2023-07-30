package de.tomjuri.armageddon.macro

import de.tomjuri.armageddon.Armageddon
import de.tomjuri.armageddon.util.*
import net.minecraft.init.Blocks
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color
import de.tomjuri.armageddon.util.RenderUtil

import net.minecraft.util.MovingObjectPosition

import net.minecraft.util.Vec3

import net.minecraft.util.BlockPos

import net.minecraft.init.Blocks

import de.tomjuri.armageddon.Armageddon

import net.minecraftforge.client.event.RenderWorldLastEvent

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

import de.tomjuri.armageddon.util.RouteParser




class RouteManager {

    private var route: List<BlockPos>? = null

    fun reloadRoute() {
        route = RouteParser.parseRoute(Armageddon.config.route)
        if (route!!.isEmpty()) {
            Logger.error("Unable to parse route!")
            return
        }
        Logger.info("Route was reloaded")
    }

    fun findStandingOn(): Int {
        for (pos in route) {
            if (player.posX === pos.getX() + 0.5 && player.posZ === pos.getZ() + 0.5) return route!!.indexOf(pos)
        }
        return -1
    }

    @SubscribeEvent
    fun onRenderWorldLast(event: RenderWorldLastEvent?) {
        if (route!!.isEmpty() || !Armageddon.config.showWaypoints) return
        for (pos in route) {
            if (world.getBlockState(pos).getBlock() === Blocks.cobblestone) RenderUtil.drawBlockBox(
                event,
                pos,
                Color.GREEN
            ) else RenderUtil.drawBlockBox(event, pos, Color.MAGENTA)
            RenderUtil.renderText(pos, route!!.indexOf(pos).toString())
            val next: BlockPos = route!![kotlin.math.min(route!!.indexOf(pos) + 1, route!!.size - 1)]
            val hit: MovingObjectPosition = player.worldObj.rayTraceBlocks(
                Vec3(pos.getX() + 0.5, pos.getY() + 2.54, pos.getZ() + 0.5),
                Vec3(next.getX() + 0.5, next.getY() + 0.5, next.getZ() + 0.5)
            )
            if (hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && hit.getBlockPos() !== next) RenderUtil.drawBlockBox(
                event,
                hit.getBlockPos(),
                Color.RED
            )
        }
    }
}