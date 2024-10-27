package output.errors;

public final class HeroAbilityError {
    private final static String[] errors = {
            null,
            "Not enough mana to use hero's ability.",
            "Hero has already attacked this turn.",
            "Selected row does not belong to the current player.",
            "Selected row does not belong to the enemy."
    };

    private static HeroAbilityError instance;

    private String command;
    private int affectedRow;
    private String error;

    private HeroAbilityError() {
    }

    /**
     * Make sure that this class is SingleTone.
     */
    public static HeroAbilityError getInstance() {
        if (instance == null) {
            instance = new HeroAbilityError();
        }

        return instance;
    }

    /**
     * Alternative to a constructor, without to break SingleTone rules.
     */
    public static HeroAbilityError init(final String command, final int affectedRow, final int error_code) {
        HeroAbilityError o = getInstance();

        o.command = command;
        o.affectedRow = affectedRow;
        o.error = errors[error_code];

        return o;
    }

    public String getCommand() {
        return command;
    }

    public int getAffectedRow() {
        return affectedRow;
    }

    public String getError() {
        return error;
    }
}
