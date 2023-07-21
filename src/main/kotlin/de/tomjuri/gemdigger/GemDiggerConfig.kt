package de.tomjuri.gemdigger

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.KeyBind
import cc.polyfrost.oneconfig.config.annotations.Slider
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
    @Slider(name = "Drill slot", min = 0f, max = 8f)
    val drillSlot = 0
    @Slider(name = "Rod slot", min = 0f, max = 8f)
    val rodSlot = 1
    @Slider(name = "AOTV slot", min = 0f, max = 8f)
    val aotvSlot = 2
    init {
        initialize()
        registerKeyBind(toggleMacro) { GemDigger.macro.toggle() }
    }
}