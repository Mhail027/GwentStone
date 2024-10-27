package output;

public final class GameStat {
    private static GameStat instance;

    private String command;
    private Object output;

    private GameStat() {
    }

    /**
     * Make sure that this class is SingleTone.
     */
    public static GameStat getInstance() {
        if (instance == null) {
            instance = new GameStat();
        }

        return instance;
    }

    /**
     * Alternative to a constructor, without to break SingleTone rules.
     */
    public static GameStat init(final String command, final Object output) {
        GameStat gameStat = getInstance();

        gameStat.command = command;
        gameStat.output = output;

        return gameStat;
    }

    public String getCommand() {
        return command;
    }

    public Object getOutput() {
        return output;
    }
}
