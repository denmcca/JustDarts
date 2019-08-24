/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JustDarts;
import java.awt.*;
import java.net.MalformedURLException;
import javax.swing.*;

/**
 *
 * @author Dennis
 */
class DartsLauncher {
    static JFrame dartFrame;
    //    static Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(null, null, "./img/circle.png");
    
    /**
    / * @param args the command line arguments
    / */
    static void dartsMain() {
//        int IMAGE_SCALE = Main.WIDTH / 640;
        int windowWidth = Main.WIDTH, windowHeight = Main.HEIGHT;
//        int scaledWindowHeight = (int)((float)Main.HEIGHT * 640f / (float)Main.WIDTH);
        int windowPositionX = Main.WINDOW_LOCATION.width, windowPositionY = Main.WINDOW_LOCATION.height;
        dartFrame = new JFrame("Darts");
        dartFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        dartFrame.setAlwaysOnTop(true);
        dartFrame.setPreferredSize(new Dimension(windowWidth, windowHeight));
        dartFrame.setResizable(false);
        dartFrame.setSize(windowWidth, windowHeight);
//        dartFrame.setCursor(blank);
        dartFrame.setLocation(windowPositionX, windowPositionY);
//        System.out.print("width = " + (dartFrame.getWidth()) / 2 + " height = " + (Main.dim.height - dartFrame.getHeight()) / 2);
        dartFrame.getContentPane().add(new DartsPanel());
//        dartFrame.pack();
        dartFrame.setVisible(true);
    }
}
