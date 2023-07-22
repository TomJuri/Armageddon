package de.tomjuri.gemdigger

import cc.polyfrost.oneconfig.utils.commands.CommandManager
import de.tomjuri.gemdigger.command.ResetRouteCommand
import de.tomjuri.gemdigger.config.GemDiggerConfig
import de.tomjuri.gemdigger.falsafe.Failsafe
import de.tomjuri.gemdigger.macro.Macro
import de.tomjuri.gemdigger.macro.RouteManager
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(modid = "gemdigger", name = "GemDigger", version = "%%VERSION%%")
class GemDigger {

    private val message = "If you managed to crack this mod, message me on Discord(tomjuri) for a prize"
    private val message2 = "But you're probably not going to be able to read this, gotta love String Encryption"

    companion object {
        lateinit var config: GemDiggerConfig
        lateinit var routeManager: RouteManager
        lateinit var macro: Macro
        lateinit var failsafe: Failsafe
    }

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        config = GemDiggerConfig()
        routeManager = RouteManager()
        macro = Macro()
        failsafe = Failsafe()
        CommandManager.register(ResetRouteCommand())
        MinecraftForge.EVENT_BUS.register(routeManager)
        MinecraftForge.EVENT_BUS.register(macro)
        MinecraftForge.EVENT_BUS.register(failsafe)

    }
}