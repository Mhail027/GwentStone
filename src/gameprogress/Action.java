package gameprogress;

import cards.HeroCard;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import cards.GameCard;
import output.EndGameOutput;
import output.GameStatOutput;
import output.PlayerStatOutput;
import output.TableCardOutput;
import output.errors.AttackError;
import output.errors.CardPlacementError;
import output.errors.HeroAbilityError;

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
        if (command.startsWith("get")) {
            handleGetAction(currGame, output);
        } else {
            handleDoAction(currGame, output);
        }
    }

    private void handleGetAction(final Game currGame, final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode newNode = switch (command) {
            case "getPlayerDeck" -> {
                ArrayList<GameCard> tableDeck = currGame.getPlayer(playerIdx).getTableDeck();
                yield objectMapper.valueToTree(
                        PlayerStatOutput.init(command, playerIdx, tableDeck));
            }
            case "getCardsInHand" -> {
                ArrayList<GameCard> handDeck = currGame.getPlayer(playerIdx).getHandDeck();
                yield objectMapper.valueToTree(
                        PlayerStatOutput.init(command, playerIdx, handDeck));
            }
            case "getPlayerHero" -> {
                HeroCard hero = currGame.getPlayer(playerIdx).getHero();
                yield objectMapper.valueToTree(
                        PlayerStatOutput.init(command, playerIdx, hero));
            }
            case "getCardsOnTable" -> {
                ArrayList<ArrayList<GameCard>> table = currGame.getTable();
                yield objectMapper.valueToTree(
                        GameStatOutput.init(command, table));
            }
            case "getPlayerTurn" -> {
                int currPlayerIdx = currGame.getCurrPlayer();
                yield objectMapper.valueToTree(
                        GameStatOutput.init(command, currPlayerIdx));
            }
            case "getPlayerMana" -> {
                int mana = currGame.getPlayer(playerIdx).getMana();
                yield objectMapper.valueToTree(
                        PlayerStatOutput.init(command, playerIdx, mana));
            }
            case "getCardAtPosition" -> {
                GameCard card = currGame.getCard(new Coordinates(x, y));
                if (card == null) {
                    yield  objectMapper.valueToTree(TableCardOutput.init(x, y,
                            "No card available at that position."));
                } else {
                    yield  objectMapper.valueToTree(
                            TableCardOutput.init(x, y, card));
                }
            }
            case "getFrozenCardsOnTable" -> {
                ArrayList<GameCard> frozenCards = currGame.getFrozenCards();
                yield objectMapper.valueToTree(
                        GameStatOutput.init(command, frozenCards));
            }
            default -> null;
        };

        if (newNode != null) {
            output.add(newNode);
        }
    }

    private void handleDoAction(final Game currGame, final ArrayNode output) {
        ObjectMapper objectMaper = new ObjectMapper();
        JsonNode newNode = switch (command) {
            case "endPlayerTurn" -> {
                currGame.goNextTurn();
                yield null;
            }
            case "placeCard" -> {
                String error = currGame.placeCard(handIdx);
                if (error != null) {
                    yield objectMaper.valueToTree(
                            CardPlacementError.init(handIdx, error));
                }
                yield null;
            }
            case "cardUsesAttack" -> {
                String error = currGame.cardUsesAttack(cardAttacker, cardAttacked);
                if (error != null) {
                    yield objectMaper.valueToTree(
                            AttackError.init(command, error));
                }
                yield null;
            }
            case "cardUsesAbility" -> {
                String error = currGame.cardUsesAbility(cardAttacker, cardAttacked);
                if (error != null) {
                    yield objectMaper.valueToTree(
                            AttackError.init(command, error));
                }
                yield null;
            }
            case "useAttackHero" -> {
                String status = currGame.useAttackOnHero(cardAttacker);
                if (status != null) {
                    if (status.equals("Game ended")) {
                        yield  objectMaper.valueToTree(
                                EndGameOutput.init(currGame.getCurrPlayer()));
                    } else {
                        yield  objectMaper.valueToTree(
                                AttackError.init(command, status));
                    }
                }
                yield null;
            }
            case "useHeroAbility" -> {
                String error = currGame.useHeroAbility(affectedRow);
                if (error != null) {
                    yield objectMaper.valueToTree(
                            HeroAbilityError.init(command, affectedRow, error));
                }
                yield null;
            }
            default -> {
                yield null;
            }
        };

        if (newNode != null) {
            output.add(newNode);
        }
    }


}
