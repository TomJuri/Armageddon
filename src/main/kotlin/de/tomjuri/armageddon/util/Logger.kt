package de.tomjuri.armageddon.util

import net.minecraft.util.ChatComponentText
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

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

    fun sendStatusWebhook() {
        val url = config.webhookUrl
        if (url.isBlank()) return
        val s = "{\n" +
                "  \"content\": null,\n" +
                "  \"embeds\": [\n" +
                "    {\n" +
                "      \"title\": \"Status: %PLAYER_NAME%\",\n" +
                "      \"description\": \":hourglass: Running for: %RUNTIME%\\n:money_with_wings: Money total: %TOTAL_MONEY%\\n:chart_with_upwards_trend: Money per hour: %MONEY_HOUR%\\n:man: Players near you: %PLAYER_NEAR%\\n\\n:camera_with_flash: Screenshot:\",\n" +
                "      \"color\": null,\n" +
                "      \"footer\": {\n" +
                "        \"text\": \"Last updated\"\n" +
                "      },\n" +
                "      \"timestamp\": \"%TIMESTAMP%\",\n" +
                "      \"image\": {\n" +
                "        \"url\": \"%SCREENSHOT_URL%\"\n" +
                "      },\n" +
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
            .replace("%PLAYER_NAME%", mc.session.username)
            .replace("%RUNTIME%", tracker.getRuntime())
            .replace("%TOTAL_MONEY%", tracker.getTotalProfit())
            .replace("%MONEY_HOUR%", tracker.getProfitPerHour())
            .replace("%PLAYER_NEAR%", tracker.getPlayersNearby())
            .replace("%TIMESTAMP%", OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
            .replace(
                "%SCREENSHOT_URL%",
                "https://images.unsplash.com/photo-1553949345-eb786bb3f7ba?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80"
            )
            .replace("%PLAYER_HEAD%", "https://mc-heads.net/head/${mc.session.playerID}.png")
           WebhookUtil.send(url, content)
    }
}
