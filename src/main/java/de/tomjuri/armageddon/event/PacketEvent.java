package de.tomjuri.armageddon.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Event;

@AllArgsConstructor
@Getter
public class PacketEvent extends Event {
    public final Packet<?> packet;
}
