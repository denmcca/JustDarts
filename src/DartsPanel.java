/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import components.*;
import components.Button;
import interfaces.Drawable;
import interfaces.GameObject;
import managers.*;

/**
 *
 * @author Dennis
 */
public class DartsPanel extends JPanel
{
    private boolean doThrow = false;
    private final int IMAGE_SCALE = Math.round((float)Main.WIDTH/640f);
    private int attemptsMade = 0;

    //in progress end
    private final static int width = Main.WIDTH, height = Main.HEIGHT;

    //measured units
//    private static int meterUnit = 320; //for every meter there are 320 pixels TODO: z movement
    //measured units end

    // begins in idle animation mode
    private int aniMode = 1;

    // intializes dart location variable
    private int dartDeviationX = 0, dartDeviationY = 0;

    private static boolean endGame = false;
    private static boolean endGameFirstPass = true;

    private final SoundManager soundManager = SoundManager.getInstance();
    private final InputManager inputManager = InputManager.getInstance();
    private final GameObjectManager gameObjectManager = GameObjectManager.getInstance();
    private final RenderManager renderManager = RenderManager.getInstance();
    private final TimeManager timeManager = TimeManager.getInstance();

    private enum GameObjectNames {
        DARTBOARD, DART, WOOD_VERT_BAR, BACKGROUND, WOOD_HORZ_BAR, RESTART,
        QUIT, HELP, HAND, TUTORIAL, WHITE_OUT, SCOREBOARD, SCORE_POP, AIM,
        PLAYER
    }

    DartsPanel() {
        final int helpIconX = (width / 9), helpIconY  = (height * 7 / 9);
        Drawable.SET_SCALE(IMAGE_SCALE);
        Drawable.SET_WINDOW_SIZE(width, height);
        final String[] audioFilePaths = {
                "tinny_impact.wav",
                "wooden_impact.wav",
                "sad_trumbone.wav",
                "missedDart.wav",
                "Game_Over_-_Sound_Effect.wav"
        };
//        prepareAudioFiles(audioFilePaths);
        renderManager.setPanel(this);

        Player player = new Player();
        player.setName(GameObjectNames.PLAYER.toString());
        player.setInputManager(inputManager);
        gameObjectManager.add(player);

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
        tutorialButton.addSubject(player);
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
        h.setPlayer(player);
        h.setName(GameObjectNames.HAND.toString());
        gameObjectManager.add(h);

        AimOscillator aim = new AimOscillator(h);
        aim.setName(GameObjectNames.AIM.toString());
        gameObjectManager.add(aim);

        WhiteOut whiteOut = new WhiteOut();
        whiteOut.setName(GameObjectNames.WHITE_OUT.toString());
        whiteOut.disableDraw();
        gameObjectManager.add(whiteOut);

        QuitButton quitButton = new QuitButton();
        quitButton.setName(GameObjectNames.QUIT.toString());
        quitButton.setPosition(new Point(width * 2 / 3, height / 2));
        quitButton.addSubject(player);
        quitButton.disableDraw();
        gameObjectManager.add(quitButton);

        RestartButton restartButton = new RestartButton();
        restartButton.setName(GameObjectNames.RESTART.toString());
        restartButton.setPosition(new Point(width / 3, height / 2));
        restartButton.addSubject(player);
        restartButton.disableDraw();
        gameObjectManager.add(restartButton);

        ScoreBoard sb = new ScoreBoard();
        sb.setName(GameObjectNames.SCOREBOARD.toString());
        gameObjectManager.add(sb);

        // listeners
        addKeyListener(inputManager.inputListener);
        addMouseMotionListener(inputManager.inputListener);
        addMouseListener(inputManager.inputListener);

        setPreferredSize (new Dimension(width, height));
        setBackground(Color.white);

        // turns off cursor for initial round
        toggleCursor();
    }

