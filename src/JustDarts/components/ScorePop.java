package JustDarts.components;

import JustDarts.interfaces.Drawable;
import JustDarts.managers.GameObjectManager;
import sun.plugin2.message.GetAuthenticationReplyMessage;

import javax.swing.*;
import java.awt.*;

public class ScorePop extends Drawable {
    private Point pingPos = null;
    private int pingDiameter = 0;
    private final int CIRCLE_DIAMETER_MAX = 20 * SCALE;
    private Point scorePopPos = null;
    private int scoreMultiplier;
    private int turnScore;
    private int[] color = new int[]{0,0,0,0};
    private Dart dart = new Dart();

    public ScorePop(Point pos, int scoreMultiplier, int turnScore, int[] color) {
        this.pingPos = new Point(pos);
        this.scorePopPos = new Point(this.pingPos);
        this.scoreMultiplier = scoreMultiplier;
        this.turnScore = turnScore;
        for(int i = 0; i < color.length; i++)
            this.color[i] = color[i];
    }

    @Override
    public void draw(JPanel p, Graphics g) {
        /// TODO create an array for scorePops, and whatever else necessary
        g.setColor(new Color(color[0], color[1], color[2], color[3]));
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16 * SCALE));

        // if score multiplier hit, draw score multiplier with "x" -> score x multiplier
        if(scoreMultiplier > 1 && scorePopPos.y > 0)
            g.drawString(turnScore + " x " + scoreMultiplier, scorePopPos.x, --scorePopPos.y);
        else
            g.drawString(Integer.toString(turnScore), scorePopPos.x, --scorePopPos.y);

        // ping surrounding used darts
        g.setColor(new Color(color[0], color[1], color[2], color[3]).brighter().brighter());
        circleDiameterPing();
        g.drawOval(pingPos.x - (pingDiameter / 2), pingPos.y - (pingDiameter / 2),
                pingDiameter, pingDiameter);
    }

    @Override
    protected void init() {

    }

    @Override
    public void update() {
        circleDiameterPing();
    }

    private void circleDiameterPing()
    {
        pingDiameter = ++pingDiameter % CIRCLE_DIAMETER_MAX;
    }

    @Override
    public void reset() {
//        GameObjectManager.getInstance().remove(this);
        shouldRemove = true;
    }
}
