package de.tomjuri.armageddon.event

import de.tomjuri.annotation.NoNative
import net.minecraft.network.Packet
import net.minecraftforge.fml.common.eventhandler.Event

@NoNative
data class PacketEvent(val packet: Packet<*>) : Event()