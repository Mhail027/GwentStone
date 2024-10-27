package output;

public final class TableCardOutput {
    private static TableCardOutput instance;

    private final String command = "getCardAtPosition";
    private int x;
    private int y;
    private Object output;

    private TableCardOutput() {
    }

    /**
     * Make sure that this class is SingleTone.
     */
    public static TableCardOutput getInstance() {
        if (instance == null) {
            instance = new TableCardOutput();
        }

        return instance;
    }

    /**
     * Alternative to a constructor, without to break SingleTone rules.
     */
    public static TableCardOutput init(int x , int y, final Object output) {
        TableCardOutput o = getInstance();

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
