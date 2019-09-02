/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JustDarts;

import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.*;

import JustDarts.components.*;
import JustDarts.components.Button;
import JustDarts.interfaces.Drawable;
import JustDarts.interfaces.GameObject;
import JustDarts.managers.*;

/**
 *
 * @author Dennis
 */
public class DartsPanel extends JPanel
{
    private boolean doThrow = false;
    private int IMAGE_SCALE = Math.round((float)Main.WIDTH/640f);

    private int attemptCount = 3;
    private int attemptsMade = 0;

    private boolean exitGame = false;

    //in progress end
    private final static int width = Main.WIDTH, height = Main.HEIGHT;

    //measured units
//    private static int meterUnit = 320; //for every meter there are 320 pixels TODO: z movement
    //measured units end

    //toggles
    private static boolean driftCxl = true; //toggle drift (true off, false on)
    //toggles end

    public enum AudioIndex { TINNY_IMPACT, WOODEN_IMPACT, TERRIBLE, WOOD_PLASTIC_IMPACT, GAMEOVER; }

    private String[] audioFilePaths = {
            "tinny_impact.wav",
            "wooden_impact.wav",
            "sad_trumbone.wav",
            "missedDart.wav",
            "Game_Over_-_Sound_Effect.wav"};
    //audio end

    //score handling
    private static int turnScore = 0, playerScore = 0, scoreMultiplier = 0;
    //score handling end

    // begins in idle animation mode
    private int aniMode = 1;
    private boolean driftXrev = false, driftYrev = false;
    private int aimCirOffsetX = 0, aimCirOffsetY = 1, aimCirRateY = 5 * IMAGE_SCALE;
    private final int circleAimMax = 75 * IMAGE_SCALE;
    private int power = 5, deviation = 1;
    // simulated distance between player and board
    private final int distance = 400;
    // simulated gravity effect which affects the downward offset of dart after throw
    private final int gravity = 9;

    // sets initial direction of aim circle
    private int aimCircleDir = 1;

    // intializes dart location variable
    private int dartDeviationX = 0, dartDeviationY = 0;
    // intializes drift values
    private int driftX = 15, driftY = 0, driftMax = 15 * IMAGE_SCALE;

    private static boolean endGame = false;
    private static boolean endGameFirstPass = true;

    private SoundManager soundManager;
    private InputManager inputManager = InputManager.getInstance();
    private GameObjectManager gameObjectManager = GameObjectManager.getInstance();
    private RenderManager renderManager = RenderManager.getInstance();
    private TimeManager timeManager = TimeManager.getInstance();

    private enum GameObjectNames {
        DARTBOARD, DART, WOOD_VERT_BAR, BACKGROUND, WOOD_HORZ_BAR, RESTART, QUIT, HELP, PLAYER, TUTORIAL, WHITE_OUT,
        SCOREBOARD
    };

    DartsPanel() {
        int helpIconX = (width / 9), helpIconY  = (height * 7 / 9);

        Drawable.SET_SCALE(IMAGE_SCALE);
        Drawable.SET_WINDOW_SIZE(width, height);
        soundManager = SoundManager.getInstance();
        prepareAudioFiles(audioFilePaths);
        renderManager.setPanel(this);

        GameObject wall = new Wall();
        wall.setName(GameObjectNames.BACKGROUND.toString());
        gameObjectManager.add(wall);

        DartBoard dartBoard = new DartBoard();
        dartBoard.setName(GameObjectNames.DARTBOARD.toString());
        dartBoard.setPosition((width - dartBoard.getWidth()) / 2,
                (height - dartBoard.getHeight()) / 2);
        gameObjectManager.add(dartBoard);

        TutorialButton tutorialButton = new TutorialButton();
        tutorialButton.setName(GameObjectNames.HELP.toString());
        tutorialButton.setPosition(new Point(helpIconX, helpIconY));
        gameObjectManager.add(tutorialButton);

        Tutorial tutorial = new Tutorial();
        tutorial.setName(GameObjectNames.TUTORIAL.toString());
        tutorial.setPosition(width / 20, height * 3 / 5);
        tutorial.disableDraw();
        gameObjectManager.add(tutorial);


        PowerBar powerBar = new PowerBar();
        powerBar.setName(GameObjectNames.WOOD_VERT_BAR.toString());
        powerBar.setPosition(width * 7 / 8, (height - powerBar.getHeight()) / 2);
        powerBar.disableDraw();
        powerBar.disableFlowLock();
        gameObjectManager.add(powerBar);

        AimBar aimBar = new AimBar();
        aimBar.setName(GameObjectNames.WOOD_HORZ_BAR.toString());
        aimBar.setPosition((width - aimBar.getWidth()) / 2, (height / 4 - aimBar.getHeight()) / 2);
        aimBar.disableDraw();
        gameObjectManager.add(aimBar);

        Hand h = new Hand();
        h.setInputManager(inputManager);
        h.setName(GameObjectNames.PLAYER.toString());
        h.setPosition(width / 2, height / 2);
        gameObjectManager.add(h);

        WhiteOut whiteOut = new WhiteOut();
        whiteOut.setName(GameObjectNames.WHITE_OUT.toString());
        whiteOut.disableDraw();
        gameObjectManager.add(whiteOut);

        QuitButton quitButton = new QuitButton();
        quitButton.setName(GameObjectNames.QUIT.toString());
        quitButton.setPosition(new Point(width * 2 / 3, height / 2));
        quitButton.disableDraw();
        gameObjectManager.add(quitButton);

        RestartButton restartButton = new RestartButton();
        restartButton.setName(GameObjectNames.RESTART.toString());
        restartButton.setPosition(new Point(width / 3, height / 2));
        restartButton.disableDraw();
        gameObjectManager.add(restartButton);

        ScoreBoard sb = new ScoreBoard();
        sb.setName(GameObjectNames.SCOREBOARD.toString());
        gameObjectManager.add(sb);

        // listeners
        InputManager.InputListener inputListener = inputManager.inputListener;
        addKeyListener(inputListener);
        addMouseMotionListener(inputListener);
        addMouseListener(inputListener);

        setPreferredSize (new Dimension(width, height));
        setBackground(Color.white);
    }

