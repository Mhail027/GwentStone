package actions;

import cards.HeroCard;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import cards.GameCard;
import fileio.Coordinates;
import gameprogress.Game;

import java.util.ArrayList;

public final class Action {
    private String command;
    private int handIdx;
    private Coordinates cardAttacker;
    private Coordinates cardAttacked;
    private int affectedRow;
    private int playerIdx;
    private int x;
    private int y;

    public Action() {
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(final String command) {
        this.command = command;
    }

    public int getHandIdx() {
        return handIdx;
    }

    public void setHandIdx(final int handIdx) {
        this.handIdx = handIdx;
    }

    public Coordinates getCardAttacker() {
        return cardAttacker;
    }

    public void setCardAttacker(final Coordinates cardAttacker) {
        this.cardAttacker = cardAttacker;
    }

    public Coordinates getCardAttacked() {
        return cardAttacked;
    }

    public void setCardAttacked(final Coordinates cardAttacked) {
        this.cardAttacked = cardAttacked;
    }

    public int getAffectedRow() {
        return affectedRow;
    }

    public void setAffectedRow(final int affectedRow) {
        this.affectedRow = affectedRow;
    }

    public int getPlayerIdx() {
        return playerIdx;
    }

    public void setPlayerIdx(final int playerIdx) {
        this.playerIdx = playerIdx;
    }

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "ActionsInput{"
                +  "command='"
                + command + '\''
                +  ", handIdx="
                + handIdx
                +  ", cardAttacker="
                + cardAttacker
                +  ", cardAttacked="
                + cardAttacked
                + ", affectedRow="
                + affectedRow
                + ", playerIdx="
                + playerIdx
                + ", x="
                + x
                + ", y="
                + y
                + '}';
    }

    public void handle(Game currGame, ArrayNode output) {
        ObjectMapper objectMaper = new ObjectMapper();
        JsonNode newNode = null;

        switch(command) {
            case "getPlayerDeck" :
                ArrayList<GameCard> deck = currGame.getPlayer(playerIdx).getTableDeck();
                newNode = objectMaper.valueToTree(new PlayerDeck(playerIdx, deck));
                break;

            case "getPlayerHero" :
                HeroCard hero = currGame.getPlayer(playerIdx).getHerro();
                newNode = objectMaper.valueToTree(new PlayerHero(playerIdx, hero));
                break;

            case "getPlayerTurn":
                int currPlayerIdx = currGame.getPlayerTurn();
                newNode = objectMaper.valueToTree(new PlayerTurn(currPlayerIdx));
                break;
        }
        output.add(newNode);

    }


}
