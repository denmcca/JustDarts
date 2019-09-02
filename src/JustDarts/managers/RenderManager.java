package JustDarts.managers;

import JustDarts.interfaces.Drawable;
import javax.swing.*;
import java.awt.*;

public class RenderManager {
    private static RenderManager instance;
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
//                System.out.println("Drawing " + go.getName());
                ((Drawable) go).draw(panel, graphics);
            }
        });
    }
}
