package managers;

import interfaces.GameObject;
import java.util.ArrayList;

public class SceneManager {
    private static SceneManager instance;
    private ArrayList<GameObject> gameObjects;

    // singleton
    private SceneManager() {
        init();
    }
    // singleton
    public SceneManager getInstance() {
        return instance == null ? instance = new SceneManager() : instance;
    }

    // sets default values
    private void init() {
        gameObjects = new ArrayList<>();
    }
}
