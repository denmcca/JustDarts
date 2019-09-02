package JustDarts.managers;

import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;

public class SoundManager {
    private static SoundManager instance;
    private HashMap<Integer, Clip> soundMap;
    private SoundManager() {
        init();
    }
    public static SoundManager getInstance() {
        if (instance == null)
            instance = new SoundManager();
        return instance;
    }
    private void init() {
        // default values
        soundMap = new HashMap<>();
    }

    public void addSound(Integer key, Clip clip) {
        soundMap.put(key, clip);
    }

    public void removeSound(Integer key) {
        soundMap.remove(key);
    }

    public boolean playSound(Integer key) {
        Clip clip = soundMap.get(key);
        clip.flush();
        if (clip == null) {
            System.out.println("Error: No such audio clip found in the Sound Manager");
            return false;
        }
        Thread soundPlayer = new Thread(new SoundPlayer(clip));
        soundPlayer.start();
        return true;
    }

    public Clip createAudioClip(String path)
    {
        String audioDir = "audio/";
        Clip clip = null;
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    new File(audioDir + path).getAbsoluteFile());
            AudioFormat format = audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            clip = (Clip)AudioSystem.getLine(info);
            clip.open(audioInputStream);
        } catch(Exception ex) {
            System.out.println("Error opening Audio Input Stream");
            ex.printStackTrace();
        }
        return clip;
    }

    // used to play each sound in a separate thread
    private class SoundPlayer implements Runnable {
        Clip clip;
        public SoundPlayer(Clip clip) {
            this.clip = clip;
            this.clip.start();
        }

        public void run() {
            try {
                Thread.sleep(clip.getMicrosecondLength() / 1000);
                clip.stop();
                clip.setFramePosition(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
