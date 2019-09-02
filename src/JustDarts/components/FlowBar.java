package JustDarts.components;

import JustDarts.interfaces.Drawable;
import JustDarts.managers.TimeManager;

public abstract class FlowBar extends Drawable {
    int barFlow;
    int factor;
    boolean isFlowReversed;
    boolean isLocked;

    FlowBar() {
        init();
    }

    @Override
    protected void init() {
        barFlow = 1;
        factor = 1;
        isFlowReversed = false;
        isLocked = true;
    }

    @Override
    public void update() { }

    protected void flow() {
        if (!shouldDraw) {
            if (barFlow > 10)
                barFlow = 0;
        }
    }

    public void reset() {
        init();
    }

    public void enableFlowLock() {
        isLocked = true;
    }

    public void disableFlowLock() {
        isLocked = false;
    }
}
