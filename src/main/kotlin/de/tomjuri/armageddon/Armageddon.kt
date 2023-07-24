package de.tomjuri.armageddon

import cc.polyfrost.oneconfig.utils.commands.CommandManager
import de.tomjuri.armageddon.command.ArmageddonCommand
import de.tomjuri.armageddon.config.ArmageddonConfig
import de.tomjuri.armageddon.failsafe.Failsafe
import de.tomjuri.armageddon.macro.Macro
import de.tomjuri.armageddon.macro.RouteManager
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(modid = "armageddon", name = "Armageddon", version = "%%VERSION%%")
class Armageddon {

    companion object {
        lateinit var config: ArmageddonConfig
        lateinit var routeManager: RouteManager
        lateinit var macro: Macro
        lateinit var failsafe: Failsafe
    }

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        Auth.auth()
        config = ArmageddonConfig()
        routeManager = RouteManager()
        macro = Macro()
        failsafe = Failsafe()
        CommandManager.register(ArmageddonCommand())
        MinecraftForge.EVENT_BUS.register(routeManager)
        MinecraftForge.EVENT_BUS.register(macro)
        MinecraftForge.EVENT_BUS.register(failsafe)
    }
}