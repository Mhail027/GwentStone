package gameprogress;

import cards.HeroCard;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import cards.GameCard;
import output.*;
import output.errors.AttackError;
import output.errors.CardPlacementError;

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

        int status;
        switch (command) {
            case "getPlayerDeck" :
                ArrayList<GameCard> tableDeck = currGame.getPlayer(playerIdx).getTableDeck();
                newNode = objectMaper.valueToTree(PlayerStatOutput.init(command, playerIdx, tableDeck));
                break;

            case "getCardsInHand":
                ArrayList<GameCard> handDeck = currGame.getPlayer(playerIdx).getHandDeck();
                newNode = objectMaper.valueToTree(PlayerStatOutput.init(command, playerIdx, handDeck));
                break;

            case "getPlayerHero" :
                HeroCard hero = currGame.getPlayer(playerIdx).getHerro();
                newNode = objectMaper.valueToTree(PlayerStatOutput.init(command, playerIdx, hero));
                break;

            case "getCardsOnTable" :
                ArrayList<ArrayList<GameCard>> table = currGame.getTable();
                newNode = objectMaper.valueToTree(GameStatOutput.init(command, table));
                break;

            case "getPlayerTurn":
                int currPlayerIdx = currGame.getPlayerTurn();
                newNode = objectMaper.valueToTree(GameStatOutput.init(command,currPlayerIdx));
                break;

            case "getPlayerMana":
                int mana = currGame.getPlayer(playerIdx).getMana();
                newNode = objectMaper.valueToTree(PlayerStatOutput.init(command, playerIdx, mana));
                break;

            case "endPlayerTurn":
                currGame.goNextTurn();
                break;

            case "placeCard":
                status = currGame.placeCard(handIdx);
                if (status != 0) {
                    newNode = objectMaper.valueToTree(CardPlacementError.init(handIdx, status));
                }
                break;

            case "cardUsesAttack":
                status = currGame.cardUsesAttack(cardAttacker, cardAttacked);
                if (status != 0) {
                    newNode = objectMaper.valueToTree(AttackError.init(command, status));
                }
                break;

            case "getCardAtPosition":
                GameCard card = currGame.getCard(new Coordinates(x, y));
                if (card == null) {
                    newNode = objectMaper.valueToTree(TableCardOutput.init(x, y,
                            "No card available at that position."));
                } else {
                    newNode = objectMaper.valueToTree(TableCardOutput.init(x, y, card));
                }
                break;

            case "cardUsesAbility":
                status = currGame.cardUsesAbility(cardAttacker, cardAttacked);
                break;



            default:
                break;
        }

        if (newNode != null)
            output.add(newNode);

    }


}
