package de.tomjuri.gemdigger

import cc.polyfrost.oneconfig.utils.commands.CommandManager
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(modid = "gemdigger", name = "GemDigger", version = "%%VERSION%%")
class ExampleMod {

    companion object {
        lateinit var config: ExampleModConfig
    }

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        config = ExampleModConfig()
        CommandManager.register(ExampleCommand())
        println("Sup world")
    }
}