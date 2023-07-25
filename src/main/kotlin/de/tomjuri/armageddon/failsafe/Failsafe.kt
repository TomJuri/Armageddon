package de.tomjuri.armageddon.failsafe

import de.tomjuri.armageddon.Armageddon
import de.tomjuri.armageddon.event.PacketEvent
import de.tomjuri.armageddon.util.Logger
import de.tomjuri.armageddon.util.Timer
import de.tomjuri.armageddon.util.player
import de.tomjuri.armageddon.util.world
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import net.minecraftforge.event.world.WorldEvent.Unload
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent

class Failsafe {

    private val pingAlertTimer: Timer = Timer()
    var pingAlertPlaying = false
    private var numPings = 15

    private fun emergency(message: String) {
        Logger.error(message)
        pingAlertPlaying = true
        numPings = 15
        Armageddon.macro.stop()
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (!pingAlertPlaying) return
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

    // rotation check
    var canRotationCheckTrigger = true
    @SubscribeEvent
    fun onReceivePacket(event: PacketEvent) {
        if (event.packet !is S08PacketPlayerPosLook)
            return
        if(!canRotationCheckTrigger)
            return
        if(event.packet.pitch - player.rotationPitch * -1 > Armageddon.config.rotationCheckThreshold || event.packet.yaw - player.rotationYaw * -1 > Armageddon.config.rotationCheckThreshold)
            emergency("You probably have been rotation checked!")
    }

    // disconnect and switch server failsafe
    @SubscribeEvent
    fun onDisconnect(event: ClientDisconnectionFromServerEvent) {
        Armageddon.macro.stop()
    }

    @SubscribeEvent
    fun onUnloadWorld(event: Unload) {
        if(Armageddon.macro.isRunning()) {
            emergency("You probably have been kicked from the server!")
        }
    }
}