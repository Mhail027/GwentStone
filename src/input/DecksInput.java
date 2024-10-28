package input;

import cards.GameCard;

import java.util.ArrayList;

public final class DecksInput {
    private int nrCardsInDeck;
    private int nrDecks;
    private ArrayList<ArrayList<GameCard>> decks;

    public DecksInput() {
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

    public ArrayList<ArrayList<GameCard>> getDecks() {
        return decks;
    }

    public void setDecks(final ArrayList<ArrayList<GameCard>> decks) {
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

    /**
     * Return the deck which is found at the wanted idx in the set of decks.
     */
    public ArrayList<GameCard> getDeck(final int deckIdx) {
        return decks.get(deckIdx);
    }
}
