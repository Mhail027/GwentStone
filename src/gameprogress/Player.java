package gameprogress;

import cards.GameCard;
import cards.HeroCard;

import java.util.ArrayList;

public class Player {
	private int idx;
	private int mana;
	private ArrayList<GameCard> handDeck;
	private ArrayList<GameCard> tableDeck;
	private HeroCard herro;

	public Player(int idx, ArrayList<GameCard> tableDeck, HeroCard herro) {
		this.idx = idx;
		this.mana = 0;
		this.handDeck = new ArrayList<>(0);
		this.tableDeck = new ArrayList<>(0);
		for (GameCard card : tableDeck)
			this.tableDeck.add(new GameCard(card));
		this.herro = herro;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public void addMana(int receivedMana) {
		this.mana += receivedMana;
	}

	public ArrayList<GameCard> getHandDeck() {
		return handDeck;
	}

	public void setHandDeck(ArrayList<GameCard> handDeck) {
		this.handDeck = handDeck;
	}

	public ArrayList<GameCard> getTableDeck() {
		return tableDeck;
	}

	public void setTableDeck(ArrayList<GameCard> tableDeck) {
		this.tableDeck = tableDeck;
	}

	public HeroCard getHerro() {
		return herro;
	}

	public void setHerro(HeroCard herro) {
		this.herro = herro;
	}
}
