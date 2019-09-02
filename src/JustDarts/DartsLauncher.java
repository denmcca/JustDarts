/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JustDarts;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import javax.swing.*;

/**
 *
 * @author Dennis
 */
class DartsLauncher {
    /**
    / * @param args the command line arguments
    / */
    void dartsMain() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame dartFrame;
        Toolkit t = Toolkit.getDefaultToolkit();
        Image i = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Cursor noCursor = t.createCustomCursor(i, new Point(0, 0), "none");
//        int IMAGE_SCALE = Main.WIDTH / 640;
        int windowWidth = Main.WIDTH, windowHeight = Main.HEIGHT;
//        int scaledWindowHeight = (int)((float)Main.HEIGHT * 640f / (float)Main.WIDTH);
        int windowPositionX = Main.WINDOW_LOCATION.width, windowPositionY = Main.WINDOW_LOCATION.height;
        dartFrame = new JFrame("Darts");

        Path path = FileSystems.getDefault().getPath("img","dart.png");
        dartFrame.setIconImage(new ImageIcon(getClass().getResource("img/dart.png")).getImage());
//        File file = new File("img/dart.png");
        System.out.println(path.toString());
        dartFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dartFrame.setAlwaysOnTop(false);
        dartFrame.setPreferredSize(new Dimension(windowWidth, windowHeight));
        dartFrame.setResizable(false);
        dartFrame.setSize(windowWidth, windowHeight);
        dartFrame.setCursor(noCursor);
//        dartFrame.setCursor(Cursor.CROSSHAIR_CURSOR);
        dartFrame.setLocation(windowPositionX, windowPositionY);
//        System.out.print("width = " + (dartFrame.getWidth()) / 2 + " height = " + (Main.dim.height - dartFrame.getHeight()) / 2);
        DartsPanel dartsPanel = new DartsPanel();
        dartsPanel.setFocusable(true);
        dartsPanel.requestFocusInWindow();
        dartFrame.getContentPane().add(dartsPanel);
//        dartFrame.pack();
        dartFrame.setVisible(true);
    }
}
