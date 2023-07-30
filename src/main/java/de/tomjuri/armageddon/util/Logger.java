package de.tomjuri.armageddon.util;

import de.tomjuri.armageddon.Armageddon;
import net.minecraft.util.ChatComponentText;

public class Logger {
    public static void info(Object message) { send("a" + message); }
    public static void error(Object message) { send("c" + message); }
    private static void send(String message) { Armageddon.INSTANCE.getPlayer().addChatMessage(new ChatComponentText("§eArmageddon §8» §" + message)); }
}
