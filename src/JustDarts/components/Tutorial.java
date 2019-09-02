package JustDarts.components;

import JustDarts.interfaces.Drawable;

import java.awt.*;
import java.util.Arrays;

public class Tutorial extends Drawable {
    public Tutorial() {
        imgBaseDim = new Dimension(230, 150); // set dims using modifiers
        frameIndexSequence.addAll(Arrays.asList(0,1,2));
        prepareFramesList(new String[]{
                "tutorial1.png", "tutorial2.png", "tutorial3.png"
        });
        init();
    }

    @Override
    protected void init() {
    }

    @Override
    public void update() {

    }

    @Override
    public void reset() {
    }
}
