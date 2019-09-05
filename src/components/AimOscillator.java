package components;

import interfaces.Animated;

import javax.swing.*;
import java.awt.*;

public class AimOscillator extends Animated {
    private Hand player;
    private boolean isDirectionReversed;
    private int aimCirOffsetX;
    private int aimCirOffsetY;
    private int aimCirRateY;
    private int circleAimMax;

    public AimOscillator(Hand player) {
        this.player = player;
        aimCirRateY = 3 * SCALE;
        circleAimMax = 75 * SCALE;
        setFrameRate(60);
        init();
    }

    @Override
    protected void init() {
        aimCirOffsetY = 1;
        aimCirOffsetX = 0;
    }

    public void update() {
        if (player.getAnimationSequence() == Hand.AnimationSequence.IDLE) {
            super.update();
            if (shouldAnimate) {
                oscillateAimCircle();
            }
        }
    }

    @Override
    public void draw(JPanel p, Graphics2D g) {
        // rendering aim circle
        g.setColor(Color.white);
        g.drawOval(
                player.getPosX()
                        + aimCirOffsetX - 10 / 2 * SCALE, // TODO: Fix this
                player.getPosY()
                        + aimCirOffsetY - 10 / 2 * SCALE,  // TODO: Fix this
                20 * SCALE, 20 * SCALE
        );
    }

    private void oscillateAimCircle() {
        if (!isDirectionReversed) {
            aimCirOffsetY -= aimCirRateY;
            if (aimCirOffsetY < -circleAimMax) {
                isDirectionReversed = true;
                aimCirOffsetY = -circleAimMax;
            }
        } else {
            aimCirOffsetY += aimCirRateY;
            if (aimCirOffsetY > circleAimMax) {
                isDirectionReversed = false;
                aimCirOffsetY = circleAimMax;
            }
        }
        aimCirOffsetX = -75 * SCALE;
    }

    public Point getAimCirOffsets() {
        return new Point(aimCirOffsetX, aimCirOffsetY);
    }
}