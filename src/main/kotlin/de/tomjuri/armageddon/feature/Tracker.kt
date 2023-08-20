package de.tomjuri.armageddon.feature

import de.tomjuri.armageddon.util.MagnitudeUtil
import de.tomjuri.armageddon.util.player
import de.tomjuri.armageddon.util.world
import net.minecraft.entity.player.EntityPlayer

class Tracker {

    private var macroStateTime = 0L
    private var totalProfit = 0L
    private var profitPerHour = 0L

    val nearbyPlayers get() = world.loadedEntityList.filter { it is EntityPlayer && it.getDistanceToEntity(player) <= 25}.size

    fun getTotalProfit() = MagnitudeUtil.magnitudify(totalProfit)
    fun getProfitPerHour() = MagnitudeUtil.magnitudify(profitPerHour)
    fun getRuntime() = MagnitudeUtil.msToTime(System.currentTimeMillis() - macroStateTime)
}