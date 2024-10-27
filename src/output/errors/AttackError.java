package output.errors;

public final class AttackError {
    private final static String[] errors = {
            null,
            "Attacker card does not belong to the current player.",
            "Attacked card does not belong to the enemy.",
            "Attacker card has already attacked this turn.",
            "Attacker card is frozen.",
            "Attacked card is not of type 'Tank’.",
            "The coordinates of the cards are worng",
            "Attacker must be a special card.",
            "Attacked card does not belong to the current player."
    };

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
    public static AttackError init(final String command, final int error_code) {
        AttackError o = getInstance();

        o.command = command;
        o.error = errors[error_code];

        return o;
    }

    public String getCommand() {
        return command;
    }

    public String getError() {
        return error;
    }
}
