package components;

import interfaces.Animated;
import java.awt.*;
import java.util.Arrays;

public class Hand extends Animated {
    public enum AnimationSequence { IDLE, HOLD, THROWING, THROWN }
    private AnimationSequence aSeq;
    private Player player;
    private final int THROWING_FPS = 21;
    private final int IDLE_FPS = 7;

    public Hand() {
        imgBaseDim = new Dimension(88, 132);
        frameIndexSequence.addAll(Arrays.asList(0,1,2,3,4,5,4,3,2,1,0,6));
        prepareFramesList(new String[]{
                "hand1.png", "hand2.png", "hand3.png", "hand4.png",
                "hand5.png", "hand6.png", "hand7.png"
        });
        setFrameRate(IDLE_FPS);
        init();
    }

    @Override
    protected void init() {
        aSeq = AnimationSequence.IDLE;
        setPosX(WINDOW_SIZE.width * 2 / 3);
        setPosY(WINDOW_SIZE.height * 2 / 3);
    }

    // make a controllable class or use get instance?
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void update() {
        // updates should animate boolean only when throwing and idle state are active to save cpu-time
        if (aSeq == AnimationSequence.THROWING | aSeq == AnimationSequence.IDLE) {
            super.update();
        }

        // depending on state of hand animation, specific indices are assigned to currentFrame
        switch (aSeq) {
            case IDLE:
                // updates hand position
                setPosition(player.getPosition());
                if (shouldAnimate) {
                    currentFrame = (currentFrame + 1) % 11;
                }
                break;
            case HOLD:
                if (currentFrame != 0) {
                    currentFrame = 0;
                }
                break;
            case THROWING:
                if (shouldAnimate) {
                    if (currentFrame < 5) {
                        setFrameRate(THROWING_FPS);
                        ++currentFrame;
                    }
                    else {
                        aSeq = AnimationSequence.THROWN;
                        setFrameRate(IDLE_FPS);
                    }
                }
                break;
            case THROWN:
                if (currentFrame != 11) {
                    currentFrame = 11;
                }
                break;
            default:
                System.out.printf("No proper state detected: %s", this);
        }
    }

    // resets values to default
    @Override
    public void reset() {
        init();
    }

    // create dart game object, assign its position, and return (ideally to game object manager)
    public Dart throwDart(Point dartPos, String dartName) {
        Dart dart = new Dart();
        dart.setName(dartName);
        dart.setPosition(dartPos);
        return dart;
    }

    // changes state of hand animation
    public void changeAnimationSequence(AnimationSequence seq) {
        aSeq = seq;
    }

    // returns current state of hand animation
    public AnimationSequence getAnimationSequence() {
        return aSeq;
    }
}
