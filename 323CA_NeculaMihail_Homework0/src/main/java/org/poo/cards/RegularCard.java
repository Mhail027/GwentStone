package org.poo.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;

public final class RegularCard extends Card {
    private int attackDamage;

    @JsonIgnore
    private boolean frozen = false;

    public RegularCard() {
    }

    public RegularCard(final RegularCard card) {
        super(card);
        attackDamage = card.attackDamage;
        frozen = card.frozen;
    }

    /**
     * Increase the attack damage.
     */
    public void addAttackDamage(final int attDamage) {
        this.attackDamage += attDamage;
    }

    /**
     * Increase the health value .
     */
    public void addHealth(final int health) {
        this.health += health;
    }

    /**
     * Verify if a card is a tank.
     *
     * @return true, if the card is a tank
     *         false, in contrary case
     */
    @JsonIgnore
    public boolean isTank() {
        for (String tank : Constants.TANKS) {
            if (name.equals(tank)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verify if a card is special.
     * @return true, if the card is special
     *         false, in contrary case
     */
    @JsonIgnore
    public boolean isSpecial() {
        for (String specialCard : Constants.SPECIAL_CARDS) {
            if (name.equals(specialCard)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Disciple uses his ability of adding +2 at health of a card.
     */
    public void useDiscipleAbility(final RegularCard card) {
        // Verify if it's Disciple.
        if (!name.equals("Disciple")) {
            return;
        }

        // Use the ability.
        card.health += 2;
        usedAttack = true;
    }

    /**
     * The Ripper uses his ability of decreasing with 2 the attackDamage
     * of a card. Negative attack damage is considered 0.
     */
    public void useTheRipperAbility(final RegularCard card) {
        // Verify if it's The Ripper.
        if (!name.equals("The Ripper")) {
            return;
        }

        // Use the ability.
        card.attackDamage -= 2;
        if (card.attackDamage < 0) {
            card.attackDamage = 0;
        }
        usedAttack = true;
    }

    /**
     * Miraj uses his ability of swapping his health value with
     * one of other card.
     */
    public void useMirajAbility(final RegularCard card) {
        // Verify if it's Miraj.
        if (!name.equals("Miraj")) {
            return;
        }

        // Use the ability.
        int tmp = card.health;
        card.health = health;
        health = tmp;
        usedAttack = true;
    }

    /**
     * The Cursed One uses his ability of swapping the attack damage
     * and the health, between them, for a card.
     */
    public void useTheCursedOneAbility(final RegularCard card) {
        // Verify if it's The Cursed One.
        if (!name.equals("The Cursed One")) {
            return;
        }

        // Use the ability.
        int newAttackDamage = card.health;
        card.health = card.attackDamage;
        card.attackDamage = newAttackDamage;
        usedAttack = true;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(final boolean frozen) {
        this.frozen = frozen;
    }
}
