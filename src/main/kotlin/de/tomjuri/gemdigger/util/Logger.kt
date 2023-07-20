package de.tomjuri.gemdigger.util

import net.minecraft.util.ChatComponentText

object Logger {
    fun info(message: Any) {
        player.addChatMessage(ChatComponentText("a${message.toString()}"))
    }
    fun error(message: Any) {
        player.addChatMessage(ChatComponentText("c$message"))
    }
    private fun send(message: String) {
        player.addChatMessage(ChatComponentText("§cGem§eDigger §8» §"))
    }
}