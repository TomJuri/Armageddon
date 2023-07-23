package de.tomjuri.armageddon.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.KeyBind
import cc.polyfrost.oneconfig.config.annotations.Slider
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.annotations.Text
import cc.polyfrost.oneconfig.config.core.OneKeyBind
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.libs.universal.UKeyboard
import de.tomjuri.armageddon.Armageddon

class ArmageddonConfig : Config(Mod("armageddon", ModType.UTIL_QOL), "armageddon.json") {

    @KeyBind(name = "Toggle macro")
    var toggleMacro = OneKeyBind(UKeyboard.KEY_C)

    @Text(name = "Route")
    var route = ""

    @Slider(name = "Drill slot", min = 1f, max = 8f, step = 1)
    var drillSlot = 1

    @Slider(name = "Rod slot", min = 1f, max = 8f, step = 1)
    var rodSlot = 2

    @Slider(name = "AOTV slot", min = 1f, max = 8f, step = 1)
    var aotvSlot = 3

    @Switch(name = "Show waypoints")
    var showWaypoints = true

    @Switch(name = "Mute game sounds")
    var muteGameSounds = false



    init {
        initialize()
        registerKeyBind(toggleMacro) { Armageddon.macro.toggle() }
    }
}