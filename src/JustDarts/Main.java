/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JustDarts;

import java.applet.Applet;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class Main extends Applet {
      
//    public static CheckOptionsPanel frame;
    public static JTextArea ta;
    public static String textAreaMsg;
    private static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private static Dimension baseDim = new Dimension(640, 480);
    private static int baseFontSize = 12;

    public static final int WIDTH = dim.width / 2, HEIGHT = WIDTH * baseDim.height / baseDim.width;
    public static final int FONT_SIZE = baseFontSize + baseFontSize * WIDTH / dim.width;
    public static final Font FONT = new Font("Courier New", Font.PLAIN, FONT_SIZE);
    public static final Dimension WINDOW_LOCATION = new Dimension((dim.width - WIDTH) / 2, (dim.height - HEIGHT) / 2);


    public static void main(String[] args)
    {
        System.out.println(WIDTH + " " + HEIGHT);
        DartsLauncher.dartsMain();
    }
    
}