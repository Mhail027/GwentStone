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
    private static final int NUMBER_OF_ROWS = 4;
    private static final int NUMBER_OF_COLS = 5;
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
        ArrayList<ArrayList<GameCard>> playerOneDecks = inputData.getPlayerOneDecks().getDecks();
        int playerOneDeckIdx = startGameData.getPlayerOneDeckIdx();
        HeroCard playerOneHerro = startGameData.getPlayerOneHero();
        players[1] = new Player(1, playerOneDecks.get(playerOneDeckIdx), playerOneHerro);

        ArrayList<ArrayList<GameCard>> playerTwoDecks = inputData.getPlayerTwoDecks().getDecks();
        int playerTwoDeckIdx = startGameData.getPlayerTwoDeckIdx();
        HeroCard playerTwoHerro = startGameData.getPlayerTwoHero();
        players[2] = new Player(1, playerTwoDecks.get(playerTwoDeckIdx), playerTwoHerro);

        // Everyone shuffle his deck.
        int shuffleSeed = startGameData.getShuffleSeed();

        ArrayList<GameCard> playerOneDeck = players[1].getTableDeck();
        Collections.shuffle(playerOneDeck, new Random(shuffleSeed));

        ArrayList<GameCard> playerTwoDeck = players[2].getTableDeck();
        Collections.shuffle(playerTwoDeck, new Random(shuffleSeed));

        // Create space to put the game table.
        table = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_ROWS; ++i)
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
        // Every player receives mana.
        int receivedMana = roundIdx;
        if (receivedMana > MAX_RECEIVED_MANA_PER_ROUND) {
            receivedMana = MAX_RECEIVED_MANA_PER_ROUND;
        }
        players[1].receivesMana(receivedMana);
        players[2].receivesMana(receivedMana);

        // Everyone draws a card from his table deck, if he can.
        for (int i = 1; i <= 2; ++i) {
            ArrayList<GameCard> tableDeck = players[i].getTableDeck();
            ArrayList<GameCard> handDeck = players[i].getHandDeck();
            if (tableDeck.size() > 0) {
                GameCard drawnCard = tableDeck.get(0);
                tableDeck.remove(0);
                handDeck.add(drawnCard);
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
        if (table.get(rowIdx).size() >= NUMBER_OF_COLS) {
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
}
