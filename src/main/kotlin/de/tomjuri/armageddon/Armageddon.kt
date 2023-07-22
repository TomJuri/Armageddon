package de.tomjuri.armageddon

import cc.polyfrost.oneconfig.utils.commands.CommandManager
import de.tomjuri.armageddon.command.ReloadRouteCommand
import de.tomjuri.armageddon.config.ArmageddonConfig
import de.tomjuri.armageddon.falsafe.Failsafe
import de.tomjuri.armageddon.macro.Macro
import de.tomjuri.armageddon.macro.RouteManager
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(modid = "armageddon", name = "Armageddon", version = "%%VERSION%%")
class Armageddon {

    private val message = "If you managed to crack this mod, message me on Discord(tomjuri) for a prize"
    private val message2 = "But you're probably not going to be able to read this, gotta love String Encryption"

    companion object {
        lateinit var config: ArmageddonConfig
        lateinit var routeManager: RouteManager
        lateinit var macro: Macro
        lateinit var failsafe: Failsafe
    }

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        config = ArmageddonConfig()
        routeManager = RouteManager()
        macro = Macro()
        failsafe = Failsafe()
        CommandManager.register(ReloadRouteCommand())
        MinecraftForge.EVENT_BUS.register(routeManager)
        MinecraftForge.EVENT_BUS.register(macro)
        MinecraftForge.EVENT_BUS.register(failsafe)
    }
}