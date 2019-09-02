package JustDarts.managers;

import JustDarts.components.Sound;
import JustDarts.components.SoundEffect;
import JustDarts.components.SoundPlayer;

import javax.sound.sampled.*;
import java.util.HashMap;

public class SoundManager {
    private static SoundManager instance;
    private HashMap<String, Sound> soundMap;
    public static final String PATH_TO_DIR = "audio/";
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

    public void addSound(Sound sound) {
        soundMap.put(sound.getPath(), sound);
    }

    public void removeSound(Integer key) {
        soundMap.remove(key);
    }

    private boolean playSound(Clip clip) {
        clip.flush();
        if (clip == null) {
            System.out.println("Error: No such audio clip found in the Sound Manager");
            return false;
        }
        Thread soundPlayer = new Thread(new SoundPlayer(clip));
        soundPlayer.start();
        return true;
    }

    public void setSoundToPlay(String path) {
        try {
            soundMap.get(path).setShouldPlay(true);
        }
        catch (Exception e) {
            addSound(new SoundEffect(PATH_TO_DIR + path));
            soundMap.get(PATH_TO_DIR + path).setShouldPlay(true);
        }
    }

    public void playSounds() {
        soundMap.values().forEach(sound -> {
            if (sound.shouldPlay()) {
                playSound(sound.getClip());
                sound.setShouldPlay(false);
            }
        });
    }
}
