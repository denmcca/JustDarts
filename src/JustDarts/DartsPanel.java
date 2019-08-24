/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JustDarts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;
import java.net.*;
import java.applet.*;

/**
 *
 * @author Dennis
 */
public class DartsPanel extends JPanel
{
    private int IMAGE_SCALE = (int)((float)Main.WIDTH/640f);
    private int xCirPos = 50 * IMAGE_SCALE;
    private int cirRadius = 50 * IMAGE_SCALE;
    private int mod = -1;

    //in progress end
    private final static int width = Main.WIDTH, height = Main.HEIGHT;

    //measured units
    private static int meterUnit = 320; //for every meter there are 320 pixels
    //measured units end

    //toggles
    private static boolean driftCxl = true; //toggle drift (true off, false on)
    //toggles end

    //audio implementation
    private String tinny_impact = "file:./audio/tinny_impact.wav",
            wooden_impact = "file:./audio/wooden_impact.wav",
            terrible = "file:./audio/PIR_fail.wav",
            woodPlastic = "file:./audio/missedDart.wav",
            gameOverSong = "file:./audio/Game_Over_-_Sound_Effect.wav";
    private Clip clip;

    private File input;
    private AudioInputStream sound;
    private DataLine.Info info;
    //audio end

    //graphics
    private String handArrStrs[] = {
            "./img/hand1.png", "./img/hand2.png", "./img/hand3.png",
            "./img/hand4.png", "./img/hand5.png", "./img/hand6.png",
            "./img/hand7.png"};
    private int handIndexSequence[] = {0,1,2,3,4,5,4,3,2,1,0,6};
    private ImageIcon handArr[];
    private final int HAND_FRAME_COUNT = handArrStrs.length;
    private ImageIcon hand, board, bg, dart, meterBar, aimBar, quitIcon,
            quitIcon2, restartIcon, restartIcon2, tutorial1PopUp,
            tutorial2PopUp, tutorial3PopUp, helpIcon, helpIcon2;
    public boolean tutorial = false, tutorial1 = false, tutorial2 = false,
            tutorial3 = false, handDraw = true;
    public int circleDiameter = 20 * IMAGE_SCALE;
    private final int circleAimMax = 50 * IMAGE_SCALE;
    private int xHand, yHand, whiteOut = 0;
    private int scoreColor1 = 255, scoreColor2 = 255, scoreColor3 = 255,
            scoreColor4 = 255;
    public int helpIconX = (width / 9), helpIconY  = (height * 7 / 9);
    public int pingSpeedMod = 0;
    //graphics ends

    //score handling
    private static int turnScore = 0, turnScore1 = 0, turnScore2 = 0,
            turnScore3 = 0, playerScore = 0, scoreMultiplier = 0,
            scoreMultiplier1 = 0, scoreMultiplier2 = 0, scoreMultiplier3, sideX,
            sideY;

    private static int scoreBoardX = (width * 7 / 10),
            scoreBoardY = (height / 15), pointsSum = 0,
            highScoreBoardX = (width / 10), highScoreBoardY = scoreBoardY;
    private static boolean scorePop1 = false, scorePop2 = false,
            scorePop3 = false;
    private static int scorePop1X, scorePop1Y, scorePop2X, scorePop2Y,
            scorePop3X, scorePop3Y;
    private static int highScore = 0;

    //score handling end
    private int handArrIndex = 0;
    private static double angle;

    private int DELAY = 120, DELAYFLOW = 18;
    private int aniMode = 1;
    private boolean driftXrev = true, driftYrev = true, imageResize = true;
    private boolean attempt1 = false, attempt1F = false, attempt2 = false, 
            attempt2F = false, attempt3 = false, attempt3F = false, 
            lockBar = false, lockAimBar = false;
    private int X1, Y1, X2, Y2, X3, Y3, xAim = 0, yAim = 1, yAimFactor = 2;
    private int power = 5, deviation = 1;
    private final int distance = 300, gravity = 5;
    private int powerBarFlow = 2, aimBarFlow = -150, powerBarFactor = 1,
            aimBarFactor = 5, dirPBF = 1, dirABF = 1, dirAimCircle = 1;

    private Timer timer, timerFlow;
    private int x = (width / 2) , y = (height / 2), driftX = 15, driftY = 0,
            dartX = 0, dartY = 0, driftMax = 15 * IMAGE_SCALE;

    //game control
    private static boolean endGame = false, restartGame = false, endGameFirstPass = true;
    //game control end
    private Applet applet = new Applet();
    private AudioClip dartWoodenImpactSnd, dartTinnyImpactSnd, dartWoodPlasticSnd, gameOverSongSnd, terribleSnd;

