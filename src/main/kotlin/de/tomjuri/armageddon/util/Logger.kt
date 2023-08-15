package de.tomjuri.armageddon.util

import de.tomjuri.macroframework.util.player
import net.minecraft.util.ChatComponentText

object Logger {
    fun info(message: Any) {
        send("a$message")
    }

    fun error(message: Any) {
        send("c$message")
    }

    private fun send(message: String) {
        player.addChatMessage(ChatComponentText("§eArmageddon §8» §$message"))
    }
}
