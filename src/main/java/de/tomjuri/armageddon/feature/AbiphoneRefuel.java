package de.tomjuri.armageddon.feature;

import de.tomjuri.armageddon.config.ArmageddonConfig;
import de.tomjuri.armageddon.macro.Macro;
import de.tomjuri.armageddon.util.*;
import net.minecraft.inventory.ContainerChest;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AbiphoneRefuel {

    private static boolean shouldRefuel = false;
    private final Timer timer = new Timer();
    private State state = State.SWITCH_TO_ABIPHONE;

    @SubscribeEvent
    public void onClientChatReceive(ClientChatReceivedEvent event) {
        if (!Macro.isEnabled() || event.type != 2) return;
        String[] split = event.message.getUnformattedText().split(" {3,}");
        for (String section : split) {
            if (!section.contains("Drill")) continue;
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
                } catch (Exception ignored) {
                }
            }
            String[] drillFuel = message.replaceAll("[^0-9 /]", "").trim().split("/");
            int fuel = Math.max(0, Integer.parseInt(drillFuel[0]));
            if (fuel > 100 || Macro.getState() != Macro.State.SWITCH_TO_ROD) {
                return;
            }
            Macro.stop();
            shouldRefuel = true;
        }
    }

    @SubscribeEvent
    public void doFunnyStuff(TickEvent.ClientTickEvent event) {
        if (!shouldRefuel || !timer.isDone()) return;
        switch (state) {
            case SWITCH_TO_ABIPHONE:
                Ref.player().inventory.currentItem = ArmageddonConfig.abiphoneSlot - 1;
                timer.start(100);
                break;
            case USE_ABIPHONE:
                KeyBindUtil.rightClick();
                timer.start(400);
                break;
            case CLICK_NPC:
                if (!(Ref.player().openContainer instanceof ContainerChest)) {
                    Logger.error("Abiphone menu did not open correctly!");
                    state = State.SWITCH_TO_ABIPHONE;
                    shouldRefuel = false;
                    return;
                }
                int i = InventoryUtil.getSlotForItem("Jotraeline");
                timer.start(500);
                if(i == -1 && InventoryUtil.getSlotForItem("Next Page") != -1) {
                    InventoryUtil.click(InventoryUtil.getSlotForItem("Next Page"), 0, 0);
                    return;
                }
                if(i != -1) {
                    InventoryUtil.click(i, 0, 0);
                    break;
                }
                Logger.error("Jotraeline not found!");
                state = State.SWITCH_TO_ABIPHONE;
                shouldRefuel = false;
                Ref.player().closeScreen();
                break;
            case PUT_DRILL:
                if (!(Ref.player().openContainer instanceof ContainerChest)) {
                    Logger.error("Drill repair menu did not open correctly!");
                    state = State.SWITCH_TO_ABIPHONE;
                    shouldRefuel = false;
                    return;
                }
                InventoryUtil.click(53 + ArmageddonConfig.drillSlot, 0, 1);
                break;
            case PUT_FUEL:
                break;
            case REFUEL:
                break;
            case TAKE_DRILL:
                break;
            case CLOSE:
                break;
        }
        state = State.values()[(state.ordinal() + 1)];
    }

    enum State {
        OPEN_BAZAAR,
        CLICK_VOLTA,
        BUY_VOLTA,
        SWITCH_TO_ABIPHONE,
        USE_ABIPHONE,
        CLICK_NPC,
        PUT_DRILL,
        PUT_FUEL,
        REFUEL,
        TAKE_DRILL,
        CLOSE
    }

}