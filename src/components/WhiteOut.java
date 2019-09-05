package components;

import interfaces.Animated;
import javax.swing.*;
import java.awt.*;

public class WhiteOut extends Animated {
    private int[] scoreColors;
    private int highScore;
    private String highestScorer;

    public WhiteOut() {
        highScore = 0;
        highestScorer = "Player";
        setFrameRate(60);
        init();
    }

    public void compareHighScore(int score) {
        if (highScore < score) {
            highScore = score;
            // player would be update here
        }
    }

    @Override
    public void draw(JPanel p, Graphics2D g) {
        if (!shouldDraw) return;
        g.setColor(new Color(255,255,255,scoreColors[3]));
        g.fillRect(0, 0, WINDOW_SIZE.width, WINDOW_SIZE.height);

        g.setColor(new Color(scoreColors[0], scoreColors[1], scoreColors[2], scoreColors[3]));
        g.setFont(new Font(Font.SERIF, Font.BOLD + Font.ITALIC, 20 * SCALE));

        g.drawString("Play Again?", (WINDOW_SIZE.width / 2) - 50 * SCALE, WINDOW_SIZE.height / 2);
        g.drawString("Top Score: " + highScore + " (" + highestScorer + ")",
                (WINDOW_SIZE.width / 12), (WINDOW_SIZE.height * 9 / 10));
    }
    @Override
    protected void init() {
        scoreColors = new int[]{255, 255, 255, 0};
    }

    @Override
    public void update() {
        super.update();

        if (!shouldDraw | !shouldAnimate) return;
        if (scoreColors[3] < 254)
        {
            scoreColors[0] -= 2;
            scoreColors[1] -= 2;
            scoreColors[2] -= 2;
            scoreColors[3] += 2; // white out

        }
        else
        {
            scoreColors[0] = 0;
            scoreColors[1] = 0;
            scoreColors[2] = 0;
            scoreColors[3] = 255; // white out
        }
    }

    public void reset() {
        init();
    }
}
