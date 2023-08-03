package de.tomjuri.armageddon.feature;

import de.tomjuri.armageddon.Armageddon;
import de.tomjuri.armageddon.config.ArmageddonConfig;
import de.tomjuri.armageddon.macro.Macro;
import de.tomjuri.armageddon.util.Logger;
import de.tomjuri.armageddon.util.Ref;
import de.tomjuri.armageddon.util.Timer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AbiphoneRefuel {

    private static boolean shouldRefuel = false;
    private final Timer timer = new Timer();

    @SubscribeEvent
    public void onClientChatReceive(ClientChatReceivedEvent event) {
        if (!Macro.isEnabled() || event.type != 2) return;
        String[] split = event.message.getUnformattedText().split(" {3,}");
        for(String section : split) {
            if(!section.contains("Drill")) continue;
            String message = section.replaceAll("(?i)§[0-9A-FK-ORZ]", "");
            Matcher matcher = Pattern.compile("(\\d[\\d,.]*\\d*)+([kKmMbBtT])").matcher(message);
            while (matcher.find()) {
                try {
                    double parsedDouble = NumberFormat.getInstance(Locale.US).parse(matcher.group(1)).doubleValue();
                    String magnitude = matcher.group(2).toLowerCase(Locale.ROOT);
                    switch (magnitude) {
                        case "k":
                            parsedDouble *= 1_000;
                            break;
                        case "m":
                            parsedDouble *= 1_000_000;
                            break;
                        case "b":
                            parsedDouble *= 1_000_000_000;
                            break;
                        case "t":
                            parsedDouble *= 1_000_000_000_000L;
                    }
                    message = matcher.replaceFirst(NumberFormat.getInstance(Locale.US).format(parsedDouble));
                    matcher.reset(message);
                } catch (Exception ignored) { }
            }
            String[] drillFuel = message.replaceAll("[^0-9 /]", "").trim().split("/");
            int fuel = Math.max(0, Integer.parseInt(drillFuel[0]));
            if(fuel > 100 || Macro.getState() != Macro.State.SWITCH_TO_ROD) {
                return;
            }
            Macro.stop();
            shouldRefuel = true;
        }
    }

    @SubscribeEvent
    public void doFunnyStuff(TickEvent.ClientTickEvent event) {
        if(!shouldRefuel) return;
        shouldRefuel = false;

    }

    enum State {
        WAITING_FOR_DRILL,
        WAITING_FOR_FUEL,
        REFUELING,
        REFUELED
    }

}