    DartsPanel() {
        // set hand animation images
        Dimension handDim = new Dimension((int)(44f/640f*(float)width), (int)(66f/480f*(float)height));
        handArr = new ImageIcon[HAND_FRAME_COUNT];
        for (int i = 0; i < HAND_FRAME_COUNT; i++) {
            handArr[i] = getImage(handArrStrs[i]);
            handArr[i].setImage(handArr[i].getImage().getScaledInstance(
                    handDim.width * IMAGE_SCALE,
                    handDim.height * IMAGE_SCALE,
                    Image.SCALE_FAST));
        }

        // sound
        dartWoodenImpactSnd = createAudioClip(wooden_impact);
        dartTinnyImpactSnd = createAudioClip(tinny_impact);
        dartWoodPlasticSnd = createAudioClip(woodPlastic);
        gameOverSongSnd = createAudioClip(gameOverSong);
        terribleSnd = createAudioClip(terrible);

        timer = new Timer(DELAY, new PlayerListener());
        timerFlow = new Timer(DELAYFLOW, new TimerListener());
        
        PlayerListener listener = new PlayerListener();

        board = getImage("./img/BigDartboard.png");       
        board.setImage(board.getImage().getScaledInstance(144 * IMAGE_SCALE, 144 * IMAGE_SCALE, Image.SCALE_SMOOTH));

        dart = getImage("./img/dart.png");
        dart.setImage(dart.getImage().getScaledInstance(10 * IMAGE_SCALE, 10 * IMAGE_SCALE, Image.SCALE_SMOOTH));

        meterBar = getImage("./img/meter_bar_wood.png");
        meterBar.setImage(meterBar.getImage().getScaledInstance(60 * IMAGE_SCALE, 275 * IMAGE_SCALE, Image.SCALE_SMOOTH));


        bg = getImage("./img/bg.jpg");
        bg.setImage(bg.getImage().getScaledInstance(width, height, Image.SCALE_FAST));
        
        aimBar = getImage("img/aimBarWood.png");
        aimBar.setImage(aimBar.getImage().getScaledInstance(350 * IMAGE_SCALE,40 * IMAGE_SCALE, Image.SCALE_SMOOTH));
        
        restartIcon = getImage("img/restart.png");
        restartIcon.setImage(restartIcon.getImage().getScaledInstance(75 * IMAGE_SCALE, 75 * IMAGE_SCALE, Image.SCALE_SMOOTH));
        
        restartIcon2 = getImage("img/restart2.png");
        restartIcon2.setImage(restartIcon2.getImage().getScaledInstance(75 * IMAGE_SCALE, 75 * IMAGE_SCALE, Image.SCALE_SMOOTH));
        
        quitIcon = getImage("img/quit.png");
        quitIcon.setImage(quitIcon.getImage().getScaledInstance(75 * IMAGE_SCALE, 75 * IMAGE_SCALE, Image.SCALE_SMOOTH));
                
        quitIcon2 = getImage("img/quit2.png");
        quitIcon2.setImage(quitIcon2.getImage().getScaledInstance(75 * IMAGE_SCALE, 75 * IMAGE_SCALE, Image.SCALE_SMOOTH));
        
        helpIcon = getImage("img/help.png");
        helpIcon.setImage(helpIcon.getImage().getScaledInstance(75 * IMAGE_SCALE, 75 * IMAGE_SCALE, Image.SCALE_SMOOTH));
        
        helpIcon2 = getImage("img/help2.png");
        helpIcon2.setImage(helpIcon2.getImage().getScaledInstance(75 * IMAGE_SCALE, 75 * IMAGE_SCALE, Image.SCALE_SMOOTH));

        tutorial1PopUp = getImage("img/tutorial1.png");
        tutorial1PopUp.setImage(tutorial1PopUp.getImage().getScaledInstance(230 * IMAGE_SCALE, 150 * IMAGE_SCALE, Image.SCALE_SMOOTH));
        tutorial2PopUp = getImage("img/tutorial2.png");
        tutorial2PopUp.setImage(tutorial2PopUp.getImage().getScaledInstance(230 * IMAGE_SCALE, 150 * IMAGE_SCALE, Image.SCALE_SMOOTH));
        tutorial3PopUp = getImage("img/tutorial3.png");
        tutorial3PopUp.setImage(tutorial3PopUp.getImage().getScaledInstance(230 * IMAGE_SCALE, 150 * IMAGE_SCALE, Image.SCALE_SMOOTH));

        setPreferredSize (new Dimension(width, height));
        setBackground(Color.white);
        
        addMouseListener(listener);
        addMouseMotionListener(listener);

        gameReset();
        //CheckingAccount.bettingCharge();
        
        xHand = width / 2;
        yHand = height / 2;
        
//        highScore = CheckingAccount.acct1.getHighScore();
        highScore = -1;

        timer.start();
        timerFlow.start();
        tutorial = false; //CheckingAccount.acct1.tutorialState;
        

    }
    
