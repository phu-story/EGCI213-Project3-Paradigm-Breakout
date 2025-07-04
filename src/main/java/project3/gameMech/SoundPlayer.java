package project3.gameMech;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundPlayer {
private static Clip soundClip, backgroundClip;

    
    public static void playsound(String file){
        try {
            File soundFile = new File(file);
            AudioInputStream audio = AudioSystem.getAudioInputStream(soundFile);
            soundClip = AudioSystem.getClip();
            soundClip.open(audio);
            soundClip.start();  
        } catch (Exception e) {
        }
    }

    
    public static void playBackgroundSound(String file) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(file));
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audio);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY); 
            backgroundClip.start();
        } catch (Exception e) {
        }
    }

    public static void stop() {
        if (soundClip != null && soundClip.isRunning()) {
            soundClip.stop();
            soundClip.close();
        }
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
        }
    }

}
