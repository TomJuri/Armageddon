package de.tomjuri.armageddon.util;

import net.minecraft.util.BlockPos;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RouteParser {
    public static List<BlockPos> parseRoute(String route) {
        try {
            JSONArray json = new JSONArray(route);
            List<BlockPos> waypoints = new ArrayList<>();
            for (int i = 0; i < json.length(); i++) {
                JSONObject obj = json.getJSONObject(i);
                BlockPos bp = new BlockPos(obj.getInt("x"), obj.getInt("y"), obj.getInt("z"));
                waypoints.add(bp);
            }
            return waypoints;
        } catch (Throwable ignored) {
            return new ArrayList<>();
        }
    }
}
