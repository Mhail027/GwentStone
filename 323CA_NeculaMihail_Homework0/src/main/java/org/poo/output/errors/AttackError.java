package org.poo.output.errors;

import org.poo.input.Coordinates;

public final class AttackError {
    private static AttackError instance;

    private String command;
    private Coordinates cardAttacked;
    private Coordinates cardAttacker;
    private String error;

    private AttackError() {
    }

    /**
     * Make sure that this class is Singleton.
     */
    public static AttackError getInstance() {
        if (instance == null) {
            instance = new AttackError();
        }

        return instance;
    }

    /**
     * Alternative to a constructor, without to break Singleton rules.
     */
    public static AttackError init(final String command, final Coordinates cardAttacked,
                                   final Coordinates cardAttacker, final String error) {
        AttackError o = getInstance();

        o.command = command;
        o.cardAttacked = cardAttacked;
        o.cardAttacker = cardAttacker;
        o.error = error;

        return o;
    }

    public String getCommand() {
        return command;
    }

    public Coordinates getCardAttacked() {
        return cardAttacked;
    }

    public Coordinates getCardAttacker() {
        return cardAttacker;
    }

    public String getError() {
        return error;
    }
}
