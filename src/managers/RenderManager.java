package managers;

import interfaces.Drawable;
import javax.swing.*;
import java.awt.*;

public class RenderManager {
    private static RenderManager instance;
    public static String PATH_TO_DIR = "resources/images/";
    private Graphics graphics;
    private JPanel panel;
    private RenderManager() {
        init();
    }
    public static RenderManager getInstance() {
        return instance == null ? instance = new RenderManager() : instance;
    }
    private void init() {
        // default values
    }

    public void setGraphics(Graphics g) {
        graphics = g;
    }

    public void setPanel(JPanel p) {
        panel = p;
    }

    public void draw() {
        if (graphics == null || panel == null)
            System.out.println("Error: Graphics or Panel not set");

        GameObjectManager.getInstance().getGameObjects().forEach((k, go) -> {
            if (go instanceof Drawable) {
                ((Drawable) go).draw(panel, (Graphics2D)graphics);
            }
        });
    }
}