    private void go(Graphics g) {
        renderManager.setGraphics(g);

        while (!exitGame) {
            timeManager.update();

            // update game objects
            gameObjectManager.update();

            // draw objects
            renderManager.draw();

            // play audio

            // check inputs
            inputManager.update();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        checkIfLeftClick();
        // checks if any attempts left

        renderManager.setGraphics(g);
        timeManager.update();

        drift();

        int dartPosX = getGameObject(GameObjectNames.PLAYER).getPosX() + dartDeviationX + aimCirOffsetX;
        int dartPosY = getGameObject(GameObjectNames.PLAYER).getPosY() + dartDeviationY + aimCirOffsetY;

        // records values for score pops
        if(!endGame ) {
            if (doThrow) {
                doThrow = false;
                ((DartBoard)getGameObject(GameObjectNames.DARTBOARD)).findScore(dartPosX, dartPosY);
                turnScore = ((DartBoard)getGameObject(GameObjectNames.DARTBOARD)).getTurnScore();
                scoreMultiplier = ((DartBoard)getGameObject(GameObjectNames.DARTBOARD)).getScoreMultiplier();
                // find score by determining how far the dart is from the center (bull's eye)
                ((ScoreBoard)getGameObject(GameObjectNames.SCOREBOARD))
                        .updateScore(turnScore * scoreMultiplier);

                ScorePop scorePop = new ScorePop(new Point(dartPosX, dartPosY), scoreMultiplier, turnScore,
                        new int[]{
                                255 * (attemptsMade == 0 ? 1 : 0),
                                255 * (attemptsMade == 1 ? 1 : 0),
                                255 * (attemptsMade == 2 ? 1 : 0),
                                255
                        });
                scorePop.setName("SCOREPOP" + attemptsMade);
                gameObjectManager.add(scorePop);

                // add dart to gameObjectManager
                throwDart(new Point(dartPosX, dartPosY), GameObjectNames.DART.toString() + attemptsMade);
            }
            if (aniMode == 2) {
                ((Hand)getGameObject(GameObjectNames.PLAYER)).changeAnimationSequence(Hand.AnimationSequence.HOLD);
            }
            // aim bar is flowing
            else if (aniMode == 3) {
                // throwing animation has completed
                if (((Hand)getGameObject(GameObjectNames.PLAYER)).getAnimationSequence() == Hand.AnimationSequence.THROWN) {
                    doThrow = true;
                    aniMode = 4;
                }
            }
            else if (aniMode == 1) {
                oscillateAimCircle();
            }
        }


        if (endGameFirstPass & endGame) {
            endGameFirstPass = false;
            ((Button)getGameObject(GameObjectNames.QUIT)).enableDraw();
            ((Button)getGameObject(GameObjectNames.RESTART)).enableDraw();
            ((Tutorial)getGameObject(GameObjectNames.TUTORIAL)).disableDraw();
            soundManager.playSound(AudioIndex.GAMEOVER.ordinal());
            // put white out to the front
            gameObjectManager.add(gameObjectManager.remove(getGameObject(GameObjectNames.WHITE_OUT)));
            gameObjectManager.add(gameObjectManager.remove(getGameObject(GameObjectNames.SCOREBOARD)));
            ((ScoreBoard)getGameObject(GameObjectNames.SCOREBOARD)).setShouldTransition(true);
            gameObjectManager.add(gameObjectManager.remove(getGameObject(GameObjectNames.RESTART)));
            gameObjectManager.add(gameObjectManager.remove(getGameObject(GameObjectNames.QUIT)));
            ((WhiteOut)getGameObject(GameObjectNames.WHITE_OUT)).enableDraw();
        }

        renderManager.draw();
        if (!endGame) drawAimCircle(g);;
        gameObjectManager.update();
        inputManager.update();
        repaint();
    }

    private void drawAimCircle(Graphics g) {
        // rendering aim circle
        g.setColor(Color.white);
        g.drawOval(getGameObject(GameObjectNames.PLAYER).getPosX()
                        + aimCirOffsetX - 10 / 2 * IMAGE_SCALE, // TODO: Fix this
                gameObjectManager.getGameObjectByName(GameObjectNames.PLAYER.toString()).getPosY()
                        + aimCirOffsetY - 10 / 2 * IMAGE_SCALE,  // TODO: Fix this
                20 * IMAGE_SCALE, 20 * IMAGE_SCALE);
    }

    private void loadState1() {

    }
    private void loadState2() {

    }
    private void loadState3() {

    }
    private void loadState4() {

    }
    private void loadState5() {

    }
    private void loadState6() {

    }


    // if final attempt is true then set endGame to true
    private void checkIfGameEnds() {
        if (attemptsMade == attemptCount & !endGame) {
            endGame = true;
            System.out.println("Game set to end");
        }
    }

    private void checkIfLeftClick() {
//        System.out.println(aniMode);
        if (inputManager.wasButtonJustReleased(1)) {
            if (!endGame) {
                // if click detected on help icon
                if (((Button)getGameObject(GameObjectNames.HELP)).isActive()) {
                    // enables tutorial
                    ((Tutorial)getGameObject(GameObjectNames.TUTORIAL)).enableDraw();
                }
                else
                {
                    // animation mode is HOLD
                    if(aniMode == 1) {// state 2 after first click
                        // lock hand in place
                        ((Hand)getGameObject(GameObjectNames.PLAYER)).toggleLock();
                        ((Hand)getGameObject(GameObjectNames.PLAYER)).changeAnimationSequence(Hand.AnimationSequence.HOLD);
                        // power bar to turn on
                        ((PowerBar)getGameObject(GameObjectNames.WOOD_VERT_BAR)).enableDraw();
                        // power bar flow to turn on
                        ((PowerBar)getGameObject(GameObjectNames.WOOD_VERT_BAR)).disableFlowLock();
                        ((Tutorial)getGameObject(GameObjectNames.TUTORIAL)).setCurrentFrame(1);
                        aniMode = 2;
                        // enabled power bar
                        // switch to HOLD animation
                    }
                    // animation is still HOLD
                    else if (aniMode == 2) { // state three after second click
                        // lock power bar
                        ((PowerBar)getGameObject(GameObjectNames.WOOD_VERT_BAR)).enableFlowLock(); // stop power flow
                        // enables aim bar
                        ((AimBar)getGameObject(GameObjectNames.WOOD_HORZ_BAR)).enableDraw();
                        // aim bar flow to turn on
                        ((AimBar)getGameObject(GameObjectNames.WOOD_HORZ_BAR)).disableFlowLock();
                        ((Tutorial)getGameObject(GameObjectNames.TUTORIAL)).setCurrentFrame(2);
                        aniMode = 3;
                    }
                    // animation is changed to THROWING
                    else if (aniMode == 3) {// state four after third click
                        ((AimBar)getGameObject(GameObjectNames.WOOD_HORZ_BAR)).enableFlowLock(); // stop aim flow
                        // calculates dart accuracy at click
                        calculateDartDeviation();
                        // changes to throwing then transitions to animation mode 4 after
                        ((Hand)getGameObject(GameObjectNames.PLAYER)).changeAnimationSequence(Hand.AnimationSequence.THROWING);
                        ((Tutorial)getGameObject(GameObjectNames.TUTORIAL)).disableDraw();
                        ((Tutorial)getGameObject(GameObjectNames.TUTORIAL)).setCurrentFrame(0);
                    }
                    // animation is THROWN
                    else if (aniMode == 4) { // state five after throw animation (time)
                        ((PowerBar)getGameObject(GameObjectNames.WOOD_VERT_BAR)).disableDraw();
                        ((PowerBar)getGameObject(GameObjectNames.WOOD_VERT_BAR)).enableFlowLock();
                        ((AimBar)getGameObject(GameObjectNames.WOOD_HORZ_BAR)).disableDraw();
                        ((AimBar)getGameObject(GameObjectNames.WOOD_HORZ_BAR)).enableFlowLock();
                        // turns off tutorial if on

                        attemptsMade++;
                        checkIfGameEnds();
                        if (!endGame) {
                            // unlocks hand movement if game isn't over
                            ((Hand)getGameObject(GameObjectNames.PLAYER)).toggleLock();
                            // switches to IDLE animation
                            ((Hand)getGameObject(GameObjectNames.PLAYER)).changeAnimationSequence(Hand.AnimationSequence.IDLE);
                            aniMode = 1;
                        }
                    }
                }
            }
            else {
//                timer.stop();
                aniMode = 0;

                // if player clicks restart button
                if (((Button)getGameObject(GameObjectNames.RESTART)).isActive()) {
                    gameReset();
                }
                // if player clicks quit button
                if (((Button)gameObjectManager.getGameObjectByName(GameObjectNames.QUIT.toString())).isActive()) {
                    gameQuit();
                }
            }
        }
    }

    private void throwDart(Point dartPos, String name) {
        gameObjectManager.add(((Hand)getGameObject(GameObjectNames.PLAYER)).throwDart(dartPos, name));
    }

    private void prepareAudioFiles(String[] audioFilePaths) {
        for (int i = 0; i < audioFilePaths.length; i++) {
            Clip clip = soundManager.createAudioClip(audioFilePaths[i]);
            soundManager.addSound(i, clip);
        }
    }

    private void drift() {
            if (aniMode == 1 && !driftCxl)
            {
                if(driftYrev)
                {
                    driftY = --driftY;
                    if (driftY < -driftMax)
                        driftYrev = false;
                }
                else
                {
                    driftY = ++driftY;
                    if (driftY > driftMax)
                        driftYrev = true;
                }
                if(driftXrev)
                {
                    driftX = --driftX;
                    if(driftX < -driftMax)
                        driftXrev = false;
                }
                else
                {
                    driftX = ++driftX;
                    if(driftX > driftMax)
                        driftXrev = true;
                }
            }
    }

    private void calculateDartDeviation() {
        power = ((PowerBar)gameObjectManager.getGameObjectByName(GameObjectNames.WOOD_VERT_BAR.toString())).getBarValue();
        deviation = ((AimBar)gameObjectManager.getGameObjectByName(GameObjectNames.WOOD_HORZ_BAR.toString())).getBarValue();
        if (power == 0)
            power = 1;

        try {
            dartDeviationY = gravity * (distance / power) * (distance / power);
        }
        catch (ArithmeticException e) {
            System.out.println(e);
            dartDeviationY = gravity * distance * distance;
        }

        dartDeviationX = deviation * (distance / power);
    }

    private float frameDelay = 0;

    private void oscillateAimCircle() {
        frameDelay += TimeManager.getInstance().getDeltaTime();
        if (frameDelay > 25f) {
            frameDelay -= 25f;
            if (aimCircleDir == -1) {
                aimCirOffsetY -= aimCirRateY;// / TimeManager.getInstance().getDeltaTime();
                if (aimCirOffsetY < -circleAimMax) {
                    aimCircleDir = 1;
                    aimCirOffsetY = -circleAimMax;
                }
            } else {
                aimCirOffsetY += aimCirRateY;// / TimeManager.getInstance().getDeltaTime();
                if (aimCirOffsetY > circleAimMax) {
                    aimCircleDir = -1;
                    aimCirOffsetY = circleAimMax;
                }
            }
            aimCirOffsetX = -75 * IMAGE_SCALE;
        }
    }

    private void gameReset() {
        attemptsMade = 0;
        System.out.println("resetting values");
        playerScore = 0;
        ((Hand)getGameObject(GameObjectNames.PLAYER)).toggleLock();
        ((RestartButton)getGameObject(GameObjectNames.RESTART)).disableDraw();
        ((QuitButton)getGameObject(GameObjectNames.QUIT)).disableDraw();
        endGame = false;
        aniMode = 1;
        endGameFirstPass = true;
        gameObjectManager.getGameObjects().forEach((k, go) -> {
            go.reset();
        });
        ((WhiteOut)getGameObject(GameObjectNames.WHITE_OUT)).disableDraw();
        gameObjectManager.collectGarbage();
    }

    private GameObject getGameObject(GameObjectNames name) {
        return gameObjectManager.getGameObjectByName(name.toString());
    }

    private void gameQuit() {
        System.exit(0);
    }

//    private double getPlayerScore() { // TODO: store scores
//        return playerScore;
//    }
//
//    private void highScoreCheck() { // TODO: reinstate high scores
//        if (playerScore > highScore) {
//            highScore = playerScore;
//        }
//    }
}