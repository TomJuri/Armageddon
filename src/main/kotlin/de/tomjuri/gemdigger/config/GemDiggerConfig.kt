package de.tomjuri.gemdigger.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.KeyBind
import cc.polyfrost.oneconfig.config.annotations.Slider
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.annotations.Text
import cc.polyfrost.oneconfig.config.core.OneKeyBind
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.libs.universal.UKeyboard
import de.tomjuri.gemdigger.GemDigger

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

    @Switch(name = "Mute game sounds")
    val muteGameSounds = false



    init {
        initialize()
        registerKeyBind(toggleMacro) { GemDigger.macro.toggle() }
    }
}