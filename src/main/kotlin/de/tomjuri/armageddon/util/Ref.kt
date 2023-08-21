package de.tomjuri.armageddon.util

import de.tomjuri.armageddon.Armageddon
import net.minecraft.client.Minecraft

val mc
    get() = Minecraft.getMinecraft()
val player
    get() = mc.thePlayer
val world
    get() = mc.theWorld
val gameSettings
    get() = mc.gameSettings
val armageddon
    get() = Armageddon.instance
val macro
    get() = Armageddon.instance.macro
val routeManager
    get() = Armageddon.instance.routeManager
val failsafe
    get() = Armageddon.instance.failsafe
val config
    get() = Armageddon.instance.config
val tracker
    get() = Armageddon.instance.tracker