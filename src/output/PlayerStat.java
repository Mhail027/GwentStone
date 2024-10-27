package output;

public final class PlayerStat {
    private static PlayerStat instance;

    private String command;
    private int playerIdx;
    private int output;

    private PlayerStat() {
    }

    /**
     * Make sure that this class is SingleTone.
     */
    public static PlayerStat getInstance() {
        if (instance == null) {
            instance = new PlayerStat();
        }

        return instance;
    }

    /**
     * Alternative to a constructor, without to break SingleTone rules.
     */
    public static PlayerStat init(final String command, final int playerIdx, final int output) {
        PlayerStat playerStat = getInstance();

        playerStat.command = command;
        playerStat.playerIdx = playerIdx;
        playerStat.output = output;

        return playerStat;
    }

    public String getCommand() {
        return command;
    }

    public int getPlayerIdx() {
        return playerIdx;
    }

    public int getOutput() {
        return output;
    }
}
