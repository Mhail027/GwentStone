package output;

public final class CardAtPosition {
    private static CardAtPosition instance;

    private final String command = "getCardAtPosition";
    private int x;
    private int y;
    private Object output;

    private CardAtPosition() {
    }

    /**
     * Make sure that this class is SingleTone.
     */
    public static CardAtPosition getInstance() {
        if (instance == null) {
            instance = new CardAtPosition();
        }

        return instance;
    }

    /**
     * Alternative to a constructor, without to break SingleTone rules.
     */
    public static CardAtPosition init(int x ,int y, final Object output) {
        CardAtPosition o = getInstance();

        o.x = x;
        o.y = y;
        o.output = output;

        return o;
    }

    public String getCommand() {
        return command;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Object getOutput() {
        return output;
    }
}
