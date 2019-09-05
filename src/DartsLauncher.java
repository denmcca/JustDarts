/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Dennis
 */
class DartsLauncher extends JFrame{
    /**
    / * @param args the command line arguments
    / */
    DartsLauncher(String title) {
        int windowWidth = Main.WIDTH, windowHeight = Main.HEIGHT;
        int windowPositionX = Main.WINDOW_LOCATION.width, windowPositionY = Main.WINDOW_LOCATION.height;

        // behavior
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setAlwaysOnTop(false);
        setResizable(false);

        // dimensions, location, aesthetic
        setTitle(title);
        setIconImage(new ImageIcon(getClass().getResource("resources/images/dart.png")).getImage());
        setPreferredSize(new Dimension(windowWidth, windowHeight));
        setSize(windowWidth, windowHeight);
        setLocation(windowPositionX, windowPositionY);

        // panel
        DartsPanel dartsPanel = new DartsPanel();
        dartsPanel.setFocusable(true);
        dartsPanel.requestFocusInWindow();
        getContentPane().add(dartsPanel);

        // show frame and panel
        setVisible(true);
    }
}
