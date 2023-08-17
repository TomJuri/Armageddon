package de.tomjuri.armageddon.util

import de.tomjuri.armageddon.mixin.MinecraftInvoker
import net.minecraft.client.settings.KeyBinding

object KeyBindUtil {
    fun leftClick() = (mc as MinecraftInvoker).invokeClickMouse()
    fun middleClick() = (mc as MinecraftInvoker).invokeMiddleClickMouse()
    fun rightClick() = (mc as MinecraftInvoker).invokeRightClickMouse()
    fun jump() = Thread {
        setPressed(gameSettings.keyBindJump, true);
        Thread.sleep(100);
        setPressed(gameSettings.keyBindJump, false)
    }.start()
    fun setPressed(key: KeyBinding, pressed: Boolean) = KeyBinding.setKeyBindState(key.keyCode, pressed)
}