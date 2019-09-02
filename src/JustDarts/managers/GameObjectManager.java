package JustDarts.managers;

import JustDarts.interfaces.GameObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;

public class GameObjectManager {
    private static GameObjectManager instance;
//    private Vector<GameObject> gameObjects;
    private LinkedHashMap<String, GameObject> gameObjectMap;

    private GameObjectManager() {
        init();
    }
    public static GameObjectManager getInstance() {
        return instance == null ? instance = new GameObjectManager() : instance;
    }
    private void init() {
        gameObjectMap = new LinkedHashMap<>();
    }

    public void add(GameObject go) {
        System.out.printf("adding %s\n", go.getName());
        gameObjectMap.put(go.getName(), go);
    }

    public void addAll(GameObject[] goList) {
        for (GameObject go : goList) {
            if (gameObjectMap.put(go.getName(), go) != null)
                System.out.println("Previously mapped GameObject replaced in GameObject Map");
        }
    }

    public GameObject remove(GameObject go) {
        return gameObjectMap.remove(go.getName());
    }

    public void collectGarbage() {
        Vector<String> collectionList = new Vector<>();
        gameObjectMap.forEach((k, go) -> {
            if (go.shouldRemove()) {
                System.out.printf("Removing %s\n", go.getName());
                collectionList.addElement(k);
            }
        });
        collectionList.forEach(k -> {
            gameObjectMap.remove(k);
        });
    }

    public GameObject getGameObjectByName(String name) {
        GameObject go = gameObjectMap.get(name);
        if (go == null)
            System.out.println("Error: No Game Object found with that name.");
        return go;
    }

    public void update() {
        gameObjectMap.forEach((k, go) -> {
            go.update();
        });
    }

    public HashMap<String, GameObject> getGameObjects() {
        return gameObjectMap;
    }
}