    // structure of main loop
//    private void go(Graphics g) {
//        renderManager.setGraphics(g);
//
//        boolean exitGame = false;
//        while (!exitGame) {
//
//            checkIfLeftClick();
//
//            timeManager.update();
//
//            // game logic
//            gameLogic();
//
//            // draw objects
//            renderManager.draw();
//
//            // play audio
//            soundManager.playSounds();
//
//            // update game objects
//            gameObjectManager.update();
//
//            // check inputs
//            inputManager.update();
//            repaint();
//        }
//    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        renderManager.setGraphics(g);

        checkIfLeftClick();

        timeManager.update();

        // game logic
        gameLogic();

        // draw objects
        renderManager.draw();

        // play audio
        soundManager.playSounds();

        // update game objects
        gameObjectManager.update();

        // check inputs
        inputManager.update();
        repaint();
    }

    private void gameLogic() {
        Point aimCircleOffsets = new Point(((AimOscillator)getGameObject(GameObjectNames.AIM)).getAimCirOffsets());
        int dartPosX = getGameObject(GameObjectNames.HAND).getPosX() + dartDeviationX + aimCircleOffsets.x;
        int dartPosY = getGameObject(GameObjectNames.HAND).getPosY() + dartDeviationY + aimCircleOffsets.y;

        // records values for score pops
        if(!endGame ) {
            if (doThrow) {
                doThrow = false;
                ((DartBoard)getGameObject(GameObjectNames.DARTBOARD)).findScore(dartPosX, dartPosY);
                //score handling
                int turnScore = ((DartBoard) getGameObject(GameObjectNames.DARTBOARD)).getTurnScore();
                int scoreMultiplier = ((DartBoard) getGameObject(GameObjectNames.DARTBOARD)).getScoreMultiplier();
                // find score by determining how far the dart is from the center (bull's eye)
                ((ScoreBoard)getGameObject(GameObjectNames.SCOREBOARD))
                        .updateScore(turnScore * scoreMultiplier);

                ScorePop scorePop = new ScorePop(new Point(dartPosX, dartPosY), scoreMultiplier, turnScore,
                        new int[]{
                                255 * (attemptsMade == 0 ? 1 : 0),
                                255 * (attemptsMade == 1 ? 1 : 0),
                                255 * (attemptsMade == 2 ? 1 : 0),
                                255
                        }
                );
                scorePop.setName(GameObjectNames.SCORE_POP.toString() + attemptsMade);
                gameObjectManager.add(scorePop);

                // add dart to gameObjectManager
                throwDart(new Point(dartPosX, dartPosY), GameObjectNames.DART.toString() + attemptsMade);
            }
            if (aniMode == 2) {
                ((Hand)getGameObject(GameObjectNames.HAND)).changeAnimationSequence(Hand.AnimationSequence.HOLD);
            }
            // aim bar is flowing
            else if (aniMode == 3) {
                // throwing animation has completed
                if (((Hand)getGameObject(GameObjectNames.HAND)).getAnimationSequence() == Hand.AnimationSequence.THROWN) {
                    doThrow = true;
                    aniMode = 4;
                }
            }
        }


        if (endGameFirstPass & endGame) {
            endGameFirstPass = false;
            toggleCursor();
            updateHighestScore();
            ((Button)getGameObject(GameObjectNames.QUIT)).enableDraw();
            ((Button)getGameObject(GameObjectNames.RESTART)).enableDraw();
            ((Tutorial)getGameObject(GameObjectNames.TUTORIAL)).disableDraw();
            soundManager.setSoundToPlay(Sounds.GAMEOVER.getValue());
            // Moves whiteout component to front
            gameObjectManager.add(gameObjectManager.remove(getGameObject(GameObjectNames.WHITE_OUT)));
            gameObjectManager.add(gameObjectManager.remove(getGameObject(GameObjectNames.SCOREBOARD)));
            ((ScoreBoard)getGameObject(GameObjectNames.SCOREBOARD)).setShouldTransition(true);
            gameObjectManager.add(gameObjectManager.remove(getGameObject(GameObjectNames.RESTART)));
            gameObjectManager.add(gameObjectManager.remove(getGameObject(GameObjectNames.QUIT)));
            ((WhiteOut)getGameObject(GameObjectNames.WHITE_OUT)).enableDraw();
        }
    }

    // if final attempt is true then set endGame to true
    private void checkIfGameEnds() {
        int attemptCount = 3;
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
//                        ((AimOscillator)getGameObject(GameObjectNames.AIM)).toggleOscillation(); // turns off oscillation
                        ((Hand)getGameObject(GameObjectNames.HAND)).changeAnimationSequence(Hand.AnimationSequence.HOLD);
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
                        ((Hand)getGameObject(GameObjectNames.HAND)).changeAnimationSequence(Hand.AnimationSequence.THROWING);
                        ((Tutorial)getGameObject(GameObjectNames.TUTORIAL)).disableDraw();
                        ((Tutorial)getGameObject(GameObjectNames.TUTORIAL)).setCurrentFrame(0);
                    }
                    // animation is THROWN
                    else if (aniMode == 4) { // state five after throw animation (time)
                        ((PowerBar)getGameObject(GameObjectNames.WOOD_VERT_BAR)).disableDraw();
                        ((PowerBar)getGameObject(GameObjectNames.WOOD_VERT_BAR)).enableFlowLock();
                        ((AimBar)getGameObject(GameObjectNames.WOOD_HORZ_BAR)).disableDraw();
                        ((AimBar)getGameObject(GameObjectNames.WOOD_HORZ_BAR)).enableFlowLock();

                        attemptsMade++;
                        checkIfGameEnds();
                        if (!endGame) {
                            // switches to IDLE animation
//                            ((AimOscillator)getGameObject(GameObjectNames.AIM)).toggleOscillation(); // turns on oscillation
                            ((Hand)getGameObject(GameObjectNames.HAND)).changeAnimationSequence(Hand.AnimationSequence.IDLE);
                            aniMode = 1;
                        }
                    }
                }
            }
            else {
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
        gameObjectManager.add(((Hand)getGameObject(GameObjectNames.HAND)).throwDart(dartPos, name));
    }

    private void updateHighestScore() {
        // updates all time highest score
        ((WhiteOut)getGameObject(GameObjectNames.WHITE_OUT)).compareHighScore(
                ((ScoreBoard)getGameObject(GameObjectNames.SCOREBOARD)).getHighScore()
        );
    }

    private void prepareAudioFiles(String[] audioFilePaths) {
        for (String audioFilePath : audioFilePaths) {
            SoundEffect sound = new SoundEffect(SoundManager.PATH_TO_DIR + audioFilePath);
            soundManager.addSound(sound);
        }
    }

    private void calculateDartDeviation() {
        int power = ((PowerBar) gameObjectManager.getGameObjectByName(GameObjectNames.WOOD_VERT_BAR.toString())).getBarValue();
        int deviation = ((AimBar) gameObjectManager.getGameObjectByName(GameObjectNames.WOOD_HORZ_BAR.toString())).getBarValue();
        if (power == 0)
            power = 1;

        // value which affects the downward offset of dart after throw
        int gravity = 9;
        // distance between player and board
        int distance = 400;
        try {
            dartDeviationY = gravity * (distance / power) * (distance / power);
        }
        catch (ArithmeticException e) {
            e.printStackTrace();
            dartDeviationY = gravity * distance * distance;
        }

        dartDeviationX = deviation * (distance / power);
    }

    private void gameReset() {
        toggleCursor();
        attemptsMade = 0;
        System.out.println("resetting values");
        ((RestartButton)getGameObject(GameObjectNames.RESTART)).disableDraw();
        ((QuitButton)getGameObject(GameObjectNames.QUIT)).disableDraw();
        endGame = false;
        aniMode = 1;
        endGameFirstPass = true;
        gameObjectManager.getGameObjects().values().forEach( go -> go.reset());
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

    private void toggleCursor() {
        Toolkit t = Toolkit.getDefaultToolkit();
        Image i = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Cursor noCursor = t.createCustomCursor(i, new Point(0, 0), "none");
        if (getCursor() == Cursor.getDefaultCursor()) {
            setCursor(noCursor);
        }
        else {
            setCursor(Cursor.getDefaultCursor());
        }
    }
}