package JustDarts.components;

import JustDarts.interfaces.Drawable;
import JustDarts.managers.InputManager;

import java.awt.*;

public class Button extends Drawable {

    public Button() {
        inputManager = InputManager.getInstance();
    }
    @Override
    protected void init() {
    }

    @Override
    public void update() {
        if (isSubjectInsideButton(inputManager.getMousePos())) {
            currentFrame = 1;
        }
        else currentFrame = 0;
    }

    @Override
    public void setPosition(Point pos) {
        super.setPosition(pos.x - getWidth() / 2, pos.y - getHeight() / 2);
    }

    public void setPosition(int newPosX, int newPosY) {
        super.setPosition(newPosX - getWidth() / 2, newPosY - getHeight() / 2);
    }

    protected int getButtonWidth() {
        return 75;
    }

    protected  int getButtonHeight() {
        return 75;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    private double calculateSquaredDistanceFromCenterOfButton(Point subjectPos) {
        return Math.pow(subjectPos.x - getPosX() - getWidth() / 2, 2)
                + Math.pow(subjectPos.y - getPosY() - getHeight() / 2, 2);
    }

    private double calculateSquaredRadiusOfButton() {
        return Math.pow(getWidth() / 2, 2);
    }

    protected boolean isSubjectInsideButton(Point subjectPos) {
        return calculateSquaredRadiusOfButton() > calculateSquaredDistanceFromCenterOfButton(subjectPos);
    }

    public boolean isActive() {
        return currentFrame == 1;
    }
}
