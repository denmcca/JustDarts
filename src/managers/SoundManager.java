package managers;

import components.Sound;
import components.SoundEffect;
import components.SoundPlayer;
import utils.Logger;

import javax.sound.sampled.*;
import java.util.HashMap;

public class SoundManager {
    private static SoundManager instance;
    private HashMap<String, Sound> soundMap;
    public static final String PATH_TO_DIR = "resources/audio/";
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

    public void removeSound(String key) {
        soundMap.remove(key);
    }

    private boolean playSound(Clip clip) {
//        clip.flush();
        Thread soundPlayer = new Thread(new SoundPlayer(clip));
        soundPlayer.start();
        return true;
    }

    public void setSoundToPlay(String path) {
        try {
            soundMap.get(PATH_TO_DIR + path).setShouldPlay(true);
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
