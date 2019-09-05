package components;

import java.awt.*;
import java.util.Arrays;

public class TutorialButton extends Button {
    public TutorialButton() {
        imgBaseDim = new Dimension(getButtonWidth(), getButtonHeight());
        frameIndexSequence.addAll(Arrays.asList(0,1));
        prepareFramesList(new String[]{
                "help.png", "help2.png"
        });
        init();
    }

//    @Override
//    protected void init() {
//    }
//
//    @Override
//    public void update() {
//
//    }


}
