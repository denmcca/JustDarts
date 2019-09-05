package components;

import interfaces.Drawable;
import managers.SoundManager;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;

public class DartBoard extends Drawable {
    private int turnScore;
    private int scoreMultiplier;

    public DartBoard() {
        imgBaseDim = new Dimension(180,180); // set dims using modifiers
        frameIndexSequence.addAll(Collections.singletonList(0));
        prepareFramesList(new String[]{
                "BigDartboard.png"
        });
        init();
    }
    @Override
    protected void init() {
    }

    @Override
    public void update() {
    }


    public void findScore(int dartPosX, int dartPosY) {
        int radius = getWidth() / 2;
        int dartDistanceFromCenterX = dartPosX - WINDOW_SIZE.width / 2; // keep for clarity
        int dartDistanceFromCenterY = WINDOW_SIZE.height / 2 - dartPosY; // reverse y-axis values
        int distanceToLayer3InsidePerimeter = radius * 8 / 100;
        int distanceToLayer2InsidePerimeter = radius * 2 / 100;
        int distanceToOuterMostPerimeter = radius * 88 / 100;
        int distanceToLayer5InsidePerimeter = radius * 54 / 100;
        int distanceToLayer6InsidePerimeter = radius * 78 / 100;
        int distanceToLayer4InsidePerimeter = radius * 48 / 100;

        long distance = getDistanceFromBullsEye(dartDistanceFromCenterX, dartDistanceFromCenterY);

        if (distance > radius || distance < 0) { // negative: bit overflow == dart was too far
            if (distance > (radius * 3) || distance < 0) {
                SoundManager.getInstance().setSoundToPlay(Sounds.TERRIBLE.getValue());
            }
            else {
                SoundManager.getInstance().setSoundToPlay(Sounds.WOOD_PLASTIC_IMPACT.getValue());
            }
            turnScore = 0;
            scoreMultiplier = 0;
        } else {
            SoundManager.getInstance().setSoundToPlay(Sounds.WOODEN_IMPACT.getValue());
            if (distance < distanceToLayer3InsidePerimeter) {
                scoreMultiplier = 1;
                if (distance < distanceToLayer2InsidePerimeter) {
                    turnScore = 50;
                }
                else {
                    turnScore = 30;
                }
            }
            else {
                if (distance < distanceToOuterMostPerimeter) {
                    turnScore = findAngleScore(dartPosX, dartPosY, findAngle(dartPosX, dartPosY));
                    if (distance < distanceToLayer5InsidePerimeter) {
                        if (distance < distanceToLayer4InsidePerimeter) {
                            scoreMultiplier = 1;
                        }
                        else {
                            scoreMultiplier = 3;
                        }
                    }
                    else {
                        if (distance < distanceToLayer6InsidePerimeter) {
                            scoreMultiplier = 1;
                        }
                        else {
                            scoreMultiplier = 2;
                        }
                    }
                }
                else {
                    scoreMultiplier = 1;
                    turnScore = 0;
                }
            }
        }
    }



    private int findAngleScore(int _dartPosX, int _dartPosY, float angle) {
        int xIn = _dartPosX - WINDOW_SIZE.width / 2;
        int yIn = WINDOW_SIZE.height / 2 - _dartPosY;

        if (-yIn > 0) { // if dart is in bottom half of board
            if(xIn > 0) { // if dart is in right half of board
                // System.out.println("Quandrant IV");
                if(angle <= -81) {
                    // System.out.println("hit 3");
                    turnScore = 3;
                }
                else if(angle <= -63) {
                    //System.out.println("hit 17");
                    turnScore = 17;
                }
                else if(angle <= -45) {
                    // System.out.println("hit 2");
                    turnScore = 2;
                }
                else if(angle <= -27) {
                    // System.out.println("hit 15");
                    turnScore = 15;
                }
                else if(angle <= -9) {
                    // System.out.println("hit 10");
                    turnScore = 10;
                }
                else {
                    // System.out.println("hit 6");
                    turnScore = 6;

                }
            }
            else { // if dart is in left half of board
                // System.out.println("Quandrant III");
                if(angle <= -171) {
                    // System.out.println("hit 11");
                    turnScore = 11;
                }
                else if(angle <= -153) {
                    // System.out.println("hit 8");
                    turnScore = 8;
                }
                else if(angle <= -135) {
                    // System.out.println("hit 16");
                    turnScore = 16;
                }
                else if(angle <= -117) {
                    // System.out.println("hit 7");
                    turnScore = 7;
                }
                else if(angle <= -99) {
                    // System.out.println("hit 19");
                    turnScore = 19;
                }
                else {
                    // System.out.println("hit 3");
                    turnScore = 3;
                }
            }
        }
        else { // if dart in top half of board
            if(xIn > 0) { // if dart on right half of board
                if(angle >= 81) {
                    // System.out.println("hit 20");
                    turnScore = 20;
                }
                else if(angle >= 63) {
                    // System.out.println("hit 1");
                    turnScore = 1;
                }
                else if(angle >= 45) {
                    // System.out.println("hit 18");
                    turnScore = 18;
                }
                else if(angle >= 27) {
                    // System.out.println("hit 4");
                    turnScore = 4;
                }
                else if(angle >= 9) {
                    // System.out.println("hit 13");
                    turnScore = 13;
                }
                else {
                    // System.out.println("hit 6");
                    turnScore = 6;
                }
            }
            else { // if dart on left half of board
                // System.out.println("Quandrant II");
                if(angle >= 171) {
                    // System.out.println("hit 11");
                    turnScore = 11;
                }
                else if(angle >= 153) {
                    // System.out.println("hit 14");
                    turnScore = 14;
                }
                else if(angle >= 135) {
                    // System.out.println("hit 9");
                    turnScore = 9;
                }
                else if(angle >= 117) {
                   // System.out.println("hit 12");
                    turnScore = 12;
                }
                else if(angle >= 99) {
                    // System.out.println("hit 5");
                    turnScore = 5;
                }
                else {
                    // System.out.println("20");
                    turnScore = 20;
                }
            }
        }
        return turnScore;
    }

    private long getDistanceFromBullsEye(long dartPosX, long dartPosY) {
        return (long)Math.sqrt(dartPosX * dartPosX + dartPosY * dartPosY);
    }

    private float findAngle(int _dartPosX, int _dartPosY) {
        Point centerPos = getCenterPosition();
        double relativeDartPosY = centerPos.y - (double)_dartPosY;
        double relativeDartPosX = (double)_dartPosX - centerPos.x;
        return (float)Math.toDegrees(Math.atan2(relativeDartPosY, relativeDartPosX)); // y , x
    }

    private Point getCenterPosition() {
        return new Point(getPosX() + getWidth() / 2, getPosY() + getHeight() / 2);
    }

    public int getTurnScore() {
        return turnScore;
    }

    public int getScoreMultiplier() {
        return scoreMultiplier;
    }
}
