package JustDarts.components;

import JustDarts.managers.TimeManager;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class AimBar extends FlowBar {
    public AimBar() {
        imgBaseDim = new Dimension(350, 40); // set dims using modifiers
        frameIndexSequence.addAll(Arrays.asList(0));
        prepareFramesList(new String[]{
                "aimBarWood.png"
        });
        init();
    }

    @Override
    public void draw(JPanel p, Graphics g) {
        if (!shouldDraw) return;
        super.draw(p, g);
        g.setColor(new Color((int) (0 + Math.abs((double) barFlow) * 1.5),
                (int) (255 - Math.abs((double) barFlow) * 1.5), 0, 200).brighter().brighter());
        g.fillRect(((WINDOW_SIZE.width / 2) + 3) - ((Math.abs(barFlow * SCALE) - barFlow * SCALE) / 2),
                (WINDOW_SIZE.height / 8) - 8 * SCALE, Math.abs(barFlow * SCALE), 18 * SCALE);
    }
    @Override
    protected void init() {

    }

    @Override
    public void update() {
        super.update();
        flow();
    }

    @Override
    protected void flow() {
        super.flow();
        if (isLocked) {
            return;
        }

        frameDelay += TimeManager.getInstance().getDeltaTime();
        if (frameDelay < 20f) return;
        frameDelay -= 20f;

        if(!isFlowReversed) {
            barFlow += factor++;

            if(barFlow > 150) {
                isFlowReversed = true;
                factor = 5;
                barFlow = 150;
            }
        }
        else {
            barFlow += factor--;

            if(barFlow < -150) {
                isFlowReversed = false;
                factor = -5;
                barFlow = -150;
            }
        }
    }

    // returns aim deviation along the x-axis
    public int getBarValue() {
        return barFlow * SCALE / 5;
    }
}
