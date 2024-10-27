package output.errors;

public final class CardAttack {
    private final static String[] errors = {
            null,
            "Attacker card does not belong to the current player.",
            "Attacked card does not belong to the enemy.",
            "Attacker card has already attacked this turn.",
            "Attacker card is frozen.",
            "Attacked card is not of type 'Tank’."
    };

    private static CardAttack instance;

    private final String command = "placeCard";
    private String error;

    private CardAttack() {
    }

    /**
     * Make sure that this class is SingleTone.
     */
    public static CardAttack getInstance() {
        if (instance == null) {
            instance = new CardAttack();
        }

        return instance;
    }

    /**
     * Alternative to a constructor, without to break SingleTone rules.
     */
    public static CardAttack init(final int error_code) {
        CardAttack object = getInstance();

        object.error = errors[error_code];

        return object;
    }

    public String getCommand() {
        return command;
    }

    public String getError() {
        return error;
    }
}
