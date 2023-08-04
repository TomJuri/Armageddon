package de.tomjuri.armageddon.mixin;

import de.tomjuri.armageddon.event.PacketEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {
    @Inject(method = "channelRead0*", at = @At("HEAD"))
    private void read(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callback) {
        MinecraftForge.EVENT_BUS.post(new PacketEvent(packet));
    }
}