package utility;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Music {
    static Clip clip;

    public static void playSound(String soundFile) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        File f = new File(soundFile);
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
        clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.start();
    }

    public static void stopSound(){
        clip.stop();
        clip.close();
    }

}