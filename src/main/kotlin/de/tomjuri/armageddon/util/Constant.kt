package de.tomjuri.armageddon.util

import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.multiplayer.WorldClient
@NoNative
val mc: Minecraft
    get() = Minecraft.getMinecraft()
@NoNative
val player: EntityPlayerSP
    get() = mc.thePlayer
@NoNative
val world: WorldClient
    get() = mc.theWorld