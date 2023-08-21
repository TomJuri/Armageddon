package de.tomjuri.armageddon

import cc.polyfrost.oneconfig.utils.commands.CommandManager
import de.tomjuri.armageddon.command.ArmageddonCommand
import de.tomjuri.armageddon.config.ArmageddonConfig
import de.tomjuri.armageddon.feature.Failsafe
import de.tomjuri.armageddon.feature.Tracker
import de.tomjuri.armageddon.feature.Webhook
import de.tomjuri.armageddon.macro.Macro
import de.tomjuri.armageddon.macro.RouteManager
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(
    modid = "armageddon",
    name = "Armageddon",
    version = "${Armageddon.COMMIT}/${Armageddon.BRANCH}",
    acceptedMinecraftVersions = "[1.8.9]"
)
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
    lateinit var tracker: Tracker

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        config = ArmageddonConfig()
        failsafe = Failsafe()
        tracker = Tracker()
        routeManager = RouteManager()
        macro = Macro()
        CommandManager.register(ArmageddonCommand())
        MinecraftForge.EVENT_BUS.register(failsafe)
        MinecraftForge.EVENT_BUS.register(macro)
        MinecraftForge.EVENT_BUS.register(routeManager)
        MinecraftForge.EVENT_BUS.register(tracker)
        MinecraftForge.EVENT_BUS.register(Webhook())
    }

    // needed for hud /shrug
    fun isTrackerInitialized(): Boolean {
        return this::tracker.isInitialized
    }
}
