package project3.gameMech;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundPlayer {
    private static Clip backgroundClip;
    private static List<Clip> soundClips = new ArrayList<>();
    private static int currentVolume = 50; 

    public static void playsound(String file) {
        try {

            File soundFile = new File(file);
            AudioInputStream audio = AudioSystem.getAudioInputStream(soundFile);
            Clip soundClip = AudioSystem.getClip();
            soundClip.open(audio);
            soundClips.add(soundClip);
            
            //current to new sound volume 
            if (soundClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl volumeControl = (FloatControl) soundClip.getControl(FloatControl.Type.MASTER_GAIN);
                volumeControl.setValue(calVolume(currentVolume));
            }
            
            soundClip.start();  
        } catch (Exception e) {
        }
    }

    public static void playBackgroundSound(String file) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(file));
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audio);
            
            // current volume to background music
            if (backgroundClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl volumeControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
                volumeControl.setValue(calVolume(currentVolume));
            }
            
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY); 
            backgroundClip.start();
        } catch (Exception e) {
        }
    }
    //seperate 2 stop 
    public static void stopEffectsound() {
        for (Clip clip : soundClips) {
            if (clip != null) {
                if (clip.isRunning()) {
                    clip.stop();
                }
                clip.flush();
                clip.close();
            }
        }
        soundClips.clear();
    }

    public static void stopBackgroundsound() {
        if (backgroundClip != null) {
            if (backgroundClip.isRunning()) {
                backgroundClip.stop();
            }
            backgroundClip.flush();
            backgroundClip.close();
            backgroundClip = null;
        }
    }

    public static void stop() {
        stopEffectsound();
        stopBackgroundsound();
    }

    public static void setVolume(int volume) {
        currentVolume = volume;
        float gain = calVolume(volume);
        cleanup(); //clear finished clips 
        try {
            // sound effect 
            for (Clip clip : soundClips) {
                if (clip != null && clip.isOpen() && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    volumeControl.setValue(gain);
                }
            }
            
            // background sound
            if (backgroundClip != null && backgroundClip.isOpen() && backgroundClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl volumeControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
                volumeControl.setValue(gain);
            }
        } catch (Exception e) {
        }
    }

    private static float calVolume(int volume) {
        if (volume == 0) {
            return -80.0f; //mute

                }return -40.0f + (46.0f * volume / 100.0f); //1-100 maps to -80.0f to 6.0f
    }
    private static void cleanup() { //clear finished clips 
        soundClips.removeIf(clip -> !clip.isRunning());
    }
    
}

