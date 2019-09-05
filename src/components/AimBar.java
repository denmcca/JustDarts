package components;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;

public class AimBar extends FlowBar {
    public AimBar() {
        imgBaseDim = new Dimension(350, 40); // set dims using modifiers
        frameIndexSequence.addAll(Collections.singletonList(0));
        prepareFramesList(new String[]{
                "aimBarWood.png"
        });
        init();
    }

    @Override
    public void draw(JPanel p, Graphics2D g) {
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

        // wait until next frame update
        if (!shouldAnimate) return;

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
