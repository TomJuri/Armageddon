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
import de.tomjuri.armageddon.util.macro

class ArmageddonConfig : Config(Mod("armageddon", ModType.UTIL_QOL), "armageddon.json") {

    @KeyBind(name = "Toggle macro")
    var toggleMacro = OneKeyBind(UKeyboard.KEY_C)

    @Text(name = "Route", secure = false, multiline = false, size = 6)
    var route = ""

    @Text(name = "Webhook URL", secure = true, multiline = false, size = 5)
    var webhookUrl = ""

    @Slider(name = "Drill slot", min = 1f, max = 8f, step = 1)
    var drillSlot = 1

    @Slider(name = "Rod slot", min = 1f, max = 8f, step = 1)
    var rodSlot = 2

    @Slider(name = "AOTV slot", min = 1f, max = 8f, step = 1)
    var aotvSlot = 3

    @Slider(name = "Abiphone slot", min = 1f, max = 8f, step = 1)
    var abiphoneSlot = 4

    @JvmField
    @Switch(name = "Show waypoints")
    var showWaypoints = true

    @JvmField
    @Switch(name = "Mute game sounds")
    var muteGameSounds = false

    @Switch(name = "Abiphone refuel")
    var abiphoneRefuel = false

    @Slider(name = "Swipe range", min = 180f, max = 360f, step = 1)
    var swipeRange = 320f

    @Slider(name = "Swipe time", min = 300f, max = 1000f, step = 1)
    var swipeTime = 500

    @Slider(name = "Look at block time", min = 150f, max = 500f, step = 1)
    var lookAtBlockTime = 500

    @Slider(name = "Failsafe Volume", min = 0f, max = 100f, step = 5)
    var failsafeVolume = 100f.toInt()

    init {
        initialize()
        registerKeyBind(toggleMacro) { macro.toggle() }
        addDependency("abiphoneRefuel", false)
    }

}
