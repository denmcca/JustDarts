package components;

import interfaces.Drawable;
import interfaces.GameObject;
import managers.InputManager;

import java.awt.*;
import java.util.ArrayList;

public class Button extends Drawable {
    private ArrayList<GameObject> subjects;

    public Button() {
        subjects = new ArrayList<>();
    }

    @Override
    protected void init() {
    }

    @Override
    public void update() {
        subjects.forEach(subject -> {
            if (isSubjectInsideButton(subject)) {
                currentFrame = 1;
            } else currentFrame = 0;
        });
    }

    @Override
    public void setPosition(Point pos) {
        super.setPosition(pos.x - getWidth() / 2, pos.y - getHeight() / 2);
    }

    public void setPosition(int newPosX, int newPosY) {
        super.setPosition(newPosX - getWidth() / 2, newPosY - getHeight() / 2);
    }

    public void addSubject(GameObject subject) {
        this.subjects.add(subject);
    }
    int getButtonWidth() {
        return 75;
    }

    int getButtonHeight() {
        return 75;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    private double calculateSquaredDistanceFromCenterOfButton(Point subjectPos) {
        return Math.pow(subjectPos.x - getPosX() - getWidth() / 2f, 2f)
                + Math.pow(subjectPos.y - getPosY() - getHeight() / 2f, 2f);
    }

    private double calculateSquaredRadiusOfButton() {
        return Math.pow(getWidth() / 2f, 2f);
    }

    private boolean isSubjectInsideButton(GameObject subject) {
        return calculateSquaredRadiusOfButton() > calculateSquaredDistanceFromCenterOfButton(subject.getPosition());
    }

    public boolean isActive() {
        return currentFrame == 1;
    }
}
