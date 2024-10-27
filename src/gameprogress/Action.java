package gameprogress;

import cards.HeroCard;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import cards.GameCard;
import fileio.Coordinates;
import output.*;
import output.error.CardPlacement;

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

    /**
     * Verify an action and ,if it is possible, do it in the current game.
     * A message containing the result of the action is added in output,
     * if it's the case.
     */
    public void handle(final Game currGame, final ArrayNode output) {
        ObjectMapper objectMaper = new ObjectMapper();
        JsonNode newNode = null;

        switch (command) {
            case "getPlayerDeck" :
                ArrayList<GameCard> tableDeck = currGame.getPlayer(playerIdx).getTableDeck();
                newNode = objectMaper.valueToTree(PlayerDeck.init(command, playerIdx, tableDeck));
                break;

            case "getCardsInHand":
                ArrayList<GameCard> handDeck = currGame.getPlayer(playerIdx).getHandDeck();
                newNode = objectMaper.valueToTree(PlayerDeck.init(command, playerIdx, handDeck));
                break;

            case "getPlayerHero" :
                HeroCard hero = currGame.getPlayer(playerIdx).getHerro();
                newNode = objectMaper.valueToTree(PlayerHero.init(playerIdx, hero));
                break;

            case "getCardsOnTable" :
                ArrayList<ArrayList<GameCard>> table = currGame.getTable();
                newNode = objectMaper.valueToTree(GameTable.init(table));
                break;

            case "getPlayerTurn":
                int currPlayerIdx = currGame.getPlayerTurn();
                newNode = objectMaper.valueToTree(GameStat.init(command,currPlayerIdx));
                break;

            case "getPlayerMana":
                int mana = currGame.getPlayer(playerIdx).getMana();
                newNode = objectMaper.valueToTree(PlayerStat.init(command, playerIdx, mana));
                break;

            case "endPlayerTurn":
                currGame.goNextTurn();
                break;

            case "placeCard":
                int status = currGame.placeCard(handIdx);
                if (status != 0) {
                    newNode = objectMaper.valueToTree(CardPlacement.init(handIdx, status));
                }
                break;

            default:
                break;
        }

        if (newNode != null)
            output.add(newNode);

    }


}
