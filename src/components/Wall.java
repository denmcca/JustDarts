package components;

import interfaces.Drawable;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;

public class Wall extends Drawable {
    public Wall() {
        imgBaseDim = new Dimension(1280,960); // set dims using modifiers
        frameIndexSequence.addAll(Collections.singletonList(0));
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
