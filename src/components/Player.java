package components;

import interfaces.GameObject;
import managers.InputManager;

public class Player extends GameObject {
    private InputManager inputManager;
    public Player() { }
    @Override
    protected void init() {

    }

    @Override
    public void update() {
        setPosition(inputManager.getMousePos());
    }

    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }
}
