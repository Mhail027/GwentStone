package gameprogress;

import cards.GameCard;
import cards.HeroCard;

import java.util.ArrayList;

public final class Player {
    private int idx;
    private int mana;
    private ArrayList<GameCard> handDeck;
    private ArrayList<GameCard> tableDeck;
    private HeroCard hero;

    public Player(final int idx, final ArrayList<GameCard> tableDeck, final HeroCard hero) {
        this.idx = idx;
        this.mana = 0;
        this.handDeck = new ArrayList<>(0);
        this.tableDeck = new ArrayList<>(0);
        for (GameCard card : tableDeck) {
            this.tableDeck.add(new GameCard(card));
        }
        this.hero = new HeroCard(hero);
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(final int idx) {
        this.idx = idx;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     * The player receives mana.
     */
    public void receivesMana(final int mana) {
        this.mana += mana;
    }

    /**
     * The player spends mana.
     */
    public void usesMana(final int mana) {
        this.mana -= mana;
    }

    public ArrayList<GameCard> getHandDeck() {
        return handDeck;
    }

    public void setHandDeck(final ArrayList<GameCard> handDeck) {
        this.handDeck = handDeck;
    }

    public ArrayList<GameCard> getTableDeck() {
        return tableDeck;
    }

    public void setTableDeck(final ArrayList<GameCard> tableDeck) {
        this.tableDeck = tableDeck;
    }

    public HeroCard getHerro() {
        return hero;
    }

    public void setHero(final HeroCard hero) {
        this.hero = hero;
    }
}
