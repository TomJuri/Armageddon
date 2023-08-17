package de.tomjuri.armageddon.util

import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.util.StringUtils


object InventoryUtil {
    fun getSlotForItem(itemName: String): Int? {
        for (slot in player.openContainer.inventorySlots) {
            if (slot.hasStack) {
                val stack = slot.stack
                if (stack != null && StringUtils.stripControlCodes(stack.getDisplayName()).contains(itemName)) {
                    return slot.slotNumber
                }
            }
        }
        return null
    }

    fun getHotbarSlotForItem(itemName: String): Int? {
        for (i in 0..8) {
            val stack = mc.thePlayer.inventory.getStackInSlot(i)
            if (stack != null && StringUtils.stripControlCodes(stack.getDisplayName()).contains(itemName))
                return i
        }
        return null
    }

    fun openInventory() {
        mc.displayGuiScreen(GuiInventory(player))
    }

    fun click(slot: Int?, mouseButton: Int, mode: Int): Boolean {
        if (mc.currentScreen !is GuiChest || slot == null || player.openContainer.getSlot(slot) == null) {
            return false
        }
        mc.playerController.windowClick(player.openContainer.windowId, slot, mouseButton, mode, player)
        return true
    }

    object MouseButton {
        const val LEFT = 0
        const val RIGHT = 1
        const val MIDDLE = 2
    }

    object ClickMode {
        const val PICKUP = 0
        const val QUICK_MOVE = 1
        const val SWAP = 2
        const val CLONE = 3
        const val THROW = 4
        const val QUICK_CRAFT = 5
        const val PICKUP_ALL = 6
    }
}