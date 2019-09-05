package components;

import interfaces.Animated;
import managers.TimeManager;

import javax.swing.*;
import java.awt.*;

public class ScorePop extends Animated {
    private Point pingPos;
    private int pingDiameter = 0;
    private final int CIRCLE_DIAMETER_MAX = 20 * SCALE;
    private Point scorePopPos;
    private int scoreMultiplier;
    private int turnScore;
    private int[] color = new int[]{0,0,0,0};

    public ScorePop(Point pos, int scoreMultiplier, int turnScore, int[] color) {
        this.pingPos = new Point(pos);
        this.scorePopPos = new Point(this.pingPos);
        this.scoreMultiplier = scoreMultiplier;
        this.turnScore = turnScore;
        setFrameRate(60);
        System.arraycopy(color, 0, this.color, 0, color.length);
    }

    @Override
    public void draw(JPanel p, Graphics2D g) {
        /// TODO create an array for scorePops, and whatever else necessary
        g.setColor(new Color(color[0], color[1], color[2], color[3]));
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16 * SCALE));

        // if score multiplier hit, draw score multiplier with "x" -> score x multiplier
        if (scorePopPos.y > 0) {
            if (scoreMultiplier > 1)
                g.drawString(turnScore + " x " + scoreMultiplier, scorePopPos.x, scorePopPos.y);
            else
                g.drawString(Integer.toString(turnScore), scorePopPos.x, scorePopPos.y);
        }
        // ping surrounding used darts
        g.setColor(new Color(color[0], color[1], color[2], color[3]).brighter().brighter());
        g.setStroke(new BasicStroke(2));
        g.drawOval(pingPos.x - (pingDiameter / 2), pingPos.y - (pingDiameter / 2),
                pingDiameter, pingDiameter);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void update() {
        super.update();
        if(shouldAnimate) {
            circleDiameterPing();
            --scorePopPos.y;
        }
    }

    private void circleDiameterPing() {
        pingDiameter = ++pingDiameter % CIRCLE_DIAMETER_MAX;
    }

    @Override
    public void reset() {
        shouldRemove = true;
    }
}
