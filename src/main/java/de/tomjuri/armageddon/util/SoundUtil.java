package de.tomjuri.armageddon.util;

import de.tomjuri.armageddon.config.ArmageddonConfig;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundUtil {
    private static Clip clip;

    public static synchronized void playSound(final String url) {
        new Thread(() -> {
            try {
                clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(SoundUtil.class.getResource("/assets/" + url));
                clip.open(inputStream);
                FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float volumePercentage = ArmageddonConfig.failsafeVolume / 100f;
                float dB = (float) (Math.log(volumePercentage) / Math.log(10.0) * 20.0);
                volumeControl.setValue(dB);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}