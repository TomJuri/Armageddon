package de.tomjuri.gemdigger.util

import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.multiplayer.WorldClient

val mc: Minecraft
    get() = Minecraft.getMinecraft()
val player: EntityPlayerSP
    get() = mc.thePlayer
val world: WorldClient
    get() = mc.theWorld