    public ImageIcon getImage(String file)
    {
        ImageIcon temp;
        
        java.net.URL bgUrl = getClass().getResource(file);
        if (bgUrl != null) 
        {
            temp = new ImageIcon(bgUrl);
        } 
        else 
        {
            throw new IllegalArgumentException("This icon file does not exist");
        }
        
        return temp;
    }
    
    public void paintComponent(Graphics g)
    {
//        System.out.println("in paint");
        super.paintComponent(g);
                
        if(hand != null && board != null)
        {
            bg.paintIcon(this, g, 0, 0); //paints wall background
            board.paintIcon(this, g, (width / 2) - (board.getIconWidth() / 2) + 1, (height / 2) - (board.getIconHeight() / 2) + 1);
            drift();

            int dartPosX = xHand - (dart.getIconWidth() / 2) + dartX;
            int dartPosY = yHand - dart.getIconHeight() / 2 + dartY;

            if(attempt1)
            {
//                System.out.println("inside attempt1");
                if(!(attempt1F))
                {
                    X1 = dartPosX;
                    Y1 = dartPosY;
                    sideX = X1;
                    sideY = Y1;
                    attempt1F = true;
                    angle = angleFind(X1 - (width / 2), -Y1 + (height / 2));
                    scoreFind(X1 - (width / 2), -Y1 + (height / 2));
                    scorePop1 = true;
                    scorePop1X = X1;
                    scorePop1Y = Y1;
                    turnScore1 = turnScore;
                    scoreMultiplier1 = scoreMultiplier;
                }
                
                if(scorePop1)
                {
                    g.setColor(Color.blue);
                    g.setFont(new Font("TimesRoman", Font.BOLD, 20 * IMAGE_SCALE));
                    
                    if(scoreMultiplier1 > 1)
                    {
                        g.drawString(turnScore1 + " x " + scoreMultiplier1, scorePop1X, scorePop1Y);
                    }
                    else
                    {
                        g.drawString(Integer.toString(turnScore1), scorePop1X, scorePop1Y);
                    }
                    
                    if(scorePop1Y > 0)
                    {
                        scorePop1Y--;
                    }
                    else
                    {
                        scorePop1 = false;
                    }
                }

                g.setColor(Color.blue.brighter().brighter());
                g.drawOval(X1 - (circleDiameter / 2), Y1 - (circleDiameter / 2), circleDiameter, circleDiameter);
                dart.paintIcon(this, g, X1 - (dart.getIconWidth() / 2), Y1 - (dart.getIconHeight() / 2));
            }
            
            if(attempt2)
            {
                if(!(attempt2F))
                {
                    X2 = dartPosX;
                    Y2 = dartPosY;
                    sideX = X2;
                    sideY = Y2;
                    attempt2F = true;
                    angle = angleFind(X2 - (width / 2), -Y2 + (height / 2));
                    scoreFind(X2 - (width / 2), -Y2 + (height / 2));
                    scorePop2 = true;
                    scorePop2X = X2;
                    scorePop2Y = Y2;
                    turnScore2 = turnScore;
                    scoreMultiplier2 = scoreMultiplier;
                }
                
                if(scorePop2)
                {
                    g.setColor(Color.yellow);
                    g.setFont(new Font("TimesRoman", Font.BOLD, 20 * IMAGE_SCALE));
                    
                    if(scoreMultiplier2 > 1)
                    {
                        g.drawString(turnScore2 + " x " + scoreMultiplier2, scorePop2X, scorePop2Y);
                    }
                    else
                    {
                        g.drawString(Integer.toString(turnScore2), scorePop2X, scorePop2Y);
                    }
                    
                    if(scorePop2Y > 0)
                    {
                        scorePop2Y--;
                    }
                    else
                    {
                        scorePop2 = false;
                    }
                }
                g.setColor(Color.yellow.brighter().brighter());
                g.drawOval(X2 - (circleDiameter / 2), Y2 - (circleDiameter / 2), circleDiameter, circleDiameter);                
                dart.paintIcon(this, g, X2 - (dart.getIconWidth() / 2), Y2 - (dart.getIconHeight() / 2));
            }
            if(attempt3)
            {
                if(!(attempt3F))
                {
                    X3 = dartPosX;
                    Y3 = dartPosY;
                    sideX = X3;
                    sideY = Y3;
                    attempt3F = true;
                    angle = angleFind(X3 - (width / 2), -Y3 + (height / 2));
                    scoreFind(X3 - (width / 2), -Y3 + (height / 2));
                    
                    scorePop3 = true;
                    scorePop3X = X3;
                    scorePop3Y = Y3;
                    turnScore3 = turnScore;
                    scoreMultiplier3 = scoreMultiplier;

                    endGame = true;
                }
                if(scorePop3)
                {
                    g.setColor(Color.green);
                    g.setFont(new Font("TimesRoman", Font.BOLD, 20 * IMAGE_SCALE));
                    if(scoreMultiplier3 > 1)
                    {
                        g.drawString(turnScore3 + " x " + scoreMultiplier3, scorePop3X, scorePop3Y);
                    }
                    else
                    {
                        g.drawString(Integer.toString(turnScore3), scorePop3X, scorePop3Y);
                    }
                    if(scorePop3Y > 0)
                    {
                        scorePop3Y--;
                    }
                    else
                    {
                        scorePop3 = false;
                    }
                }
      
                highScoreCheck();
                
                g.setColor(Color.green.brighter().brighter());
                g.drawOval(X3 - (circleDiameter / 2), Y3 - (circleDiameter / 2), circleDiameter, circleDiameter);                
                dart.paintIcon(this, g, X3 - (dart.getIconWidth() / 2), Y3 - (dart.getIconHeight() / 2));
            }
//            System.out.println("player score = " + playerScore);


            if(aniMode == 2)
            {
                meterBar.paintIcon(this, g, (width * 7 / 8) - 8 * IMAGE_SCALE, (height - meterBar.getIconHeight()) / 2 - 23 * IMAGE_SCALE);
                g.setColor(new Color(255 - powerBarFlow, powerBarFlow, 0, 200).brighter().brighter());
                // 23 == vertical offset
                g.fillRect((width * 7 / 8) + 5 * IMAGE_SCALE, height * 2 / 3 - powerBarFlow * IMAGE_SCALE, 32 * IMAGE_SCALE, powerBarFlow * IMAGE_SCALE); //powerBar
//                System.out.println("animode 2 paint " + ", width = " + width + ", height = " + height + ", powerBarFlow = " + powerBarFlow);
            }
            if(aniMode == 3)  //aimBar
            {   
                aimBar.paintIcon(this, g, (width / 2) - (aimBar.getIconWidth() / 2), height / 8 - (aimBar.getIconHeight() / 2));
                g.setColor(new Color((int)(0 + Math.abs((double)aimBarFlow) * 1.5), (int)(255 - Math.abs((double)aimBarFlow) * 1.5), 0, 200).brighter().brighter());
                g.fillRect(((width / 2) + 3) - ((Math.abs(aimBarFlow * IMAGE_SCALE) - aimBarFlow * IMAGE_SCALE) / 2), (height / 8) - 8 * IMAGE_SCALE, Math.abs(aimBarFlow * IMAGE_SCALE), 18 * IMAGE_SCALE);
            }

            g.setColor(Color.white);
            g.drawOval(xHand + xAim, yHand + yAim - 50, 20, 20);
            
            if (aniMode == 1)
            {
                getHandCoor();
            }

            if (!tutorial)
            {
                if((Math.pow(x - helpIconX, 2) + Math.pow(y - helpIconY, 2)) < (Math.pow(helpIcon.getIconHeight() / 2, 2))) //turns on tutorial/tips
                {
                    helpIcon2.paintIcon(this, g, helpIconX - helpIcon.getIconWidth() / 2, helpIconY - helpIcon.getIconHeight() / 2);
                }
                else
                {
                    helpIcon.paintIcon(this, g, helpIconX - helpIcon.getIconWidth() / 2, helpIconY - helpIcon.getIconHeight() / 2);
                }
            }
                
            hand.paintIcon(this, g, xHand, yHand); //paints animated hand
            
            if (aniMode == 0)
            {
                if (whiteOut < 254)
                {           
                    whiteOut += 2;
                    scoreColor1 -= 2;
                    scoreColor2 -= 2;
                    scoreColor3 -= 2;

                }
                else
                {
                    whiteOut = 255;
                    scoreColor1 = 0;
                    scoreColor2 = 0;
                    scoreColor3 = 0;
                }

                g.setColor(new Color(255,255,255,whiteOut));
                g.fillRect(0, 0, width, height);

                g.setColor(new Color(scoreColor1, scoreColor2, scoreColor3, scoreColor4));
                g.setFont(new Font(Font.SERIF, Font.BOLD+Font.ITALIC, 20 * IMAGE_SCALE));
                if(CheckingAccount.activeBet)
                {   
                    g.drawString(winMessage(), width / 4, height / 4);
                }
                g.drawString("Play Again?", (width / 2) - 50 * IMAGE_SCALE, height / 2);
                
                g.drawString("Top Score: " + CheckingAccount.highestScore + " (" + CheckingAccount.highestScorer + ")", (width / 12), (height * 9 / 10));
                
                if((Math.pow(x - (width / 3), 2) + Math.pow(y - (height / 2), 2)) < Math.pow(restartIcon.getIconWidth() / 2, 2))
                {
                    restartIcon2.paintIcon(this, g, (width / 3) - (restartIcon2.getIconWidth() / 2), (height / 2) - (restartIcon.getIconHeight() / 2));                   
                }
                else
                {
                    restartIcon.paintIcon(this, g, (width / 3) - (restartIcon.getIconWidth() / 2), (height / 2) - (restartIcon.getIconHeight() / 2));
                }
                
                if(Math.pow(x - (width * 2 / 3), 2) + Math.pow(y - (height / 2) , 2) < Math.pow(quitIcon.getIconWidth() / 2, 2))
                {
                    quitIcon2.paintIcon(this, g, (width * 2 / 3) - (quitIcon2.getIconWidth() / 2), (height / 2) - (quitIcon2.getIconHeight() / 2));
                }
                else
                {
                    quitIcon.paintIcon(this, g, (width * 2 / 3) - (quitIcon.getIconWidth() / 2), (height / 2) - (quitIcon.getIconHeight() / 2));

                }
                
            }
            
            g.setColor(new Color(scoreColor1, scoreColor2, scoreColor3, scoreColor4));
            g.setFont(new Font("Serif", Font.BOLD+Font.ITALIC, 30 * IMAGE_SCALE));
            g.drawString("Score: " + playerScore, scoreBoardX, scoreBoardY);
            g.drawString("High Score: " + highScore, highScoreBoardX, highScoreBoardY);
            
            if(tutorial1)
            {
                tutorial1PopUp.paintIcon(null, g, width / 100 , height * 3 / 5);               
            }
            if(tutorial2)
            {
                tutorial2PopUp.paintIcon(null, g, width / 100, height * 3 / 5);
            }
            if(tutorial3)
            {
                tutorial3PopUp.paintIcon(null, g, width / 100, height * 3 / 5);
            }
            

        }

    }
    
