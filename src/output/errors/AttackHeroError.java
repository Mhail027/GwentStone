package output.errors;

import input.Coordinates;

public final class AttackHeroError {
    private static AttackHeroError instance;

    private String command;
    private Coordinates cardAttacker;
    private String error;

    private AttackHeroError() {
    }

    /**
     * Make sure that this class is Singleton.
     */
    public static AttackHeroError getInstance() {
        if (instance == null) {
            instance = new AttackHeroError();
        }

        return instance;
    }

    /**
     * Alternative to a constructor, without to break Singleton rules.
     */
    public static AttackHeroError init(final String command, final Coordinates cardAttacker,
                                       final String error) {
        AttackHeroError o = getInstance();

        o.command = command;
        o.cardAttacker = cardAttacker;
        o.error = error;

        return o;
    }

    public String getCommand() {
        return command;
    }

    public String getError() {
        return error;
    }

    public Coordinates getCardAttacker() {
        return cardAttacker;
    }
}
