package gameprogress;

import cards.RegularCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public final class Deck {
    private int nrCards;
    private final ArrayList<RegularCard> cards = new ArrayList<>();

    /**
     * This constructor is based on deep copy.
     *
     * @param cards list with cards of the deck
     */
    public Deck(final ArrayList<RegularCard> cards) {
        if (cards == null) {
            nrCards = 0;
        } else {
            nrCards = cards.size();
            for (RegularCard card : cards) {
                this.cards.add(new RegularCard(card));
            }
        }
    }

    /**
     * Verify if the deck has cards.
     *
     * @return true, if it has
     *         false, in contrary case
     */
    public boolean hasCards() {
        return (nrCards > 0);
    }

    /**
     * Add a new card at the final of the deck.
     */
    public void addCard(final RegularCard card) {
        cards.add(card);
        nrCards++;
    }

    /**
     * Get a card from the deck, without to remove it.
     *
     * @param idx the position of the card in the list
     * @return the card
     */
    public RegularCard getCard(final int idx) {
        if (idx < 0 || idx >= nrCards) {
            return null;
        }
        return cards.get(idx);
    }

    /**
     * Remove a card from the deck.
     *
     * @param idx the position of the card in list
     */
    public void removeCard(final int idx) {
        if (idx < 0 || idx >= nrCards) {
            return;
        }
        cards.remove(idx);
        nrCards--;
    }

    /**
     * Get a card from the deck and after remove it from there.
     *
     * @param idx the position of the card in list
     * @return the card
     */
    public RegularCard takeCard(final int idx) {
        RegularCard card = getCard(idx);
        removeCard(idx);
        return card;
    }

    /**
     * The cards from a deck are shuffled using a seed.
     */
    public void shuffleCards(final int shuffleSeed) {
        Collections.shuffle(cards, new Random(shuffleSeed));
    }

    public ArrayList<RegularCard> getCards() {
        return cards;
    }
}
