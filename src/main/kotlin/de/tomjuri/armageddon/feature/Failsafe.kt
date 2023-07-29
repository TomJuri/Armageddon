package de.tomjuri.armageddon.feature

import de.tomjuri.annotation.NoNative
import de.tomjuri.armageddon.Armageddon
import de.tomjuri.armageddon.event.PacketEvent
import de.tomjuri.armageddon.util.*
import net.minecraft.init.Blocks
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import net.minecraft.util.BlockPos
import net.minecraftforge.event.world.WorldEvent.Unload
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent

class Failsafe {

    private val pingAlertTimer = Timer()
    private val reactionStartTimer = Timer()
    private var shouldReact = false
    private var movement = ""
    var pingAlertPlaying = false
    private var numPings = 15
    private val messages = listOf("wtf", "wtf??", "lmao", "lmfao", "tf", "ok admins",
            "?!?", "check?", "uhhhh", "nice", "oh wtf", "bruh that scared me", "bruh")

    fun emergency(message: String, movement: String) {
        Logger.error(message)
        reactionStartTimer.startTimer(1500)
        pingAlertPlaying = true
        numPings = 15
        shouldReact = true
        this.movement = movement
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if(reactionStartTimer.isDone() && shouldReact) {
            Armageddon.macro.stop()
            shouldReact = false
            if(movement.isNotEmpty())
                player.sendChatMessage("/ac ${messages.random()}")
                Armageddon.movementRecorder.play(movement)
        }
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

    var canRotationCheckTrigger = true

    @NoNative
    @SubscribeEvent
    fun onReceivePacket(event: PacketEvent) {
        if(!Armageddon.macro.isRunning()) return
        if (event.packet !is S08PacketPlayerPosLook)
            return
        if (!canRotationCheckTrigger)
            return
        if (event.packet.pitch - player.rotationPitch * -1 > Armageddon.config.rotationCheckThreshold || event.packet.yaw - player.rotationYaw * -1 > Armageddon.config.rotationCheckThreshold)
            emergency("You probably have been rotation checked!", failsafeMovementNoMovement)
    }

    @SubscribeEvent
    fun onDisconnect(event: ClientDisconnectionFromServerEvent) {
        if(!Armageddon.macro.isRunning()) return
        Armageddon.macro.stop()
    }

    @SubscribeEvent
    fun onUnloadWorld(event: Unload) {
        if (!Armageddon.macro.isRunning()) return
        emergency("You probably have been kicked from the server!", "")
    }

    @SubscribeEvent
    fun onTick0(event: ClientTickEvent) {
        if (!Armageddon.macro.isRunning()) return
        val loc = player.position
        val top = world.getBlockState(BlockPos(loc.x, loc.y + 2, loc.z)).block
        val bottom = world.getBlockState(BlockPos(loc.x, loc.y - 1, loc.z)).block
        val leftBottom = world.getBlockState(BlockPos(loc.x - 1, loc.y, loc.z)).block
        val leftTop = world.getBlockState(BlockPos(loc.x - 1, loc.y + 1, loc.z)).block
        val rightBottom = world.getBlockState(BlockPos(loc.x + 1, loc.y, loc.z)).block
        val rightTop = world.getBlockState(BlockPos(loc.x + 1, loc.y + 1, loc.z)).block
        val frontBottom = world.getBlockState(BlockPos(loc.x, loc.y, loc.z - 1)).block
        val frontTop = world.getBlockState(BlockPos(loc.x, loc.y + 1, loc.z - 1)).block
        val backBottom = world.getBlockState(BlockPos(loc.x, loc.y, loc.z + 1)).block
        val backTop = world.getBlockState(BlockPos(loc.x, loc.y + 1, loc.z + 1)).block

        if (
                top == Blocks.stone
                && bottom == Blocks.stone
                && leftBottom == Blocks.stone
                && leftTop == Blocks.stone
                && rightBottom == Blocks.stone
                && rightTop == Blocks.stone
                && frontBottom == Blocks.stone
                && frontTop == Blocks.stone
                && backBottom == Blocks.stone
                && backTop == Blocks.stone
        ) {
            emergency("You probably have been hardstone box checked!", failsafeMovementNoMovement)
        }
    }

    @SubscribeEvent
    fun onTick1(event: ClientTickEvent) {
        if (!Armageddon.macro.isRunning()) return
        val radius = 20
        var bedrock = 0
        var oak = false
        for (x in -radius..radius) {
            for (y in -radius..radius) {
                for (z in -radius..radius) {
                    if (world.getBlockState(player.position.add(x, y, z)).equals(Blocks.bedrock)) bedrock++
                    if (world.getBlockState(player.position.add(x, y, z)).equals(Blocks.log)) oak = true
                }
            }
        }
        if(bedrock > 2 && oak)
            emergency("You probably have been bedrock box checked!", failsafeMovement)
    }
}