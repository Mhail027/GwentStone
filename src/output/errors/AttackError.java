package output.errors;

public final class AttackError {
    private static AttackError instance;

    private String command;
    private String error;

    private AttackError() {
    }

    /**
     * Make sure that this class is SingleTone.
     */
    public static AttackError getInstance() {
        if (instance == null) {
            instance = new AttackError();
        }

        return instance;
    }

    /**
     * Alternative to a constructor, without to break SingleTone rules.
     */
    public static AttackError init(final String command, final String error) {
        AttackError o = getInstance();

        o.command = command;
        o.error = error;

        return o;
    }

    public String getCommand() {
        return command;
    }

    public String getError() {
        return error;
    }
}
