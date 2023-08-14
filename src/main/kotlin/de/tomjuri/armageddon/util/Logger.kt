package de.tomjuri.armageddon.util

import net.minecraft.util.ChatComponentText

object Logger {
    fun info(message: Any) {
        send("a$message")
    }

    fun error(message: Any) {
        send("c$message")
    }

    private fun send(message: String) {
        Ref.player().addChatMessage(ChatComponentText("§eArmageddon §8» §$message"))
    }
}
