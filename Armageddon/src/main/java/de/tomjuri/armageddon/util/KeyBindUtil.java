package de.tomjuri.armageddon.util;

import de.tomjuri.armageddon.Armageddon;
import de.tomjuri.armageddon.mixin.MinecraftInvoker;
import net.minecraft.client.settings.KeyBinding;

public class KeyBindUtil {
    public static void leftClick() { ((MinecraftInvoker) Ref.mc()).invokeClickMouse(); }
    public static void rightClick() { ((MinecraftInvoker) Ref.mc()).invokeRightClickMouse(); }
    public static void setKey(int key, boolean pressed) { KeyBinding.setKeyBindState(key, pressed); }
}
