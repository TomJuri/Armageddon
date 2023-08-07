package de.tomjuri.armageddon.feature;

import de.tomjuri.armageddon.Armageddon;
import de.tomjuri.armageddon.config.ArmageddonConfig;
import de.tomjuri.armageddon.macro.Macro;
import de.tomjuri.armageddon.util.*;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.Sys;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AbiphoneRefuel {

    public boolean shouldRefuel = false;
    private final Timer timer = new Timer();
    private State state = State.OPEN_BAZAAR;
    private int maxFuel = 0;
    private int buyCounter = 0;

    @SubscribeEvent
    public void onClientChatReceive(ClientChatReceivedEvent event) {
        if (!Armageddon.getInstance().getMacro().isEnabled() || event.type != 2 || shouldRefuel || !ArmageddonConfig.abiphoneRefuel) return;
        try {
            String[] split = event.message.getUnformattedText().split(" {3,}");
            for (String section : split) {
                if (!section.contains("Drill")) continue;
                String message = section.replaceAll("(?i)§[0-9A-FK-ORZ]", "");
                Matcher matcher = Pattern.compile("(\\d[\\d,.]*\\d*)+([kKmMbBtT])").matcher(message);
                while (matcher.find()) {
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
                }
                String[] drillFuel = message.replaceAll("[^0-9 /]", "").trim().split("/");
                int fuel = Math.max(0, Integer.parseInt(drillFuel[0]));
                int fuel0 = Math.max(1, Integer.parseInt(drillFuel[1]));
                if (fuel0 == 1) return;
                maxFuel = Math.max(1, Integer.parseInt(drillFuel[1]));
                Logger.info(fuel);
                if (fuel > 100 || Armageddon.getInstance().getMacro().getState() != Macro.State.SWITCH_TO_ROD) {
                    return;
                }
                Logger.info("Drill fuel: " + fuel + "/" + maxFuel + " starting refuel.");
                Armageddon.getInstance().getMacro().stop();
                shouldRefuel = true;
            }
        } catch (Throwable ignored) {
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!shouldRefuel || !timer.isDone()) return;
        switch (state) {
            case OPEN_BAZAAR:
                Ref.player().sendChatMessage("/bz volta");
                timer.start(500);
                break;
            case CLICK_VOLTA:
                if (!InventoryUtil.click(InventoryUtil.getSlotForItem("Volta"), 0, 0)) {
                    Logger.error("There was an issue finding the Volta!");
                    stop();
                    return;
                }
                timer.start(500);
                break;
            case CLICK_BUY_VOLTA:
                if (!InventoryUtil.click(InventoryUtil.getSlotForItem("Buy Instantly"), 0, 0)) {
                    Logger.error("There was an issue finding the Buy Instantly item!");
                    stop();
                    return;
                }
                timer.start(500);
                break;
            case BUY_VOLTA:
                if(maxFuel / 10_000 <= buyCounter) {
                    Ref.player().closeScreen();
                    break;
                }
                if (!InventoryUtil.click(InventoryUtil.getSlotForItem("Buy only one"), 0, 0)) {
                    Logger.error("There was an issue finding the buy item!");
                    stop();
                    return;
                }
                timer.start(500);
                buyCounter++;
                return;
            case SWITCH_TO_ABIPHONE:
                Ref.player().inventory.currentItem = ArmageddonConfig.abiphoneSlot - 1;
                timer.start(100);
                break;
            case USE_ABIPHONE:
                KeyBindUtil.rightClick();
                timer.start(400);
                break;
            case CLICK_NPC:
                if (InventoryUtil.getSlotForItem("Jotraeline") == -1 && InventoryUtil.getSlotForItem("Next Page") != -1) {
                    InventoryUtil.click(InventoryUtil.getSlotForItem("Next Page"), 0, 0);
                    timer.start(500);
                    return;
                }
                if (InventoryUtil.getSlotForItem("Jotraeline") != -1) {
                    InventoryUtil.click(InventoryUtil.getSlotForItem("Jotraeline"), 0, 0);
                    timer.start(5000);
                    break;
                }
                Logger.error("Jotraeline not found!");
                stop();
                return;
            case PUT_DRILL:
                Logger.info(InventoryUtil.getSlotForItem("Volta"));
                if(!InventoryUtil.click(80 + ArmageddonConfig.drillSlot, 0, 1)) {
                    Logger.error("Jotraeline menu did not open!");
                    stop();
                    return;
                }
                timer.start(500);
                break;
            case PUT_FUEL:
                if(!InventoryUtil.click(InventoryUtil.getSlotForItem("Volta"), 0, 1)) {
                    Logger.error("Couldn't put Volta into menu!");
                    stop();
                    return;
                }
                timer.start(500);
                break;
            case REFUEL:
                if(!InventoryUtil.click(22, 0, 0)) {
                    Logger.error("Couldn't combine Volta and drill!");
                    stop();
                    return;
                }
                timer.start(500);
                break;
            case TAKE_DRILL:
                if(!InventoryUtil.click(13, 0, 1)) {
                    Logger.error("Couldn't take drill from menu!");
                    stop();
                    return;
                }
                timer.start(500);
                break;
            case CLOSE:
                Logger.info("Successfully refueled drill!");
                stop();
                break;
        }
        state = State.values()[(state.ordinal() + 1)];
    }

    private void stop() {
        if(Ref.mc().currentScreen != null)
            Ref.player().closeScreen();
        state = State.OPEN_BAZAAR;
        shouldRefuel = false;
        Armageddon.getInstance().getMacro().start();
    }

    enum State {
        OPEN_BAZAAR,
        CLICK_VOLTA,
        CLICK_BUY_VOLTA,
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