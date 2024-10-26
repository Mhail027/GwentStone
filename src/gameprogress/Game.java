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
    private GameCard[][] table = new GameCard[NUMBER_OF_ROWS][NUMBER_OF_COLS];

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

    /**
     * Do the needed actions as a new round to can start:
     * every player receives mana and draws a card.
     */
    public void startRound() {
        // Every player receives mana.
        int receivedMana = (int) roundIdx;
        if (receivedMana > MAX_RECEIVED_MANA_PER_ROUND) {
            receivedMana = MAX_RECEIVED_MANA_PER_ROUND;
        }
        players[1].addMana(receivedMana);
        players[2].addMana(receivedMana);

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
            startRound();
        }
    }
}
