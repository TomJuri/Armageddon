package de.tomjuri.gemdigger.util

import de.tomjuri.gemdigger.mixin.MinecraftInvoker
import net.minecraft.client.Minecraft
import net.minecraft.client.settings.KeyBinding

object KeyBindUtil {
    fun rightClick() { (mc as MinecraftInvoker).invokeRightClickMouse() }
    fun setKey(key: Int, heldDown: Boolean) { KeyBinding.setKeyBindState(key, heldDown) }
}