package de.tomjuri.gemdigger

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.Slider
import cc.polyfrost.oneconfig.config.annotations.Text
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType

class ExampleModConfig : Config(Mod("examplemod", ModType.UTIL_QOL), "examplemod.json") {

    init {
        initialize()
    }

    @Text(name = "Route")
    val route = ""
    @Slider(name = "Drill slot", min = 0f, max = 8f)
    val drillSlot = 0f
    @Slider(name = "Rod slot", min = 0f, max = 8f)
    val rodSlot = 1f
    @Slider(name = "AOTV slot", min = 0f, max = 8f, )
    val aotvSlot = 2f
}