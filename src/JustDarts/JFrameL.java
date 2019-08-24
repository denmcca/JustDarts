/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JustDarts;

/**
 *
 * @author Dennis
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class JFrameL extends JFrame implements WindowListener
{
    
    /** Creates a new instance of JFrameL */
    public JFrameL(String title) {
        super(title);
        addWindowListener(this);
    }
    public void windowClosing(WindowEvent e) 
    {
        Main.frame.setVisible(false);
        int option = 0;
        if (CheckingAccount.saveFlag == true)
        {
            System.exit(0);
        }
        else
        {
            option = JOptionPane.showConfirmDialog(null, "Changes have "
                    + "been made since the last save. Would you like to save "
                    + "now?");
            if (0 == option) //if yes (1 = no, 2 = cancel, -1 = closewindow)
            {
                CheckingAccount.saveFile();
                System.exit(0);
            }
            else if(option != 2)
            {
                System.exit(0);
            }
            else
            {
                Main.frame.setVisible(true);//User has cancelled close
            }
        }
    }

    public void windowClosed(WindowEvent e) {    }

    public void windowOpened(WindowEvent e) {    }

    public void windowIconified(WindowEvent e) {    }

    public void windowDeiconified(WindowEvent e) {    }

    public void windowActivated(WindowEvent e) {    }

    public void windowDeactivated(WindowEvent e) {    }    
}
