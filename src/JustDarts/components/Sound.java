package JustDarts.components;

import javax.sound.sampled.*;
import java.io.File;

public abstract class Sound {
    protected Clip clip;
    protected boolean shouldPlay;
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
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
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

    public void setShouldPlay(boolean shouldPlay) {
        this.shouldPlay = shouldPlay;
    }

    public void reset() {
        init();
    }
}
