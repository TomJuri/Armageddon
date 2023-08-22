package de.tomjuri.armageddon.util

import net.minecraft.util.BlockPos
import org.json.JSONArray

object RouteParser {
    fun parseRoute(route: String?): MutableList<BlockPos> {
        return try {
            val json = JSONArray(route)
            val waypoints: MutableList<BlockPos> = ArrayList()
            for (i in 0 until json.length()) {
                val obj = json.getJSONObject(i)
                val bp = BlockPos(obj.getInt("x"), obj.getInt("y"), obj.getInt("z"))
                waypoints.add(bp)
            }
            waypoints
        } catch (ignored: Throwable) {
            ArrayList()
        }
    }
}
