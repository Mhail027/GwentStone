package gameprogress;

import cards.GameCard;
import cards.HeroCard;
import fileio.Input;
import fileio.StartGameInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Game {
	private int roundIdx;
	private int playerTurn;
	private Player[] players = new Player[3];
	private GameCard[][] table = new GameCard[4][5];

	public int getPlayerTurn () {
		return playerTurn;
	}

	public Player getPlayer(int idx) {
		return players[idx];
	}

	public Game(Input inputData, StartGameInput startGameData) {
		roundIdx = 1;
		playerTurn = startGameData.getStartingPlayer();

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

	public void startCurrRound() {
		// Every player receives mana.
		int receivedMana = (int)roundIdx;
		if (receivedMana > 10)
			receivedMana =  10;
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

	public void goNextTurn() {
		roundIdx += 1;
	}
}
