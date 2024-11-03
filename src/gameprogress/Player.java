package gameprogress;

import cards.HeroCard;
import cards.RegularCard;

public final class Player {
    private final int idx;
    private int mana;
    private final Deck handDeck;
    private final Deck tableDeck;
    private final  HeroCard hero;

    public Player(final int idx, final Deck tableDeck, final HeroCard hero) {
        this.idx = idx;
        this.mana = 0;
        this.handDeck = new Deck(null);
        this.tableDeck = tableDeck;
        this.hero = new HeroCard(hero);
    }

    /**
     * The player receives mana.
     *
     * @param receivedMana the mana which he receives it
     */
    public void receivesMana(final int receivedMana) {
        this.mana += receivedMana;
    }

    /**
     * The player spends mana.
     *
     * @param usedMana the mana which he uses it
     */
    public void usesMana(final int usedMana) {
        this.mana -= usedMana;
    }

    /**
     * Verify if a player has tanks.
     *
     * @return true, if the player has tanks
     *         false, in contrary case
     */
    public boolean hasTanks(final GameTable table) {
        int frontRow = (idx == 1) ? 2 : 1;

        for (RegularCard card : table.getRow(frontRow)) {
            if (card.isTank()) {
                return true;
            }
        }
        return false;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public Deck getHandDeck() {
        return handDeck;
    }

    public Deck getTableDeck() {
        return tableDeck;
    }

    public HeroCard getHero() {
        return hero;
    }

}
