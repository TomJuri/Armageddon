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
    init {
        initialize()
        registerKeyBind(toggleMacro) { Armageddon.instance.macro.toggle() }
    }

    companion object {
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

        @Slider(name = "Abiphone slot", min = 1f, max = 8f, step = 1)
        var abiphoneSlot = 4

        @JvmField
        @Switch(name = "Show waypoints")
        var showWaypoints = true

        @JvmField
        @Switch(name = "Mute game sounds")
        var muteGameSounds = false

        @Switch(name = "Abiphone refuel")
        var abiphoneRefuel = true

        @Slider(name = "Swipe range", min = 180f, max = 360f, step = 1)
        var swipeRange = 320f

        @Slider(name = "Swipe time", min = 300f, max = 1000f, step = 1)
        var swipeTime: Long = 500

        @Slider(name = "Look at block time", min = 150f, max = 500f, step = 1)
        var lookAtBlockTime: Long = 500

        @Slider(name = "Delay after mounting dillo", min = 100f, max = 500f, step = 50)
        var delayAfterMountingDillo = 200

        @Slider(name = "Delay after teleporting", min = 100f, max = 900f, step = 50)
        var delayAfterTeleporting = 250

        @Slider(name = "Rotation check threshold", min = 0f, max = 8f)
        var rotationCheckThreshold = 2f

        @Slider(name = "Failsafe Volume", min = 0f, max = 100f, step = 5)
        var failsafeVolume = 100f.toInt()
    }
}
