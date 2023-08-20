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
        player.addChatMessage(ChatComponentText("§eArmageddon §8» §$message"))
    }

    fun sendWebhook(title: String, ping: Boolean, embedMessage: String, color: String) {
        Thread {
            val s = "{\n" +
                    "  \"content\": %PING%,\n" +
                    "  \"embeds\": [\n" +
                    "    {\n" +
                    "      \"title\": \"%TITLE%\",\n" +
                    "      \"description\": \"%EMBED_MESSAGE%\",\n" +
                    "      \"color\": %COLOR%,\n" +
                    "      \"thumbnail\": {\n" +
                    "        \"url\": \"%PLAYER_HEAD%\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"username\": \"Armageddon\",\n" +
                    "  \"avatar_url\": \"https://imagedelivery.net/95QNzrEeP7RU5l5WdbyrKw/d8e0f5d3-5153-4969-7be6-64e441798b00/shopitem\",\n" +
                    "  \"attachments\": []\n" +
                    "}"
            val content = s
                .replace("%PING%", if (ping) "\"@everyone\"" else "null")
                .replace("%TITLE%", title)
                .replace("%EMBED_MESSAGE%", embedMessage)
                .replace("%COLOR%", color)
                .replace("%PLAYER_HEAD%", "https://mc-heads.net/head/${mc.session.playerID}.png")
            println(s)
            println(WebhookUtil.send(config.webhookUrl, content))
        }.start()
    }
}
