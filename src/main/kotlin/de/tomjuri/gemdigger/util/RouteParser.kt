package de.tomjuri.gemdigger.util

import net.minecraft.util.BlockPos
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


object RouteParser {
    fun parseRoute(route: String): List<BlockPos> {
        return try {
            val json = JSONArray(route)
            val waypoints: MutableList<BlockPos> = ArrayList()
            for (i in 0 until json.length()) {
                val obj: JSONObject = json.getJSONObject(i)
                val bp = BlockPos(obj.getInt("x"), obj.getInt("y"), obj.getInt("z"))
                waypoints.add(bp)
            }
            waypoints
        } catch (ignored: Throwable) {
            emptyList()
        }
    }
}