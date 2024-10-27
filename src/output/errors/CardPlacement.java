package output.errors;

public final class CardPlacement {
    private final static String[] errors = {
            null,
            "Number of card is bigger than number of cards from hand.",
            "Not enough mana to place card on table.",
            "Cannot place card on table since row is full."
    };

    private static CardPlacement instance;

    private final String command = "placeCard";
    private int handIdx;
    private String error;

    private CardPlacement() {
    }

    /**
     * Make sure that this class is SingleTone.
     */
    public static CardPlacement getInstance() {
        if (instance == null) {
            instance = new CardPlacement();
        }

        return instance;
    }

    /**
     * Alternative to a constructor, without to break SingleTone rules.
     */
    public static CardPlacement init(final int handIdx, final int error_code) {
        CardPlacement object = getInstance();

        object.handIdx = handIdx;
        object.error = errors[error_code];

        return object;
    }

    public String getCommand() {
        return command;
    }

    public int getHandIdx() {
        return handIdx;
    }

    public String getError() {
        return error;
    }
}
