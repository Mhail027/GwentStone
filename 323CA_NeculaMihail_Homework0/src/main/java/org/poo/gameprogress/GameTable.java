package org.poo.gameprogress;

import org.poo.cards.RegularCard;
import org.poo.input.Coordinates;

import java.util.ArrayList;

public final class GameTable {
    private static GameTable instance;

    private ArrayList<ArrayList<RegularCard>> cards;

    private GameTable() {

    }

    private static GameTable getInstance() {
        if (instance == null) {
            instance = new GameTable();
        }
        return instance;
    }

    /**
     * Alternative to a constructor, without to break Singleton rules.
     */
    public static GameTable init() {
        GameTable o = getInstance();

        o.cards = new ArrayList<>();
        for (int i = 0; i < Constants.TABLE_ROWS; ++i) {
            o.cards.add(new ArrayList<>());
        }

        return o;
    }

    /**
     * Add a card at the final of a row.
     *
     * @param card the new card
     * @param rowIdx the row's number
     */
    public void addCard(final RegularCard card, final int rowIdx) {
        cards.get(rowIdx).add(card);
    }

    /**
     * Find the card from a specified position.
     *
     * @param card coordinates of the card
     * @return the card
     */
    public RegularCard getCard(final Coordinates card) {
        if (coordinatesAreValid(card)) {
            return cards.get(card.getX()).get(card.getY());
        }
        return null;
    }

    /**
     * Remove a card from the table.
     *
     * @param card coordinates of the card
     */
    public void removeCard(final Coordinates card) {
        if (coordinatesAreValid(card)) {
            cards.get(card.getX()).remove(card.getY());
        }
    }

    /**
     * Get a row from the table
     *
     * @param rowIdx the row's number
     * @return the row
     */
    public ArrayList<RegularCard> getRow(final int rowIdx) {
        return cards.get(rowIdx);
    }

    /**
     * Find the number of cards from a row.
     *
     * @param rowIdx the row's number
     * @return the number of cards from the specified row
     */
    public int getRowSize(final int rowIdx) {
        return cards.get(rowIdx).size();
    }

    /**
     * Mark that every card can use his attack/ability.
     */
    public void everyoneCanAttack() {
        for (ArrayList<RegularCard> row : cards) {
            for (RegularCard card : row) {
                card.setUsedAttack(false);
            }
        }
    }

    /**
     * Unfreeze all cards from a row.
     *
     * @param rowIdx the row's number
     */
    public void unfreezeRow(final int rowIdx) {
        for (RegularCard card : cards.get(rowIdx)) {
            card.setFrozen(false);
        }
    }

    /**
     * @return ArrayList with the frozen cards from table
     */
    public ArrayList<RegularCard> getFrozenCards() {
        ArrayList<RegularCard> frozenCards = new ArrayList<>(0);

        for (ArrayList<RegularCard> row : cards) {
            for (RegularCard card : row) {
                if (card.isFrozen()) {
                    frozenCards.add(card);
                }
            }
        }

        return frozenCards;
    }

    /**
     * Find the row on which a player must put the wanted card.
     */
    public int findRowForCard(final int playerIdx, final String cardName) {
        // Find the rows of the player.
        int backRow = (playerIdx == 1) ? 3 : 0;
        int frontRow = (playerIdx == 1) ? 2 : 1;

        for (String name : Constants.CARDS_ON_BACK_ROW) {
            if (cardName.equals(name)) {
                return backRow;
            }
        }
        return frontRow;
    }

    /**
     * Verify if the given card belongs to the specified player.
     *
     * @param card coordinates of the card
     * @param player idx of teh player
     * @return true, if the card belongs to the player
     *         false, in contrary case
     */
    public boolean cardBelongsToPerson(final Coordinates card, final int player) {
        // Coordinates must be positive or 0.
        if (card.getX() < 0 || card.getY() < 0) {
            return false;
        }

        // Verify if the card is on a correct row.
        if (player == 1 && card.getX() != 3 && card.getX() != 2) {
            return false;
        }
        if (player == 2 && card.getX() != 1 && card.getX() != 0) {
            return false;
        }

        // Verify if at the given position exists a card.
        return card.getY() < getRowSize(card.getX());
    }

    /**
     * Verify if is a card at the given position.
     *
     * @param position the coordinates of the position
     * @return true, if the coordinates are valid
     *         false, in contrary case
     */
    public boolean coordinatesAreValid(final Coordinates position) {
        // Verify if exists the row.
        if (position.getX() >= cards.size()) {
            return false;
        }

        // Verify the position.
        ArrayList<RegularCard> row = getRow(position.getX());
        return position.getY() < row.size();
    }

    public ArrayList<ArrayList<RegularCard>> getCards() {
        return cards;
    }
}
