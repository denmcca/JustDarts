package interfaces;

import managers.TimeManager;

public class Animated extends Drawable {
    private float frameTimeDelta;
    private float frameDelay;
    protected boolean shouldAnimate;

    protected Animated() {
        frameTimeDelta = 0f;
        setFrameRate(60);
    }

    // allows animation update speed to be adjusted publicly
    public void setFrameRate(float rate) {
        if (rate > 0) {
            frameDelay = 1 / rate * 1000;
            return;
        }
        System.out.println("Error: invalid value for frame-rate");
    }

    // explicitly sets whether or not animated object should update animations at each interval
    void setShouldAnimate(boolean value) {
        shouldAnimate = value;
    }

    @Override
    protected void init() {
        frameTimeDelta = 0f;
    }

    public void update() {
//        System.out.println("frameDelay: " + frameDelay + ", frameTimeDelta: " + frameTimeDelta);
        frameTimeDelta += TimeManager.getInstance().getDeltaTime();
        if (shouldAnimate) shouldAnimate = false;
        if (frameTimeDelta >= frameDelay) {
            frameTimeDelta -= frameDelay;
            shouldAnimate = true;
        }
    }
}
