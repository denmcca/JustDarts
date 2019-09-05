package managers;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class InputManager {
    // inline classes moved to bottom of class code
    private static InputManager instance;
    private InputManager() {
        init();
    }
    // maps keys that have been inquired during run-time
    private HashMap<Character, Key> keys;
    // maps mouse buttons that have been inquired during run-time
    private HashMap<Integer, MouseButton> mButtons;
    private Point mousePos;
    // initialize input listener to catch mouse and keyboard inputs
    public InputListener inputListener;

    public static InputManager getInstance() {
        return instance == null ? instance = new InputManager() : instance;
    }

    private void init() {
        keys = new HashMap<>();
        mButtons = new HashMap<>();
        mousePos = new Point();
        inputListener = new InputListener();
    }

    // returns true if given key is currently pressed down
    public Boolean isKeyDown(Character keyChar) {
        verifyKeyNotNull(keyChar);
        return keys.get(keyChar).isPressed;
    }

    // returns true if given key is currently down, but prev not down
    public Boolean wasKeyJustPressed(Character keyChar) {
        verifyKeyNotNull(keyChar);
        return !keys.get(keyChar).wasPressed && keys.get(keyChar).isPressed;
    }

    // returns true if given key is currently not down, but prev was down
    public Boolean wasKeyJustReleased(Character keyChar) {
        verifyKeyNotNull(keyChar);
        return keys.get(keyChar).wasPressed && !keys.get(keyChar).isPressed;
    }

    public Boolean isButtonDown(Integer buttonNum) {
//        System.out.println("isButtonDown: " + buttonNum);
        verifyMouseButtonNotNull(buttonNum);
        return mButtons.get(buttonNum).isPressed;
    }

    public Boolean wasButtonJustPressed(Integer buttonNum) {
//        System.out.println("wasButtonJustPressed: " + buttonNum);
        verifyMouseButtonNotNull(buttonNum);
        return !mButtons.get(buttonNum).wasPressed && mButtons.get(buttonNum).isPressed;
    }

    public Boolean wasButtonJustReleased(Integer buttonNum) {
//        System.out.println("wasButtonJustReleased: " + buttonNum);
        verifyMouseButtonNotNull(buttonNum);
        return mButtons.get(buttonNum).wasPressed && !mButtons.get(buttonNum).isPressed;
    }

    public Point getMousePos() {
        return mousePos;
    }

    // assures that key is in key hash-map
    private void verifyKeyNotNull(Character keyChar) {
        if (keys.get(keyChar) == null)
            keys.put(keyChar, new Key());
    }

    // assures that mouse button is in mouse button hash-map
    private void verifyMouseButtonNotNull(Integer button) {
        if (mButtons.get(button) == null)
            mButtons.put(button, new MouseButton());
    }

    // run at end of each instruction cycle
    public void update() {
        // updates wasPressed value to isPressed value at beginning of each update
        keys.forEach((k, key) -> {
            if (key.wasPressed != key.isPressed)
                key.wasPressed = key.isPressed;
        });
        // updates wasPressed value to isPressed value at beginning of each update
        mButtons.forEach((k, button) -> {
            if (button.wasPressed != button.isPressed) {
                button.wasPressed = button.isPressed;
            }
        });
    }

    //
    public class InputListener implements MouseListener, MouseMotionListener, ActionListener, KeyListener {
        @Override
        public void actionPerformed(ActionEvent e) { }

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            verifyKeyNotNull(e.getKeyChar());
            keys.get(e.getKeyChar()).isPressed = true;
        }

        @Override
        public void keyReleased(KeyEvent e) {
            verifyKeyNotNull(e.getKeyChar());
            keys.get(e.getKeyChar()).isPressed = false;
        }

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {
            verifyMouseButtonNotNull(e.getButton());
            mButtons.get(e.getButton()).isPressed = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            verifyMouseButtonNotNull(e.getButton());
            mButtons.get(e.getButton()).isPressed = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mousePos.x = e.getPoint().x;
            mousePos.y = e.getPoint().y;
        }
    }

    private class Key {
        boolean isPressed = false;
        boolean wasPressed = false;
        //float: lastTimeAccessed ( can be used to delete non active keys from hash-map )
    }
    private class MouseButton {
        boolean isPressed = false;
        boolean wasPressed = false;
    }
}

