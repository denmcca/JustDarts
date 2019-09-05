package components;

import javax.sound.sampled.Clip;

// used to play each sound in a separate thread
 public class SoundPlayer implements Runnable {
    private Clip clip;
    public SoundPlayer(Clip clip) {
        this.clip = clip;
    }

    public void run() {
        clip.setFramePosition(0);
        this.clip.start();
    }
}

