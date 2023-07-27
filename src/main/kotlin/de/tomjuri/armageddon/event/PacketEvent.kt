package de.tomjuri.armageddon.event

import de.tomjuri.armageddon.util.NoNative
import net.minecraft.network.Packet
import net.minecraftforge.fml.common.eventhandler.Event

@NoNative
data class PacketEvent(val packet: Packet<*>) : Event()