package components;

public enum Sounds {

    TINNY_IMPACT("tinny_impact.wav"),
    WOODEN_IMPACT("wooden_impact.wav"),
    TERRIBLE("sad_trumbone.wav"),
    WOOD_PLASTIC_IMPACT("missedDart.wav"),
    GAMEOVER("Game_Over_-_Sound_Effect.wav");

    String value;
    Sounds(String path) {
        value = path;
    }

    public String getValue() {
        return value;
    }
}