    private void getHandCoor()
    {
        xHand = (x - (getImage(handArrStrs[0]).getIconWidth() / 2) + driftX * IMAGE_SCALE);
        yHand = (y - (getImage(handArrStrs[0]).getIconHeight() / 2) + driftY * IMAGE_SCALE);
    }
    
    private class TimerListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            circularPath();
            circleDiameterPing();
            
            if(aniMode == 1)
            {
                aimCircleCoor();
                if(tutorial)
                {
                    tutorial1 = true;
                }
            }

            if (aniMode < 4 && aniMode > 1)
            {
                if(aniMode == 2)
                {
                    powerBar();
                    if(tutorial)
                    {
                        tutorial1 = false;
                        tutorial2 = true;
                    }
                }
                if(aniMode == 3)
                {    
                    aimBar();
                    if(tutorial)
                    {
                        tutorial2 = false;
                        tutorial3 = true;
                    }
                }
                
            }
            
            repaint();       
        }
    }
    
    private class PlayerListener implements MouseListener, 
                                            MouseMotionListener, 
                                            ActionListener
    {
        public void mousePressed(MouseEvent event)
        {
//            mouse click after release
        }
        
        public void mouseClicked(MouseEvent event)
        {
            if(!endGame)
            {

                if((Math.pow(x - (width / 32), 2) + Math.pow(y - (height * 9 / 12), 2)) < Math.pow(helpIcon.getIconWidth(), 2)) //turns on tutorial/tips
                {
                    tutorial = true;
                }
                else
                {
                    if(aniMode == 1) //switch to throw animation
                    {
                        aniMode = 2;
                        handArrIndex = 0;
                        DELAY = 10;

                        handReady();

                        timer.setDelay(DELAY);                
                    }
                    else if (aniMode == 2)
                    {
                        aniMode = 3;
                        lockBar = true;
                    }
                    else if (aniMode == 3)
                    {
                        dartPosition();
                        aniMode = 4;
                    }
                    else if (aniMode == 4)
                    {
                        aniMode = 1;
                        DELAY = 120;
                        handArrIndex = 0;
                        timer.setDelay(DELAY);
                        timer.start();
                        tutorialOff();
                    }                    
                }
                

            }
            else
            {
                timer.stop();
                aniMode = 0;
                                
                if(endGameFirstPass)
                {
                    endGameFirstPass = false;
                    x = 0;
                    y = 0;
                    gameOverSongSnd.play();
                    if (CheckingAccount.activeBet)
                    {
                        CheckingAccount.depositWin();
                    }
                    CheckingAccount.checkTopScore();
                }    

                if((Math.pow(x - (width / 3), 2) + Math.pow(y - (height / 2), 2)) < Math.pow(restartIcon.getIconWidth() / 2, 2))
                {
                    DartsLauncher.dartFrame.dispose();
                    CheckingAccount.startGame();
                }
                if(Math.pow(x - (width * 2 / 3), 2) + Math.pow(y - (height / 2) , 2) < Math.pow(quitIcon.getIconWidth() / 2, 2))
                {
                    gameQuit();
                }
                

            }
        }
        
        public void actionPerformed(ActionEvent event)
        {

            if(aniMode == 1) //default animation
            {
               handIdle();
            }
            else if(aniMode == 4) //throw animation
            {            
                handThrow();                
            }
            else {}
            
            repaint();
        }
        
        public void mouseDragged(MouseEvent event){}
        public void mouseReleased(MouseEvent event)
        {
            //detected click release
        }
        public void mouseEntered(MouseEvent event){}
        public void mouseExited(MouseEvent event){}
        public void mouseMoved(MouseEvent event)
        {
            x = event.getX();
            y = event.getY();
            imageResize = true;
        }
    }

    // Moves through animation array
    private void handIdle()
    {
        lockBar = false;
        hand = handArr[handIndexSequence[handArrIndex]];
        handArrIndex = (handArrIndex + 1) % 11;
    }
    
    private void handReady()
    {
        hand = handArr[1];
    }
    
    private void handThrow()
    {
        hand = handArr[handArrIndex];
        if(handArrIndex < 5)
            handArrIndex++;
        else if(handArrIndex == 5)
        {
            handArrIndex = 6;
            if(!(attempt1))
            {
                attempt1 = true;
            }
            else if(!(attempt2))
            {
                attempt2 = true;
            }
            else if(!(attempt3))
            {
                attempt3 = true;
            }
        }
        else if(handArrIndex == 6)
        {
            timer.stop();
        }
        else{}
    }
    
    public void drift()
    {
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
    private void dartPosition()
    {
        power = powerBarFlow;

        if (power == 0)
            power = 1;
        
        try
        {
            dartY = yAim + gravity * (distance / power) * (distance / power);
        }
        catch (ArithmeticException e)
        {
            System.out.println(e);
            dartY = yAim + gravity * distance * distance;
        }
        
        dartX = deviation * (distance / power) - 25 * IMAGE_SCALE;
    }

    private void powerBar()
    {
        if(aniMode == 2)
        {
            if(!(lockBar))
            {
                if (dirPBF == 1)
                {
                    powerBarFlow += powerBarFactor++;

                    if(powerBarFlow > 100)
                    {
                        dirPBF = -1;
                        powerBarFactor = 12;
                        powerBarFlow = 155;
                    }
                }
                else
                {
                    powerBarFlow += powerBarFactor--;

                    if(powerBarFlow < 1)
                    {
                        dirPBF = 1;
                        powerBarFactor = 0;
                        powerBarFlow = 0;
                    }
                }
            }
        }
    }
    private void aimBar()
    {
        if (aniMode == 3)
        {
            if(!(lockAimBar))
            {
                if(dirABF == 1)
                {
    //                if (aimBarFlow < -155)
                    aimBarFlow += aimBarFactor++;

                    if(aimBarFlow > 150)
                    {
                        dirABF = -1;
                        aimBarFactor = 5;
                        aimBarFlow = 150;
                    }
                }
                else
                {
                    aimBarFlow += aimBarFactor--;

                    if(aimBarFlow < -150)
                    {
                        dirABF = 1;
                        aimBarFactor = -5;
                        aimBarFlow = -150;
                    }
                }
                deviation = aimBarFlow * IMAGE_SCALE / 5;
            }
        }
    }
    private Clip getAudioClip(String path)
    {
        try
        {
            input = new File(path);
            System.out.println("file exists? " + input.exists() + " file executable? " + input.canExecute());
            sound = AudioSystem.getAudioInputStream(input.getAbsoluteFile());
            info = new DataLine.Info(Clip.class, sound.getFormat());
            clip = (Clip) AudioSystem.getLine(info);
        }
        catch (IOException | UnsupportedAudioFileException | LineUnavailableException e)
        {
            System.out.println(e);
            System.exit(1);
        }
        return clip;
    }
    
    private AudioClip createAudioClip(String path)
    {
        try
        {
            return Applet.newAudioClip(new URL(path));
        }
        catch (MalformedURLException murle)
        {
            murle.printStackTrace();
        }
        return null;
    }
    
    void aimCircleCoor()
    {
        if(dirAimCircle == -1)
        {
            yAim -= yAimFactor * IMAGE_SCALE;
            if(yAim < -circleAimMax)
            {
                dirAimCircle = 1;
                yAim = -circleAimMax;
            }
        }
        else 
        {
            yAim += yAimFactor * IMAGE_SCALE;
            if(yAim > circleAimMax)
            {
                dirAimCircle = -1;
                yAim = circleAimMax;
            }   
        }
        xAim = -75;

    }
    
    private void circleDiameterPing()
    {
        pingSpeedMod++;
        if(pingSpeedMod % 1 == 0)
        {
            circleDiameter--;
            if (circleDiameter == 0)
            circleDiameter = 20;
        }
    }
    private void circularPath()
    {
        if (xCirPos > cirRadius)
            mod = -1;
        else if (xCirPos < -cirRadius)
            mod = 1;

        //in progress
        int yCirPos = (int) Math.sqrt(Math.pow(cirRadius, 2) - Math.pow(xCirPos, 2)) * mod; // TODO: does not change. sqrt does not need to be recalculated every time
        xCirPos += (1 * mod);
    }
    
    private void scoreFind(int xIn, int yIn)
    {
        int distance = xIn*xIn + yIn*yIn ;
        int radius = (board.getIconWidth() / 2) * (board.getIconWidth() / 2);
        if ( distance > radius)
        {
            if(distance > (radius * 10))
            {
                System.out.println(1);
                terribleSnd.play();
            }
            else
            {
                System.out.println(2);
                dartTinnyImpactSnd.play();
//                wallImpact.start();
//                if (!(wallImpact.isRunning()))
//                {
//                    wallImpact.stop();
//                }
            }
            turnScore = 0;
            scoreMultiplier = 0;
        }
        else
        {
            dartWoodenImpactSnd.play();
            if (distance > 2*2*IMAGE_SCALE*IMAGE_SCALE)
            {
                if (distance > 5*5*IMAGE_SCALE*IMAGE_SCALE)
                {
                    if(distance > 35*35*IMAGE_SCALE*IMAGE_SCALE)
                    {
                        if(distance > 38*38*IMAGE_SCALE*IMAGE_SCALE)
                        {
                            if(distance > 56*56*IMAGE_SCALE*IMAGE_SCALE)
                            {
                                if (distance > 59*59*IMAGE_SCALE*IMAGE_SCALE)
                                {
//                                    System.out.println("outside rim");
                                    scoreMultiplier = 0;
                                    turnScore = 0;
                                    return;
                                }
//                                System.out.println("double ring");
                                scoreMultiplier = 2;
                                        playerScore += scoreMultiplierFind(xIn, yIn);

                                return;
                            }
//                            System.out.println("between double ring and triple ring");
                            scoreMultiplier = 1;
                                    playerScore += scoreMultiplierFind(xIn, yIn);

                            return;
                        }
//                        System.out.println("triple ring");
                        scoreMultiplier = 3;
                                playerScore += scoreMultiplierFind(xIn, yIn);

                        return;
                    }
//                    System.out.println("between outer bullseye and triple ring");
                    scoreMultiplier = 1;
                            playerScore += scoreMultiplierFind(xIn, yIn);

                    return;
                }
//                System.out.println("outer bullseye ring");
                scoreMultiplier = 1;
                turnScore = 25;
                playerScore += turnScore;
                return;
            }
//            System.out.println("BULLSEYE");
            scoreMultiplier = 1;
            turnScore = 50;
            playerScore += turnScore;
            return;
        }
        return;
    }
    private static double angleFind(double xIn, double yIn)
    {
        return Math.toDegrees(Math.atan2(yIn, xIn)); // y , x
    }
    private static int scoreMultiplierFind(int xIn, int yIn)
    {
        sideY = yIn - (height / 2);
        sideX = xIn - (width / 2);
        if (-yIn > 0)
        {
            if(xIn > 0)
            {
//                System.out.println("Quandrant IV");
                if(angle <= -81)
                {
//                    System.out.println("hit 3");
                    turnScore = 3;
                }
                else if(angle <= -63)
                {
//                    System.out.println("hit 17");
                    turnScore = 17;
                }
                else if(angle <= -45)
                {
//                    System.out.println("hit 2");
                    turnScore = 2;
                }
                else if(angle <= -27)
                {
//                    System.out.println("hit 15");
                    turnScore = 15;
                }                
                else if(angle <= -9)
                {
//                    System.out.println("hit 10");
                    turnScore = 10;
                }
                else
                {
//                    System.out.println("hit 6");
                    turnScore = 6;

                }
            }
            else
            {
//                System.out.println("Quandrant III");
                if(angle <= -171)
                {
//                    System.out.println("hit 11");
                    turnScore = 11;
                }
                else if(angle <= -153)
                {
//                    System.out.println("hit 8");
                    turnScore = 8;
                }
                else if(angle <= -135)
                {
//                    System.out.println("hit 16");
                    turnScore = 16;
                }
                else if(angle <= -117)
                {
//                    System.out.println("hit 7");
                    turnScore = 7;
                }
                else if(angle <= -99)
                {
//                    System.out.println("hit 19");
                    turnScore = 19;
                }
                else
                {
//                    System.out.println("hit 3");
                    turnScore = 3;
                }
            }
        }
        else
        {
            if(xIn > 0)
            {
                if(angle >= 81)
                {
//                    System.out.println("hit 20");
                    turnScore = 20;
                }
                else if(angle >= 63)
                {
//                    System.out.println("hit 1");                    
                    turnScore = 1;
                }   
                else if(angle >= 45)
                {
//                    System.out.println("hit 18");
                    turnScore = 18;
                }
                else if(angle >= 27)
                {
//                    System.out.println("hit 4");
                    turnScore = 4;
                }
                else if(angle >= 9)
                {
//                    System.out.println("hit 13");
                    turnScore = 13;
                }
                else
                {
//                    System.out.println("hit 6");
                    turnScore = 6;
                }
            }
            else
            {
//                System.out.println("Quandrant II");
                if(angle >= 171)
                {
//                    System.out.println("hit 11");
                    turnScore = 11;
                }
                else if(angle >= 153)
                {
//                    System.out.println("hit 14");
                    turnScore = 14;
                }
                else if(angle >= 135)
                {
//                    System.out.println("hit 9");
                    turnScore = 9;
                }
                else if(angle >= 117)
                {
//                    System.out.println("hit 12");
                    turnScore = 12;
                }
                else if(angle >= 99)
                {
//                    System.out.println("hit 5");
                    turnScore = 5;
                }
                else
                {
//                    System.out.println("20");
                    turnScore = 20;
                }
            }
        }
                
        return turnScore * scoreMultiplier;
    }
    
    private void gameReset()
    {
        System.out.println("resetting values");
        attempt1 = false;
        attempt2 = false;
        attempt3 = false;
        attempt1F = false;
        attempt2F = false;
        attempt3F = false;
        scorePop1 = false;
        scorePop2 = false;
        scorePop3 = false;
        playerScore = 0;
        DELAY = 120;
        handArrIndex = 0;
        timer.setDelay(DELAY);
        endGame = false;
        timer.start();
        aniMode = 1;
        timerFlow.start();
        whiteOut = 0;
        endGameFirstPass = true;
        scoreColor1 = 255;
        scoreColor2 = 255;
        scoreColor3 = 255;
    }
    private void gameQuit()
    {
        DartsLauncher.dartFrame.dispose();
//        Main.frame.setVisible(true);
        System.exit(0);
    }
    
    private String winMessage()
    {
        String message = new String("");
        String scoreStr = CheckingAccount.fmt.format(CheckingAccount.calculateWin(playerScore));
        message += ("You won " + scoreStr + "!");
        return message;
        
    }
    
    public static double getPlayerScore()
    {
        return playerScore;
    }
    
    private Clip setAudio(final String path) {
        try {
    //        stopPlay();
            InputStream is = new FileInputStream(path);
            InputStream bufferedIn = new BufferedInputStream(is);
            AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn);
            AudioFormat format = ais.getFormat();
            // this is the value of format.
            // PCM_SIGNED 44100.0 Hz, 16 bit, stereo, 4 bytes/frame, little-endian
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            clip = (Clip)AudioSystem.getLine(info);
            clip.open(ais);
    //        clip.start();
        } catch (Exception e) {
    //        stopPlay();
                e.printStackTrace();
        }
        
        return clip;
    }
    
    public void highScoreCheck()
    {
        if (playerScore > highScore)
        {
            highScore = playerScore;
        }
        else
        {
//            highScore = CheckingAccount.acct1.highScore;
        }
    }
    
    public void tutorialOff()
    {
        tutorial = false;
        tutorial1 = false;
        tutorial2 = false;
        tutorial3 = false;
//        CheckingAccount.acct1.tutorialState = false;
    }
}
