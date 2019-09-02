package JustDarts.components;

import java.awt.*;
import java.util.Arrays;

public class QuitButton extends Button {
    public QuitButton() {
        imgBaseDim = new Dimension(getButtonWidth(), getButtonHeight());
        frameIndexSequence.addAll(Arrays.asList(0,1));
        prepareFramesList(new String[]{
                "quit.png", "quit2.png"
        });
        init();
    }

//    @Override
//    protected void init() {
//        super.init();
//    }


//    public void update(){}
}
