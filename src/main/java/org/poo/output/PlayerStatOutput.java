package org.poo.output;

public final class PlayerStatOutput {
    private static PlayerStatOutput instance;

    private String command;
    private int playerIdx;
    private Object output;

    private PlayerStatOutput() {
    }

    /**
     * Make sure that this class is Singleton.
     */
    public static PlayerStatOutput getInstance() {
        if (instance == null) {
            instance = new PlayerStatOutput();
        }

        return instance;
    }

    /**
     * Alternative to a constructor, without to break Singleton rules.
     */
    public static PlayerStatOutput init(final String command,
                                        final int playerIdx,
                                        final Object output) {
        PlayerStatOutput o = getInstance();

        o.command = command;
        o.playerIdx = playerIdx;
        o.output = output;

        return o;
    }

    public String getCommand() {
        return command;
    }

    public int getPlayerIdx() {
        return playerIdx;
    }

    public Object getOutput() {
        return output;
    }
}
