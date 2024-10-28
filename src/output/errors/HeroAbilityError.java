package output.errors;

public final class HeroAbilityError {
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
    public static HeroAbilityError init(final String command,
                                        final int affectedRow,
                                        final String error) {
        HeroAbilityError o = getInstance();

        o.command = command;
        o.affectedRow = affectedRow;
        o.error = error;

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
