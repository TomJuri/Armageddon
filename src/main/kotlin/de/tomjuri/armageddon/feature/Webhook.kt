package de.tomjuri.armageddon.feature

import de.tomjuri.armageddon.util.*
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class Webhook {

    private var timer = Timer(0)

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (!timer.isOver()) return
        timer = Timer(60_000)
        if (config.webhookUrl.isBlank() || !macro.isEnabled()) return
        Thread(sendStatusWebhook()).start()
    }

    private fun sendStatusWebhook() = Runnable {
        val s = "{\n" +
                "  \"content\": null,\n" +
                "  \"embeds\": [\n" +
                "    {\n" +
                "      \"title\": \"Status: %PLAYER_NAME%\",\n" +
                "      \"description\": \":hourglass: Running for: %RUNTIME%\\n:money_with_wings: Money total: %TOTAL_MONEY%\\n:chart_with_upwards_trend: Money per hour: %MONEY_HOUR%\\n:man: Players near you: %PLAYER_NEAR%\\n\\n:camera_with_flash: Screenshot:\",\n" +
                "      \"color\": 4718336,\n" +
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
        val screenshots = listOf(
            "https://images.unsplash.com/photo-1534447677768-be436bb09401?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=1080&ixid=MnwxfDB8MXxyYW5kb218MHx8bGFuZHNjYXBlfHx8fHx8MTY5MjQ3MTk5MQ&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1920",
            "https://images.unsplash.com/photo-1443397646383-16272048780e?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=1080&ixid=MnwxfDB8MXxyYW5kb218MHx8bGFuZHNjYXBlfHx8fHx8MTY5MjQ3MjAwOQ&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1920",
            "https://images.unsplash.com/photo-1519681393784-d120267933ba?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=1080&ixid=MnwxfDB8MXxyYW5kb218MHx8bGFuZHNjYXBlfHx8fHx8MTY5MjQ3MjAxNg&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1920",
            "https://images.unsplash.com/photo-1421930866250-aa0594cea05c?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=1080&ixid=MnwxfDB8MXxyYW5kb218MHx8bGFuZHNjYXBlfHx8fHx8MTY5MjQ3MjAxOA&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1920",
            "https://images.unsplash.com/photo-1413752362258-7af2a667b590?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=1080&ixid=MnwxfDB8MXxyYW5kb218MHx8bGFuZHNjYXBlfHx8fHx8MTY5MjQ3MjAyMA&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1920",
            "https://images.unsplash.com/photo-1584148721201-b6432e0d5106?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=1080&ixid=MnwxfDB8MXxyYW5kb218MHx8bGFuZHNjYXBlfHx8fHx8MTY5MjQ3MjAyMg&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1920",
            "https://images.unsplash.com/photo-1465146344425-f00d5f5c8f07?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=1080&ixid=MnwxfDB8MXxyYW5kb218MHx8bGFuZHNjYXBlfHx8fHx8MTY5MjQ3MjAyMw&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1920",
            "https://images.unsplash.com/photo-1491425432462-010715fd7ed7?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=1080&ixid=MnwxfDB8MXxyYW5kb218MHx8bGFuZHNjYXBlfHx8fHx8MTY5MjQ3MjAyNQ&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1920",
            "https://images.unsplash.com/photo-1511300636408-a63a89df3482?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=1080&ixid=MnwxfDB8MXxyYW5kb218MHx8bGFuZHNjYXBlfHx8fHx8MTY5MjQ3MjAyNw&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1920",
            "https://images.unsplash.com/photo-1465146344425-f00d5f5c8f07?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=1080&ixid=MnwxfDB8MXxyYW5kb218MHx8bGFuZHNjYXBlfHx8fHx8MTY5MjQ3MjAyOQ&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1920",
            "https://images.unsplash.com/photo-1531366936337-7c912a4589a7?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=1080&ixid=MnwxfDB8MXxyYW5kb218MHx8bGFuZHNjYXBlfHx8fHx8MTY5MjQ3MjAzMQ&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1920"
        )
        val content = s
            .replace("%PLAYER_NAME%", mc.session.username)
            .replace("%RUNTIME%", tracker.getRuntime())
            .replace("%TOTAL_MONEY%", tracker.getTotalProfit())
            .replace("%MONEY_HOUR%", tracker.getProfitPerHour())
            .replace("%PLAYER_NEAR%", tracker.nearbyPlayers.toString())
            .replace("%TIMESTAMP%", OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
            .replace("%SCREENSHOT_URL%", screenshots.random())
            .replace("%PLAYER_HEAD%", "https://mc-heads.net/head/${mc.session.playerID}.png")
        WebhookUtil.send(config.webhookUrl, content)
    }
}