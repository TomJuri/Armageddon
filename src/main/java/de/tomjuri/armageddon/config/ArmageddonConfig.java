package de.tomjuri.armageddon.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.KeyBind;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.annotations.Text;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.libs.universal.UKeyboard;
import de.tomjuri.armageddon.Armageddon;
import de.tomjuri.armageddon.macro.Macro;

public class ArmageddonConfig extends Config {
    @KeyBind(name = "Toggle macro")
    public static OneKeyBind toggleMacro = new OneKeyBind(UKeyboard.KEY_C);
    @Text(name = "Route")
    public static String route = "";
    @Slider(name = "Drill slot", min = 1f, max = 8f, step = 1)
    public static int drillSlot = 1;
    @Slider(name = "Rod slot", min = 1f, max = 8f, step = 1)
    public static int rodSlot = 2;
    @Slider(name = "AOTV slot", min = 1f, max = 8f, step = 1)
    public static int aotvSlot = 3;
    @Switch(name = "Show waypoints")
    public static boolean showWaypoints = true;
    @Switch(name = "Mute game sounds")
    public static boolean muteGameSounds = false;
    @Slider(name = "Swipe range", min = 180f, max = 360f, step = 1)
    public static int swipeRange = 320;
    @Slider(name = "Swipe time", min = 300f, max = 1000f, step = 1)
    public static int swipeTime = 500;
    @Slider(name = "Look at block time", min = 150f, max = 500f, step = 1)
    public static int lookAtBlockTime = 500;
    @Slider(name = "Rotation check threshold", min = 0f, max = 8f)
    public static float rotationCheckThreshold = 2f;

    public ArmageddonConfig() {
        super(new Mod("armageddon", ModType.UTIL_QOL), "armageddon.json");
        initialize();
        registerKeyBind(toggleMacro, Macro::toggle);
    }
}
