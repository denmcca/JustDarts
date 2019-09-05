package components;

import interfaces.Drawable;

import javax.swing.*;
import java.awt.*;

public class ScoreBoard extends Drawable {
    private int score;
    private int highScore;
    private Point scorePosition;
    private Point highScorePosition;
    private boolean shouldTransition;
    private int color = 255;

    public ScoreBoard() {
        highScore = 0;
        init();
    }

    public void reset() {
        init();
    }

    @Override
    protected void init() {
        score = 0;
        scorePosition = new Point(WINDOW_SIZE.width * 7 / 10, WINDOW_SIZE.height / 15);
        highScorePosition = new Point(WINDOW_SIZE.width/ 10, scorePosition.y);
        color = 255;
        shouldTransition = false;
//        pointsList = new ArrayList<>();
    }

    @Override
    public void update() {
        if (shouldTransition) {
            // stop transition if 0
            if (--color < 1)
                shouldTransition = false;
        }
    }

    @Override
    public void draw(JPanel p, Graphics2D g) {
        g.setColor(new Color(color, color, color, 255));
        g.setFont(new Font("Serif", Font.BOLD+Font.ITALIC, 30 * SCALE));
        g.drawString("Score: " + score, scorePosition.x, scorePosition.y);
        g.drawString("High Score: " + highScore, highScorePosition.x, highScorePosition.y);
    }

    public void updateScore(int score) {
        this.score += score;
        if (this.score > highScore)
            highScore = this.score;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setShouldTransition(boolean shouldTransition) {
        this.shouldTransition = shouldTransition;
    }
}
