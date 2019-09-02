package JustDarts.components;

import JustDarts.managers.TimeManager;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class PowerBar extends FlowBar {
    public PowerBar() {
        imgBaseDim = new Dimension(60, 275); // set dims using modifiers
        frameIndexSequence.addAll(Arrays.asList(0));
        prepareFramesList(new String[]{
                "meter_bar_wood.png"
        });
        init();
    }

    @Override
    public void draw(JPanel p, Graphics g) {
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

        frameDelay += TimeManager.getInstance().getDeltaTime();
        if (frameDelay < 20f) return;
        frameDelay -= 20f;

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
