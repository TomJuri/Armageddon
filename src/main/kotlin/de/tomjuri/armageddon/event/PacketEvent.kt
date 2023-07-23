package de.tomjuri.armageddon.event

import net.minecraft.network.Packet
import net.minecraftforge.fml.common.eventhandler.Event

data class PacketEvent(val packet: Packet<*>) : Event()