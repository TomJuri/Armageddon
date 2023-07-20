package de.tomjuri.gemdigger

import cc.polyfrost.oneconfig.utils.commands.CommandManager
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(modid = "gemdigger", name = "GemDigger", version = "%%VERSION%%")
class GemDigger {

    companion object {
        lateinit var config: GemDiggerConfig
        lateinit var routeManager: RouteManager
        lateinit var macro: Macro
    }

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        config = GemDiggerConfig()
        routeManager = RouteManager()
        macro = Macro()
        CommandManager.register(ResetCommand())
        CommandManager.register(ResetRouteCommand())
        MinecraftForge.EVENT_BUS.register(routeManager)
        MinecraftForge.EVENT_BUS.register(macro)

    }
}