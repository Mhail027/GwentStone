package actions;

import cards.GameCard;

import java.util.ArrayList;

public final class PlayerDeck {
    private static PlayerDeck instance;

    private final String command = "getPlayerDeck";
    private int playerIdx;
    private ArrayList<GameCard> output;

    private PlayerDeck() {
    }

    /**
     * Make sure that this class is SingleTone.
     */
    public static PlayerDeck getInstance() {
        if (instance == null) {
            instance = new PlayerDeck();
        }

        return instance;
    }

    /**
     * Alternative to a constructor, without to break SingleTone rules.
     */
    public static PlayerDeck init(final int playerIdx, final ArrayList<GameCard> output) {
        PlayerDeck playerDeck = getInstance();
        playerDeck.playerIdx = playerIdx;
        playerDeck.output = output;
        return playerDeck;
    }

    public String getCommand() {
        return command;
    }

    public int getPlayerIdx() {
        return playerIdx;
    }

    public ArrayList<GameCard> getOutput() {
        return output;
    }
}
