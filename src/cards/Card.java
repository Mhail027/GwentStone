package cards;

import java.util.ArrayList;

public class Card {
    protected int mana;
    protected int health;
    protected String description;
    protected ArrayList<String> colors;
    protected String name;

    public Card() {
    }

    public Card(final Card card) {
        mana = card.mana;
        health = card.health;
        description = card.description;
        colors = card.colors;
        name = card.name;
    }

    public final int getMana() {
        return mana;
    }

    public final void setMana(final int mana) {
        this.mana = mana;
    }

    public final int getHealth() {
        return health;
    }

    public final void setHealth(final int health) {
        this.health = health;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(final String description) {
        this.description = description;
    }

    public final ArrayList<String> getColors() {
        return colors;
    }

    public final void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }
}
