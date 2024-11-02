package input;

import cards.HeroCard;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import cards.RegularCard;
import gameprogress.Deck;
import gameprogress.Game;
import output.EndGameOutput;
import output.GameStatOutput;
import output.PlayerStatOutput;
import output.TableCardOutput;
import output.errors.AttackError;
import output.errors.AttackHeroError;
import output.errors.CardPlacementError;
import output.errors.HeroAbilityError;

import java.util.ArrayList;

public final class ActionInput {
    private String command;
    private int handIdx;
    private Coordinates cardAttacker;
    private Coordinates cardAttacked;
    private int affectedRow;
    private int playerIdx;
    private int x;
    private int y;

    public ActionInput() {
    }

    /**
     * Do an action in the current game. A message containing the result of the
     * action is added in output, if it's the case.
     */
    public void handle(final Game currGame, final ArrayNode output) {
        if (command.startsWith("get")) {
            handleGetAction(currGame, output);
        } else {
            handleDoAction(currGame, output);
        }
    }

    /**
     * Do an action of type "get some information about the game".
     */
    private void handleGetAction(final Game currGame, final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode newNode = switch (command) {
            case "getPlayerDeck" -> {
                Deck tableDeck = currGame.getPlayer(playerIdx).getTableDeck();
                yield objectMapper.valueToTree(
                        PlayerStatOutput.init(command, playerIdx, tableDeck.getCards()));
            }
            case "getCardsInHand" -> {
                Deck handDeck = currGame.getPlayer(playerIdx).getHandDeck();
                yield objectMapper.valueToTree(
                        PlayerStatOutput.init(command, playerIdx, handDeck.getCards()));
            }
            case "getPlayerHero" -> {
                HeroCard hero = currGame.getPlayer(playerIdx).getHero();
                yield objectMapper.valueToTree(
                        PlayerStatOutput.init(command, playerIdx, hero));
            }
            case "getCardsOnTable" -> {
                ArrayList<ArrayList<RegularCard>> table = currGame.getTable().getCards();
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
                RegularCard card = currGame.getTable().getCard(new Coordinates(x, y));
                if (card == null) {
                    yield  objectMapper.valueToTree(TableCardOutput.init(x, y,
                            "No card available at that position."));
                } else {
                    yield  objectMapper.valueToTree(
                            TableCardOutput.init(x, y, card));
                }
            }
            case "getFrozenCardsOnTable" -> {
                ArrayList<RegularCard> frozenCards = currGame.getTable().getFrozenCards();
                yield objectMapper.valueToTree(
                        GameStatOutput.init(command, frozenCards));
            }
            case "getPlayerOneWins" -> {
                yield objectMapper.valueToTree(
                        GameStatOutput.init(command, currGame.getPlayerOneWins()));
            }
            case "getPlayerTwoWins" -> {
                yield objectMapper.valueToTree(
                        GameStatOutput.init(command, currGame.getPlayerTwoWins()));
            }
            case "getTotalGamesPlayed" -> {
                yield objectMapper.valueToTree(
                        GameStatOutput.init(command, currGame.getGamesPlayed()));
            }
            default -> null;
        };

        if (newNode != null) {
            output.add(newNode);
        }
    }

    /**
     * Do an action of type "do something which help the game to progress".
     */
    private void handleDoAction(final Game currGame, final ArrayNode output) {
        if (currGame.isEnded()) {
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode newNode = switch (command) {
            case "endPlayerTurn" -> {
                currGame.goNextTurn();
                yield null;
            }
            case "placeCard" -> {
                String error = currGame.placeCard(handIdx);
                if (error != null) {
                    yield objectMapper.valueToTree(
                            CardPlacementError.init(handIdx, error));
                }
                yield null;
            }
            case "cardUsesAttack" -> {
                String error = currGame.cardUsesAttack(cardAttacker, cardAttacked);
                if (error != null) {
                    yield objectMapper.valueToTree(
                            AttackError.init(command, cardAttacked, cardAttacker, error));
                }
                yield null;
            }
            case "cardUsesAbility" -> {
                String error = currGame.cardUsesAbility(cardAttacker, cardAttacked);
                if (error != null) {
                    yield objectMapper.valueToTree(
                            AttackError.init(command, cardAttacked, cardAttacker, error));
                }
                yield null;
            }
            case "useAttackHero" -> {
                String status = currGame.useAttackOnHero(cardAttacker);
                if (status != null) {
                    if (status.equals("Game ended")) {
                        currGame.currPlayerWon();
                        yield objectMapper.valueToTree(
                                EndGameOutput.init(currGame.getCurrPlayer()));
                    } else {
                        yield objectMapper.valueToTree(
                                AttackHeroError.init(command, cardAttacker, status));
                    }
                }
                yield null;
            }
            case "useHeroAbility" -> {
                String error = currGame.useHeroAbility(affectedRow);
                if (error != null) {
                    yield objectMapper.valueToTree(
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
}
