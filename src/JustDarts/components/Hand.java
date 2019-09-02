package JustDarts.components;

import JustDarts.interfaces.Drawable;
import JustDarts.managers.GameObjectManager;
import JustDarts.managers.InputManager;
import JustDarts.managers.TimeManager;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Hand extends Drawable {
    private Boolean isPosLocked = false;
    public enum AnimationSequence {IDLE, HOLD, THROWING, THROWN;}
    private AnimationSequence aSeq;
    private InputManager inputManager;

    public Hand() {
        imgBaseDim = new Dimension(44 * SCALE, 66 * SCALE);
        frameIndexSequence.addAll(Arrays.asList(0,1,2,3,4,5,4,3,2,1,0,6));
        prepareFramesList(new String[]{
                "hand1.png", "hand2.png", "hand3.png", "hand4.png",
                "hand5.png", "hand6.png", "hand7.png"
        });
        init();
    }

    protected void init() {
        aSeq = AnimationSequence.IDLE;
        setPosX(100);
        setPosY(100);
    }

    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public void update() {
        setPosition(inputManager.getMousePos());
        frameDelay += TimeManager.getInstance().getDeltaTime() / 1000f;
//        System.out.println(frameDelay);
        switch (aSeq) {
            case IDLE:
                if (frameDelay > 1/10f) {
                    currentFrame = (currentFrame + 1) % 11;
                    frameDelay -= 1/10f;
                }
                break;
            case HOLD:
                currentFrame = 0;
                break;
            case THROWING:
                if (currentFrame < 5 & frameDelay >= 1/10f) {
                    ++currentFrame;
                    frameDelay -= 1/10f;
                }
                else aSeq = AnimationSequence.THROWN;
                break;
            case THROWN:
                currentFrame = 11;
                break;
            default:
                System.out.printf("No proper state detected: %s", this);
        }
    }

    @Override
    public void reset() {
        init();
    }

    public Dart throwDart(Point dartPos, String dartName) {
        Dart dart = new Dart();
        dart.setName(dartName);
        dart.setPosition(dartPos);
        return dart;
    }

    @Override
    public void setPosition(Point pos){
        if (!isPosLocked) super.setPosition(pos);
    }

    @Override
    public void setPosX(int posX){
//        System.out.printf("isPosLocked: %b", isPosLocked);
        if (!isPosLocked) super.setPosX(posX);
    }

    @Override
    public void setPosY(int posY){
        if (!isPosLocked) super.setPosY(posY);
    }

    public Boolean toggleLock() {
        return isPosLocked = !isPosLocked;
    }

    public void changeAnimationSequence(AnimationSequence seq) {
        aSeq = seq;
    }

    public AnimationSequence getAnimationSequence() {
        return aSeq;
    }

}
