package JustDarts.components;

import javax.sound.sampled.Clip;

// used to play each sound in a separate thread
 public class SoundPlayer implements Runnable {
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

