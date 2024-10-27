package output;

public final class EndGameOutput {
    private static EndGameOutput instance;

    private String gameEnded;

    private EndGameOutput() {
    }

    /**
     * Make sure that this class is SingleTone.
     */
    public static EndGameOutput getInstance() {
        if (instance == null) {
            instance = new EndGameOutput();
        }

        return instance;
    }

    /**
     * Alternative to a constructor, without to break SingleTone rules.
     */
    public static EndGameOutput init(final int winner) {
        EndGameOutput o = getInstance();

        if (winner == 1) {
            o.gameEnded = "Player one killed the enemy hero.";
        } else {
            o.gameEnded = "Player two killed the enemy hero.";
        }

        return o;
    }

    public String getGameEnded() {
        return gameEnded;
    }

}
