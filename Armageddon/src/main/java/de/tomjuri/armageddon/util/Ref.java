package de.tomjuri.armageddon.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;

public class Ref {
    public static Minecraft mc() { return Minecraft.getMinecraft(); }
    public static EntityPlayerSP player() { return mc().thePlayer; }
    public static World world() { return mc().theWorld; }
}
