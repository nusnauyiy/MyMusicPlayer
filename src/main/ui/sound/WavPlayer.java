package ui.sound;

import model.Song;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.util.concurrent.CountDownLatch;

//the class that handles all the audio playing
//still under construction for pause function and other play modes
public class WavPlayer {

    static AudioInputStream audioInputStream;
    public static final String LOOP = "LOOP";
    public static Clip clip;

    //MODIFIES: this
    //EFFECT: play the audio clip, cannot do anything else whie playing loop continuesly when using the LOOP mode
    //REFERENCE:
    //https://stackoverflow.com/questions/22035768/javax-sound-sampled-clip-terminating-before-playing-sound
    public static boolean play(Song s, String mode) {
        final CountDownLatch clipDone = new CountDownLatch(1);
        try {
            audioInputStream = AudioSystem.getAudioInputStream(s.getFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    event.getLine().close();
                    clipDone.countDown();
                }
            });
            clip.start();
            if (mode.equals(LOOP)) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            clipDone.await();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //MODIFIES: this
    //EFFECT: play the audio clip, can play overlappingly
    //REFERENCE:
    //https://stackoverflow.com/questions/22035768/javax-sound-sampled-clip-terminating-before-playing-sound
    public static boolean playOverlap(Song s, String mode) {

        try {
            audioInputStream = AudioSystem.getAudioInputStream(s.getFile());
            if (clip != null) {
                clip.stop();
            }
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    event.getLine().close();

                }
            });
            clip.start();
            if (mode.equals(LOOP)) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
