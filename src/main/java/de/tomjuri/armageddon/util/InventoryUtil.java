package de.tomjuri.armageddon.util;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;

public class InventoryUtil {

    public static int getSlotForItem(String itemName) {
        for (Slot slot : Ref.mc().thePlayer.openContainer.inventorySlots) {
            if (slot.getHasStack()) {
                ItemStack stack = slot.getStack();
                if (StringUtils.stripControlCodes(stack.getDisplayName()).contains(itemName)) {
                    return slot.slotNumber;
                }
            }
        }
        return -1;
    }

    public static void click(int slot, int mouseButton, int mode) {
        Ref.mc().playerController.windowClick(Ref.player().openContainer.windowId, slot, mouseButton, mode, Ref.mc().thePlayer);
    }
}
