/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JustDarts;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class Main{
      
    public static CheckOptionsPanel frame;
    public static JTextArea ta;
    public static String textAreaMsg;
    private static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private static Dimension baseDim = new Dimension(640, 480);
    private static int baseFontSize = 12;

    public static final int WIDTH = dim.width / 2, HEIGHT = (int)((float)WIDTH * (float)baseDim.height / (float)baseDim.width);
    public static final int FONT_SIZE = baseFontSize + baseFontSize * WIDTH / dim.width;
    public static final Font FONT = new Font("Courier New", Font.PLAIN, FONT_SIZE);
    public static final Dimension WINDOW_LOCATION = new Dimension((dim.width - WIDTH) / 2, (dim.height - HEIGHT) / 2);


    public static void main(String[] args) throws IOException
    {
//        Insets textAreaPad = new Insets(3,3,3,0);
//        String mainTitle = "Darts.Game";
//        CheckingAccount.dataStore = new Vector<>();
//        textAreaMsg = "Welcome to the Game of Darts";
//        frame = new CheckOptionsPanel(mainTitle, new Dimension(WIDTH, HEIGHT));
//        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//
//        ta = new JTextArea(textAreaMsg);
//        ta.setEditable(false);
//        ta.setBackground(Color.WHITE);
//        ta.setMargin(textAreaPad);
//        ta.setFont(FONT);
//        JScrollPane scrollPane = new JScrollPane(ta);
//        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
//        frame.getContentPane().add(scrollPane);
//        frame.pack();
//        frame.setAlwaysOnTop(true);
////        frame.setSize(WIDTH, HEIGHT);
//        frame.setLocation(WINDOW_LOCATION.width, WINDOW_LOCATION.height);
//        frame.setVisible(true);
        DartsLauncher.dartsMain();
    }
    
}