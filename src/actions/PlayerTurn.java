package actions;

public final class PlayerTurn {
    private static PlayerTurn instance;

    private final String command = "getPlayerTurn";
    private int output;

    private PlayerTurn() {
    }

    /**
     * Make sure that this class is SingleTone.
     */
    public static PlayerTurn getInstance() {
        if (instance == null) {
            instance = new PlayerTurn();
        }

        return instance;
    }

    /**
     * Alternative to a constructor, without to break SingleTone rules.
     */
    public static PlayerTurn init(final int output) {
        PlayerTurn playerTurn = getInstance();
        playerTurn.output = output;
        return playerTurn;
    }

    public String getCommand() {
        return command;
    }

    public int getOutput() {
        return output;
    }
}
