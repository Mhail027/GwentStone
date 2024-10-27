package gameprogress;

import cards.GameCard;
import cards.HeroCard;
import fileio.Input;
import fileio.StartGameInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public final class Game {
    private static final int NUMBER_OF_PLAYERS = 2;
    private static final int TABLE_ROWS = 4;
    private static final int TABLE_COLS = 5;
    private static final int MAX_RECEIVED_MANA_PER_ROUND = 10;

    private static Game instance;

    private int roundIdx;
    private int playerTurn;
    private int firstPlayerOfRound;

    // We start the count from 1 for players.
    private Player[] players = new Player[NUMBER_OF_PLAYERS + 1];
    private ArrayList<ArrayList<GameCard>> table;

    private Game() {
    }

    /**
     * Make sure that this class is SingleTone.
     */
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }

        return instance;
    }

    /**
     * Alternative to a constructor, without to break SingleTone rules.
     */
    public static Game init(final Input inputData, final StartGameInput startGameData) {
        Game currGame = getInstance();
        currGame.initHelper(inputData, startGameData);
        return currGame;
    }

    private void initHelper(final Input inputData, final StartGameInput startGameData) {
        // Note some "boring", but needed data.
        roundIdx = 1;
        playerTurn = startGameData.getStartingPlayer();
        firstPlayerOfRound = startGameData.getStartingPlayer();

        // Every player receives the deck and the herro for the current game.
        int playerOneDeckIdx = startGameData.getPlayerOneDeckIdx();
        HeroCard playerOneHerro = startGameData.getPlayerOneHero();
        players[1] = new Player(1, inputData.getPlayerOneDecks().getDeck(playerOneDeckIdx), playerOneHerro);

        int playerTwoDeckIdx = startGameData.getPlayerTwoDeckIdx();
        HeroCard playerTwoHerro = startGameData.getPlayerTwoHero();
        players[2] = new Player(2, inputData.getPlayerTwoDecks().getDeck(playerTwoDeckIdx), playerTwoHerro);

        // Everyone shuffle his deck.
        int shuffleSeed = startGameData.getShuffleSeed();
        for (int i = 1; i <= NUMBER_OF_PLAYERS; ++i) {
            ArrayList<GameCard> playerDeck = players[i].getTableDeck();
            Collections.shuffle(playerDeck, new Random(shuffleSeed));
        }

        // Create space to put the game table.
        table = new ArrayList<>();
        for (int i = 0; i < TABLE_ROWS; ++i)
            table.add(new ArrayList<>());
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    /**
     * Return the player which has the given idx.
     */
    public Player getPlayer(final int idx) {
        return players[idx];

    }

    public ArrayList<ArrayList<GameCard>> getTable() {
        return table;
    }

    /**
     * Do the needed actions as a new round to can start:
     * every player receives mana and draws a card.
     */
    public void startRound() {
        playersReceiveMana();
        playersDrawCard();
        everyCardCanAttack();
    }

    private void playersReceiveMana() {
        int receivedMana = roundIdx;
        if (receivedMana > MAX_RECEIVED_MANA_PER_ROUND) {
            receivedMana = MAX_RECEIVED_MANA_PER_ROUND;
        }

        for (int i = 1; i <= NUMBER_OF_PLAYERS; ++i) {
            players[i].receivesMana(receivedMana);
        }
    }

    private void playersDrawCard() {
        for (int i = 1; i <= NUMBER_OF_PLAYERS; ++i) {
            ArrayList<GameCard> tableDeck = players[i].getTableDeck();
            ArrayList<GameCard> handDeck = players[i].getHandDeck();

            if (!tableDeck.isEmpty()) {
                GameCard drawnCard = tableDeck.get(0);
                tableDeck.remove(0);
                handDeck.add(drawnCard);
            }
        }
    }

    private void everyCardCanAttack() {
        for (ArrayList<GameCard> row : table) {
            for (GameCard card : row) {
                card.setUsedAtack(0);
            }
        }
    }

    /**
     * The turn of current player ends.
     */
    public void goNextTurn() {
        playerTurn += 1;
        if (playerTurn > NUMBER_OF_PLAYERS) {
            playerTurn = 1;
        }

        // Verify if we must start a new round.
        if (playerTurn == firstPlayerOfRound) {
            roundIdx++;
            startRound();
        }
    }

    public int placeCard(final int cardIdx) {
        // Verify if the cardIdx is valid.
        int cardsInHand = players[playerTurn].getHandDeck().size();
        if (cardIdx >= cardsInHand) {
            return 1;
        }

        // Take the wanted card from handDeck.
        GameCard card = players[playerTurn].getHandDeck().get(cardIdx);

        // Verify if we have enough mana for this card.
        if (card.getMana() > players[playerTurn].getMana()) {
            return 2;
        }

        // Verify if it's space on the row.
        int rowIdx = findRowForCard(playerTurn, card.getName());
        if (table.get(rowIdx).size() >= TABLE_COLS) {
            return 3;
        }

        // Place the card on table.
        players[playerTurn].getHandDeck().remove(cardIdx);
        table.get(rowIdx).add(card);
        players[playerTurn].usesMana(card.getMana());

        return 0;
    }

    private int findRowForCard(final int playerIdx, final String cardName) {

        if (cardName.equals("Sentinel") || cardName.equals("Berserker")) {
            return (playerIdx == 1) ? 3 : 0;
        }
        if (cardName.equals("The Cursed One") || cardName.equals("Disciple")) {
            return (playerIdx == 1) ? 3 : 0;
        }

        return (playerIdx == 1) ? 2 : 1;
    }

    public int cardUsesAttack(Coordinates attacker, Coordinates attacked) {
        // Verify if the attacker is a card of the current player.
        if (tableCardBelongsToPerson(attacker, playerTurn) == 0) {
            return 1;
        }

        // Verify if the attacked is a card of th eopponent.
        int opponentIdx = playerTurn % 2 + 1;
        if (tableCardBelongsToPerson(attacked, opponentIdx) == 0) {
            return 2;
        }
        // Take the attacker card.
        GameCard attackerCard = table.get(attacker.getX()).get(attacker.getY());

        // Verify if the card didn't attack already in this round.
        if (attackerCard.getUsedAtack() == 1) {
            return 3;
        }

        // Vereify if the atacker is not frozen.
        if (attackerCard.getFrozen() == 1) {
            return 4;
        }

        // Take the attacked card.
        GameCard attackedCard = table.get(attacked.getX()).get(attacked.getY());

        // If attacked card is not a tank, verify if there's a tank of front row of opponent.
        if (!attackedCard.getName().equals("Goliath") &&  !attackedCard.getName().equals("Warden")) {
            int opponentFrontRow = (playerTurn == 1) ? 2 : 0;
            for (GameCard card : table.get(opponentFrontRow)) {
                if (card.getName().equals("Goliath") || card.getName().equals("Warden"))
                    return 5;
            }
        }

        // The attack takes place.
        attackedCard.decreaseHealth(attackerCard.getAttackDamage());
        if (attackedCard.getHealth() <= 0) {
            table.get(attacked.getX()).remove(attacked.getY());
        }
        return 0;
    }

    private int tableCardBelongsToPerson(Coordinates card, int personIdx) {
        if (card.getX() < 0 || card.getY() < 0)
            return 0;
        if (personIdx == 1 && card.getX() != 3 && card.getX() != 2) {
                return 0;
        }
        if (personIdx == 2 && card.getX() != 1 && card.getX() != 0) {
                return 0;
        }
        if (card.getY() >= table.get(card.getX()).size()) {
            return 0;
        }

        return 1;
    }

    private boolean coordinatesAreValid(Coordinates card) {
        return ((tableCardBelongsToPerson(card, 1) == 1 || tableCardBelongsToPerson(card, 2) == 1));
    }

    public GameCard getCard(Coordinates position) {
        if (coordinatesAreValid(position)) {
            return table.get(position.getX()).get(position.getY());
        }
        return null;
    }
}
