package JustDarts.components;

import JustDarts.interfaces.Drawable;

import java.awt.*;
import java.util.Arrays;

public class Wall extends Drawable {
    public Wall() {
        imgBaseDim = new Dimension(WINDOW_SIZE.width / SCALE,WINDOW_SIZE.height / SCALE); // set dims using modifiers
        frameIndexSequence.addAll(Arrays.asList(0));
        prepareFramesList(new String[]{
                "bg.jpg"
        });
        init();
    }

    @Override
    protected void init() {

    }

    @Override
    public void update() {

    }
}
