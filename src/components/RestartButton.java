package components;

import java.awt.*;
import java.util.Arrays;

public class RestartButton extends Button {
    public RestartButton() {
        imgBaseDim = new Dimension(getButtonWidth(), getButtonHeight());
        frameIndexSequence.addAll(Arrays.asList(0,1));
        prepareFramesList(new String[]{
                "restart.png", "restart2.png"
        });
        init();
    }
}
