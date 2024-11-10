package org.poo.input;

import org.poo.cards.RegularCard;

import java.util.ArrayList;

public final class DecksInput {
    private int nrCardsInDeck;
    private int nrDecks;
    private ArrayList<ArrayList<RegularCard>> decks;

    public DecksInput() {
    }

    /**
     * Return the deck which is found at the wanted idx in the set of decks.
     */
    public ArrayList<RegularCard> getDeck(final int deckIdx) {
        return decks.get(deckIdx);
    }

    public int getNrCardsInDeck() {
        return nrCardsInDeck;
    }

    public void setNrCardsInDeck(final int nrCardsInDeck) {
        this.nrCardsInDeck = nrCardsInDeck;
    }

    public int getNrDecks() {
        return nrDecks;
    }

    public void setNrDecks(final int nrDecks) {
        this.nrDecks = nrDecks;
    }

    public ArrayList<ArrayList<RegularCard>> getDecks() {
        return decks;
    }

    public void setDecks(final ArrayList<ArrayList<RegularCard>> decks) {
        this.decks = decks;
    }

    @Override
    public String toString() {
        return "InfoInput{"
                + "nr_cards_in_deck="
                + nrCardsInDeck
                +  ", nr_decks="
                + nrDecks
                + ", decks="
                + decks
                + '}';
    }
}
