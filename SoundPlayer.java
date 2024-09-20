// Sound Player for 2048 Game

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundPlayer {
    public static void playSound(String fileName) {
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
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}