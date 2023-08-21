package de.tomjuri.armageddon.feature

import de.tomjuri.armageddon.util.MagnitudeUtil
import de.tomjuri.armageddon.util.player
import de.tomjuri.armageddon.util.world
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class Tracker {

    var macroStartTime = 0L
    private var totalProfit = 0L
    private var profitPerHour = 0L

    val nearbyPlayers get() = world.loadedEntityList.filter { it is EntityPlayer && it.getDistanceToEntity(player) <= 25}.size

    fun getTotalProfit() = MagnitudeUtil.magnitudify(totalProfit)
    fun getProfitPerHour() = MagnitudeUtil.magnitudify(profitPerHour)
    fun getRuntime() = MagnitudeUtil.msToTime(System.currentTimeMillis() - macroStartTime)

    @SubscribeEvent
    fun onInventoryChange(event: ClientChatReceivedEvent) {
        val unformatted = StringUtils.stripControlCodes(event.message.unformattedText)
        if(!unformatted.contains("PRISTINE! You found ")) return
        val qualities = mapOf("Rough" to 3, "Flawed" to 240, "Fine" to 19200)
        val qualityType = qualities.keys.find { type -> unformatted.contains(type) }
        val quantity = Regex("\\d+").find(unformatted)?.value?.toIntOrNull() ?: 0
        totalProfit += quantity * qualities[qualityType]!!
        profitPerHour = (totalProfit * 3600000) / (System.currentTimeMillis() - macroStartTime)
    }
}