package input;

import java.util.ArrayList;

public final class GameInput {
    private StartGameInput startGame;
    private ArrayList<ActionInput> actionInputs;

    public GameInput() {
    }

    public StartGameInput getStartGame() {
        return startGame;
    }

    public void setStartGame(final StartGameInput startGame) {
        this.startGame = startGame;
    }

    public ArrayList<ActionInput> getActions() {
        return actionInputs;
    }

    public void setActions(final ArrayList<ActionInput> actions) {
        this.actionInputs = actions;
    }

    @Override
    public String toString() {
        return "GameInput{"
                +  "startGame="
                + startGame
                + ", actions="
                + actionInputs
                + '}';
    }
}
