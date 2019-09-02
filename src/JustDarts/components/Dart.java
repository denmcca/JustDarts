package JustDarts.components;

import JustDarts.interfaces.Drawable;
import JustDarts.managers.GameObjectManager;

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
    public void draw(JPanel p, Graphics g) {
        //super.draw(p, g);
        frames.get(frameIndexSequence.get(currentFrame)).paintIcon(p, g, getPosX() - getWidth() / 2, getPosY() - getHeight() / 2);

    }

    @Override
    public void update() {

    }

    @Override
    public void reset() {
        shouldRemove = true;
    }

//    @Override
//    public int getPosX() {
//        return super.getPosX() - getWidth() / 2;
//    }
//
//    @Override
//    public int getPosY() {
//        return super.getPosY() - getHeight() / 2;
//    }

}
