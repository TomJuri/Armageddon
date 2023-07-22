package de.tomjuri.armageddon.util

import de.tomjuri.armageddon.mixin.MinecraftInvoker
import net.minecraft.client.settings.KeyBinding

object KeyBindUtil {
    fun rightClick() { (mc as MinecraftInvoker).invokeRightClickMouse() }
    fun setKey(key: Int, heldDown: Boolean) { KeyBinding.setKeyBindState(key, heldDown) }
}