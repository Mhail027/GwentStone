package org.poo.output.errors;

public final class CardPlacementError {
    private static CardPlacementError instance;

    private static final String COMMAND = "placeCard";
    private int handIdx;
    private String error;

    private CardPlacementError() {
    }

    /**
     * Make sure that this class is Singleton.
     */
    public static CardPlacementError getInstance() {
        if (instance == null) {
            instance = new CardPlacementError();
        }

        return instance;
    }

    /**
     * Alternative to a constructor, without to break Singleton rules.
     */
    public static CardPlacementError init(final int handIdx, final String error) {
        CardPlacementError o = getInstance();

        o.handIdx = handIdx;
        o.error = error;

        return o;
    }

    public String getCommand() {
        return COMMAND;
    }

    public int getHandIdx() {
        return handIdx;
    }

    public String getError() {
        return error;
    }
}
