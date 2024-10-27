package output;

import cards.HeroCard;

public final class PlayerHero {
    private static PlayerHero instance;

    private final String command = "getPlayerHero";
    private int playerIdx;
    private HeroCard output;

    private PlayerHero() {
    }

    /**
     * Make sure that this class is SingleTone.
     */
    public static PlayerHero getInstance() {
        if (instance == null) {
            instance = new PlayerHero();
        }

        return instance;
    }

    /**
     * Alternative to a constructor, without to break SingleTone rules.
     */
    public static PlayerHero init(final int playerIdx, final HeroCard output) {
        PlayerHero playerHero = getInstance();
        playerHero.playerIdx = playerIdx;
        playerHero.output = output;
        return playerHero;
    }

    public String getCommand() {
        return command;
    }

    public int getPlayerIdx() {
        return playerIdx;
    }
    public HeroCard getOutput() {
        return output;
    }
}
