package output.errors;

public final class CardPlacementError {
    private static final String[] ERRORS = {
            null,
            "Number of card is bigger than number of cards from hand.",
            "Not enough mana to place card on table.",
            "Cannot place card on table since row is full."
    };

    private static CardPlacementError instance;

    private final String command = "placeCard";
    private int handIdx;
    private String error;

    private CardPlacementError() {
    }

    /**
     * Make sure that this class is SingleTone.
     */
    public static CardPlacementError getInstance() {
        if (instance == null) {
            instance = new CardPlacementError();
        }

        return instance;
    }

    /**
     * Alternative to a constructor, without to break SingleTone rules.
     */
    public static CardPlacementError init(final int handIdx,
                                          final int errorCode) {
        CardPlacementError o = getInstance();

        o.handIdx = handIdx;
        o.error = ERRORS[errorCode];

        return o;
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
