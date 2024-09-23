// This file have functionality for integreating sound 2048 game

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer {
    public static void playSound(String fileName) {
        // This try-catch block handle following exceptions :
        // - File format is not correct or unsupported Audio File 
        // - File is not found (IO Exception )
        try {
            URL soundURL = SoundPlayer.class.getResource(fileName);
            if (soundURL == null) {
                System.err.println(Constant.SOUND_NOT_FOUND);
                return;
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException exception) {
            exception.printStackTrace();
        }
    }
}