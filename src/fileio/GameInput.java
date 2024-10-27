package fileio;

import gameprogress.Action;

import java.util.ArrayList;

public final class GameInput {
    private StartGameInput startGame;
    private ArrayList<Action> actions;

    public GameInput() {
    }

    public StartGameInput getStartGame() {
        return startGame;
    }

    public void setStartGame(final StartGameInput startGame) {
        this.startGame = startGame;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setActions(final ArrayList<Action> actions) {
        this.actions = actions;
    }

    @Override
    public String toString() {
        return "GameInput{"
                +  "startGame="
                + startGame
                + ", actions="
                + actions
                + '}';
    }
}
