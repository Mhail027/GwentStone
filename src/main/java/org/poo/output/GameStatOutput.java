package org.poo.output;

public final class GameStatOutput {
    private static GameStatOutput instance;

    private String command;
    private Object output;

    private GameStatOutput() {
    }

    /**
     * Make sure that this class is Singleton.
     */
    public static GameStatOutput getInstance() {
        if (instance == null) {
            instance = new GameStatOutput();
        }

        return instance;
    }

    /**
     * Alternative to a constructor, without to break Singleton rules.
     */
    public static GameStatOutput init(final String command,
                                      final Object output) {
        GameStatOutput o = getInstance();

        o.command = command;
        o.output = output;

        return o;
    }

    public String getCommand() {
        return command;
    }

    public Object getOutput() {
        return output;
    }
}
