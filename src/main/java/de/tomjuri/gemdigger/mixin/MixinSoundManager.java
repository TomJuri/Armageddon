package de.tomjuri.gemdigger.mixin;

import de.tomjuri.gemdigger.GemDigger;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.audio.SoundPoolEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundManager.class)
public class MixinSoundManager {
    @Inject(method = "getNormalizedVolume", at = @At("RETURN"), cancellable = true)
    private void getNormalizedVolume(ISound sound, SoundPoolEntry soundPoolEntry, SoundCategory category, CallbackInfoReturnable<Float> ci) {
        if(GemDigger.failsafe.getPingAlertPlaying() && category == SoundCategory.PLAYERS) return;
        if(GemDigger.macro.isRunning() && GemDigger.config.getMuteGameSounds()) {
            ci.setReturnValue(0.0f);
        }
    }
}