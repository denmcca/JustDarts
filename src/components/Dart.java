package components;

import interfaces.Drawable;

import javax.swing.*;
import java.awt.*;

public class Dart extends Drawable {
    public Dart(){
        imgBaseDim = new Dimension(10, 10);
        String[] imageFilePaths = {"dart.png"};
        prepareFramesList(imageFilePaths);
        frameIndexSequence.add(0);
        init();
    }

    public void init(){
        currentFrame = 0;
    }

    // like all other game object will only be drawn when passed to the GO Manager
    @Override
    public void draw(JPanel p, Graphics2D g) {
        //super.draw(p, g);
        g.drawImage(frames.get(frameIndexSequence.get(currentFrame)),
                getPosX() - getWidth() / 2,
                getPosY() - getHeight() / 2,
                null
        );

    }

    @Override
    public void update() {

    }

    @Override
    public void reset() {
        shouldRemove = true;
    }
}
