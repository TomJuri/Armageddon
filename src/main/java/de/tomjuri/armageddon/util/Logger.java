package de.tomjuri.armageddon.util;

import net.minecraft.util.ChatComponentText;

public class Logger {
    public static void info(Object message) { send("a" + message); }
    public static void error(Object message) { send("c" + message); }
    private static void send(String message) { Ref.player().addChatMessage(new ChatComponentText("§eArmageddon §8» §" + message)); }
}
