package components;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.util.Objects;

public abstract class Sound {
    private Clip clip;
    private boolean shouldPlay;
    private String path;

    public Sound(String path) {
        this.path = path;
        createAudioClip(path);
        init();
    }

    private void init() {
        shouldPlay = false;
    }

    private void createAudioClip(String path)
    {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(path)))
            ); // wrapping Resource Stream in Buffered Input Stream resolves mark/reset error!
            AudioFormat format = ais.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            clip = (Clip)AudioSystem.getLine(info);
            clip.open(ais);
        } catch(Exception ex) {
            System.out.println("Error opening Audio Input Stream");
            ex.printStackTrace();
        }
    }

    public String getPath() {
        return path;
    }

    public Clip getClip() {
        return clip;
    }

    public boolean shouldPlay() {
        return shouldPlay;
    }

    public boolean isPlaying() {
        return clip.isRunning();
    }

    public void setShouldPlay(boolean shouldPlay) {
        this.shouldPlay = shouldPlay;
    }

    public void reset() {
        init();
    }
}
