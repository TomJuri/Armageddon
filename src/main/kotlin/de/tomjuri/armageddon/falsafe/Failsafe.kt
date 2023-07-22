package de.tomjuri.armageddon.falsafe

import de.tomjuri.armageddon.Armageddon
import de.tomjuri.armageddon.event.ReceivePacketEvent
import de.tomjuri.armageddon.util.*
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

class Failsafe {

    private val pingAlertTimer: Timer = Timer()
    var pingAlertPlaying = false
    private var numPings = 15

    private fun emergency() {
        pingAlertPlaying = true
        numPings = 15
        Armageddon.macro.stop()
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!pingAlertPlaying) return
        if (event.phase === TickEvent.Phase.START) return
        if (numPings <= 0) {
            pingAlertPlaying = false
            return
        }
        if (pingAlertTimer.isDone()) {
            world.playSound(player.posX, player.posY, player.posZ, "random.orb", 10.0f, 1.0f, false)
            pingAlertTimer.startTimer(100)
            numPings--
        }
    }

    @SubscribeEvent
    fun onReceivePacket(event: ReceivePacketEvent) {
        if(mc.thePlayer == null) return
        if (event.packet is S08PacketPlayerPosLook) {
          //  emergency()
        }
    }
}