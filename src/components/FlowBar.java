package components;

import interfaces.Animated;

public abstract class FlowBar extends Animated {
    int barFlow;
    int factor;
    boolean isFlowReversed;
    boolean isLocked;

    FlowBar() {
        setFrameRate(60);
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
    public void update() {
        super.update();
    }

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
