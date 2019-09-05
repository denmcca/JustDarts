package managers;

public class TimeManager  {
    private static TimeManager instance;
    private long prevTime;
    private long lastTime;

    private TimeManager() {
        lastTime = System.currentTimeMillis();
        prevTime = lastTime;
        init();
    }
    public static TimeManager getInstance() {
        if (instance == null)
            instance = new TimeManager();
        return instance;
    }

    private void init() {
        // defaults
    }

    public void update() {
        prevTime = lastTime;
        lastTime = System.currentTimeMillis();
    }

    public long getDeltaTime() {
        return lastTime - prevTime;
    }
}
