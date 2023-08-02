package de.tomjuri.armageddon.feature;

import de.tomjuri.armageddon.Armageddon;
import de.tomjuri.armageddon.config.ArmageddonConfig;
import de.tomjuri.armageddon.macro.Macro;
import de.tomjuri.armageddon.util.Ref;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AbiphoneRefuel {
    @SubscribeEvent
    public void onTick(ClientChatReceivedEvent event) {
        if (!Macro.isEnabled()) return;
  /*      String stripColoring = TextUtils.stripColor(section);
        int fuel = Math.max(0, Integer.parseInt(splitStats[0]));
        int maxFuel = Math.max(1, Integer.parseInt(splitStats[1]));*/
    }

}