package de.tomjuri.armageddon.feature

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.text.NumberFormat
import java.util.*
import java.util.regex.Pattern

class RefuelWatcher {
    @SubscribeEvent
    fun onClientChatReceive(event: ClientChatReceivedEvent) {
        val split = event.message.unformattedText.split(" {3,}");
        for (section in split) {
            if (!section.contains("Drill")) continue
            var message = section.replace(Regex("(?i)§[0-9A-FK-ORZ]"), "")
            val matcher = Pattern.compile("(\\d[\\d,.]*\\d*)+([kKmMbBtT])").matcher(message)
            while (matcher.find()) {
                var parsedDouble = NumberFormat.getInstance(Locale.US).parse(matcher.group(1)).toDouble()
                val magnitude = matcher.group(2).lowercase(Locale.ROOT)
                when (magnitude) {
                    "k" -> parsedDouble *= 1_000;
                    "m" -> parsedDouble *= 1_000_000;
                    "b" -> parsedDouble *= 1_000_000_000;
                    "t" -> parsedDouble *= 1_000_000_000_000L;
                }
                message = matcher.replaceFirst(NumberFormat.getInstance(Locale.US).format(parsedDouble))
                matcher.reset(message);
            }
            val drillFuel = message.replace(Regex("[^0-9 /]"), "").trim().split("/")
            val fuel = 0.coerceAtLeast(Integer.parseInt(drillFuel[0]))
            val maxFuel = 1.coerceAtLeast(Integer.parseInt(drillFuel[1]))
            if(fuel > 100) continue

        }
    }
}