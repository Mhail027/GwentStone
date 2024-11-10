package org.poo.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

public abstract class Card {
    protected int mana;
    protected int health;
    protected String description;
    protected ArrayList<String> colors;
    protected String name;

    @JsonIgnore
    protected boolean usedAttack = false;

    public Card() {
    }

    public Card(final Card card) {
        mana = card.mana;
        health = card.health;
        description = card.description;
        colors = card.colors;
        name = card.name;
        usedAttack = card.usedAttack;
    }

    /**
     * The card loses a part of his health.
     */
    public final void decreaseHealth(final int damage) {
        health -= damage;
    }

    public final int getMana() {
        return mana;
    }

    public final int getHealth() {
        return health;
    }

    public final String getDescription() {
        return description;
    }

    public final ArrayList<String> getColors() {
        return colors;
    }

    public final String getName() {
        return name;
    }

    public final void setUsedAttack(final boolean usedAttack) {
        this.usedAttack = usedAttack;
    }

    public final boolean isUsedAttack() {
        return usedAttack;
    }
}
