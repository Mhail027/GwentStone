package org.poo.cards;

import java.util.ArrayList;

public class HeroCard extends Card {
    private static final int HERO_HEALTH = 30;

    public HeroCard() {
        health = HERO_HEALTH;
    }

    public HeroCard(final HeroCard hero) {
        super(hero);
    }

    /**
     * General Kocioraw uses his ability of improving with +1 the attack
     * damage of the cards from a row.
     */
    public void useGeneralKociorawAbility(final ArrayList<RegularCard> affectedRow) {
        // Verify if is Kocioraw.
        if (!name.equals("General Kocioraw")) {
            return;
        }

        // Use the ability.
        for (RegularCard card : affectedRow) {
            card.addAttackDamage(1);
        }
       usedAttack = true;
    }

    /**
     * King Mudface uses his ability of improving with +1 the health
     * of the cards from a row.
     */
    public void useKingMudfaceAbility(final ArrayList<RegularCard> affectedRow) {
        // Verify if it's King Mudface.
        if (!name.equals("King Mudface")) {
            return;
        }

        // Use the ability.
        for (RegularCard card : affectedRow) {
            card.addHealth(1);
        }
        usedAttack = true;
    }

    /**
     * Lord Royce uses his ability of freezing all the cards from a row.
     */
    public void useLordRoyceAbility(final ArrayList<RegularCard> affectedRow) {
        // Verify if it's Lord Royce.
        if (!name.equals("Lord Royce")) {
            return;
        }

        // Use the ability.
        for (RegularCard card : affectedRow) {
            card.setFrozen(true);
        }
        usedAttack = true;

    }

    /**
     * Empress Thorina uses his ability of eliminating the card with
     * the highest health from a row.
     */
    public void useEmpressThorinaAbility(final ArrayList<RegularCard> affectedRow) {
        // Verify if it's Empress Thorina.
        if (!name.equals("Empress Thorina")) {
            return;
        }

        // Find the card with the highest health fromm the row.
        RegularCard highestHealthCard = affectedRow.getFirst();
        int cardIdx = 0, colIdx = 0;
        for (RegularCard card : affectedRow) {
            if (card.getHealth() > highestHealthCard.getHealth()) {
                highestHealthCard = card;
                cardIdx = colIdx;
            }
            colIdx++;
        }

        // Use the ability.
        affectedRow.remove(cardIdx);
        usedAttack = true;
    }
}
