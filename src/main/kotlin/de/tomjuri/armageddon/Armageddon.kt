package de.tomjuri.armageddon

import cc.polyfrost.oneconfig.utils.commands.CommandManager
import de.tomjuri.armageddon.command.ArmageddonCommand
import de.tomjuri.armageddon.config.ArmageddonConfig
import de.tomjuri.armageddon.feature.Failsafe
import de.tomjuri.armageddon.macro.Macro
import de.tomjuri.armageddon.macro.RouteManager
import de.tomjuri.macroframework.MacroFramework
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(modid = "armageddon", name = "Armageddon", version = "${Armageddon.COMMIT}/${Armageddon.BRANCH}", acceptedMinecraftVersions = "[1.8.9]")
class Armageddon {

    companion object {
        @Mod.Instance("armageddon")
        lateinit var instance: Armageddon
        const val COMMIT = "%%COMMIT%%"
        const val BRANCH = "%%BRANCH%%"
    }

    lateinit var config: ArmageddonConfig
    lateinit var failsafe: Failsafe
    lateinit var macro: Macro
    lateinit var routeManager: RouteManager

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        MacroFramework.init()
        config = ArmageddonConfig()
        failsafe = Failsafe()
        routeManager = RouteManager()
        macro = Macro()
        CommandManager.register(ArmageddonCommand())
        MinecraftForge.EVENT_BUS.register(failsafe)
        MinecraftForge.EVENT_BUS.register(macro)
        MinecraftForge.EVENT_BUS.register(routeManager)
    }
}
