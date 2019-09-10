package interfaces;

import managers.TimeManager;

public class Animated extends Drawable {
    // Amount of time that has passed between changed frames.
    private float frameTimeDelta;
    // Desired frame rate in milliseconds inversed.
    private float frameDelay;
    // Flag which determines if next frame should be updated.
    protected boolean shouldAnimate;

    /**
     * Default constructor.
     * Sets frame rate to 60 as default.
     * Initializes frameTimeDelta to 0.
     */
    protected Animated() {
        frameTimeDelta = 0f;
        setFrameRate(60);
    }

    /**
     * Public interface which allows developer to directed set the frame rate.
     * @param rate is the desired frame rate per second
     */
    public void setFrameRate(float rate) {
        // Frame rate cannot be 0 or negative.
        if (rate <= 0) {
            System.out.println("Error: invalid value for frame-rate");
            return;
        }
        // (frames per second / 1000)^(-1)
        frameDelay = 1 / rate * 1000;
    }

    /**
     * Resets frameTimeDelta back to 0.
     */
    @Override
    protected void init() {
        frameTimeDelta = 0f;
    }

    /**
     * Updates frameTimeDelta per cycle during update.
     * Resets shouldAnimate flag to false if true.
     * Changes shouldAnimate flag to true and removes the value from frameDelay from frameTimeDelta
     * if enough time passed between frames.
     */
    public void update() {
        frameTimeDelta += TimeManager.getInstance().getDeltaTime();
        if (shouldAnimate) shouldAnimate = false;
        if (frameTimeDelta >= frameDelay) {
            frameTimeDelta -= frameDelay;
            shouldAnimate = true;
        }
    }
}
