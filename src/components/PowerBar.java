package components;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;

public class PowerBar extends FlowBar {
    public PowerBar() {
        imgBaseDim = new Dimension(60, 275); // set dims using modifiers
        frameIndexSequence.addAll(Collections.singletonList(0));
        prepareFramesList(new String[]{
                "meter_bar_wood.png"
        });
        init();
    }

    @Override
    public void draw(JPanel p, Graphics2D g) {
        if (!shouldDraw) return;
        super.draw(p, g);
        g.setColor(new Color(255 - barFlow, barFlow, 0, 200).brighter().brighter());
        g.fillRect(getPosX() + getWidth() / 4,
                getPosY() + getHeight() * 13 / 14 - barFlow * SCALE,
                32 * SCALE, barFlow * SCALE); //powerBar
    }

    @Override
    protected void init() {
    }

    @Override
    public void update() {
        super.update();
        flow();
    }

    // stretches and shrinks power bar light vertically
    @Override
    protected void flow() {
        super.flow();
        if (isLocked) {
            return;
        }

        // skip until next frame
        if (!shouldAnimate) return;

        if (!isFlowReversed) {
            barFlow += factor++;// / TimeManager.getInstance().getDeltaTime();

            if(barFlow > 50) {
                isFlowReversed = true;
                factor = 12;
                barFlow = 155;
            }
        }
        else {
            barFlow += factor--;// / TimeManager.getInstance().getDeltaTime();

            if(barFlow < 3) {
                isFlowReversed = false;
                factor = 0;
                barFlow = 2;
            }
        }
    }

    // returns value last captured on power bar
    public int getBarValue() {
        return barFlow;
    }
}
