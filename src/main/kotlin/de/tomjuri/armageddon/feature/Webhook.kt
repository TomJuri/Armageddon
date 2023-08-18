package de.tomjuri.armageddon.feature

import de.tomjuri.armageddon.util.Logger
import de.tomjuri.armageddon.util.Timer
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

class Webhook {

    private var timer = Timer(0)

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (!timer.isOver()) return
        timer = Timer(60_000)
        Thread {
            Logger.sendStatusWebhook()
        }.start()
    }
}