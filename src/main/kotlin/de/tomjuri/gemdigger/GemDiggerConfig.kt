package de.tomjuri.gemdigger

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.KeyBind
import cc.polyfrost.oneconfig.config.annotations.Number
import cc.polyfrost.oneconfig.config.annotations.Slider
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.annotations.Text
import cc.polyfrost.oneconfig.config.core.OneKeyBind
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.libs.universal.UKeyboard

class GemDiggerConfig : Config(Mod("gemdigger", ModType.UTIL_QOL), "gemdigger.json") {

    @KeyBind(name = "Toggle macro")
    val toggleMacro = OneKeyBind(UKeyboard.KEY_C)

    @Text(name = "Route")
    val route = ""

    @Slider(name = "Drill slot", min = 1f, max = 8f, step = 1)
    val drillSlot = 1

    @Slider(name = "Rod slot", min = 1f, max = 8f, step = 1)
    val rodSlot = 2

    @Slider(name = "AOTV slot", min = 1f, max = 8f, step = 1)
    val aotvSlot = 3

    @Switch(name = "Show waypoints")
    val showWaypoints = true

 /*   @Number(name = "Use rod delay", min = 60f, max = 1000f, step = 1)
    val dela = 100

    @Number(name = "Mount dillo delay", min = 60f, max = 1000f, step = 1)
    val delayAfterUseRod = 100

    @Number(name = "Delay after slot to Rod", min = 60f, max = 1000f, step = 1)
    val  = 100*/



    init {
        initialize()
        registerKeyBind(toggleMacro) { GemDigger.macro.toggle() }
    }
}