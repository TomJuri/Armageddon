package de.tomjuri.armageddon.macro;

import de.tomjuri.armageddon.Armageddon;
import de.tomjuri.armageddon.config.ArmageddonConfig;
import de.tomjuri.armageddon.util.Logger;
import de.tomjuri.armageddon.util.RenderUtil;
import de.tomjuri.armageddon.util.RouteParser;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RouteManager {

    private static List<BlockPos> route = new ArrayList<>();

    public static void reloadRoute() {
        route = RouteParser.parseRoute(ArmageddonConfig.route);
        if (route.isEmpty()) {
            Logger.error("Unable to parse route!");
            return;
        }
        Logger.info("Route was reloaded");
    }

    public static int findStandingOn() {
        for (BlockPos pos : route) {
            if (Armageddon.INSTANCE.getPlayer().posX == pos.getX() + 0.5 && Armageddon.INSTANCE.getPlayer().posZ == pos.getZ() + 0.5)
                return route.indexOf(pos);
        }
        return -1;
    }

    public static BlockPos getNext() {
        int index = findStandingOn();
        if(index == -1)
            return null;
        int nextIndex = index + 1;
        if (nextIndex >= route.size())
            nextIndex = 0;
        return route.get(nextIndex);
    }

    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        if (route.isEmpty() || !ArmageddonConfig.showWaypoints)
            return;
        for (BlockPos pos : route) {
            if (Armageddon.INSTANCE.getWorld().getBlockState(pos).getBlock() == Blocks.cobblestone)
                RenderUtil.drawBlockBox(event, pos, Color.GREEN);
            else
                RenderUtil.drawBlockBox(event, pos, Color.MAGENTA);
            RenderUtil.renderText(pos, String.valueOf(route.indexOf(pos)));
            BlockPos next = route.get(Math.min(route.indexOf(pos) + 1, route.size() - 1));
            MovingObjectPosition hit = Armageddon.INSTANCE.getPlayer().worldObj.rayTraceBlocks(
                    new Vec3(pos.getX() + 0.5, pos.getY() + 2.54, pos.getZ() + 0.5),
                    new Vec3(next.getX() + 0.5, next.getY() + 0.5, next.getZ() + 0.5)
            );
            if (hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && hit.getBlockPos() != next)
                RenderUtil.drawBlockBox(event, hit.getBlockPos(), Color.RED);
        }
    }
}
