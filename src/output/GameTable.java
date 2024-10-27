package output;

import cards.GameCard;

import java.util.ArrayList;

public final class GameTable {
    private static GameTable instance;

    private final String command = "getCardsOnTable";
    private ArrayList<ArrayList<GameCard>> output;

    private GameTable() {
    }

    /**
     * Make sure that this class is SingleTone.
     */
    public static GameTable getInstance() {
        if (instance == null) {
            instance = new GameTable();
        }

        return instance;
    }

    /**
     * Alternative to a constructor, without to break SingleTone rules.
     */
    public static GameTable init(final ArrayList<ArrayList<GameCard>> output) {
        GameTable gameTable = getInstance();

        gameTable.output = output;

        return gameTable;
    }

    public String getCommand() {
        return command;
    }

    public ArrayList<ArrayList<GameCard>> getOutput() {
        return output;
    }
}
