package de.tomjuri.gemdigger.event

import net.minecraft.network.Packet
import net.minecraftforge.fml.common.eventhandler.Event

data class ReceivePacketEvent(val packet: Packet<*>) : Event()