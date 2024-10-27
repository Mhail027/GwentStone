package cards;

import com.fasterxml.jackson.annotation.JsonIgnore;

public final class GameCard extends Card {
    private int attackDamage;
    @JsonIgnore
    private int frozen = 0;

    public GameCard() {
    }

    public GameCard(final GameCard card) {
        super(card);
        attackDamage = card.attackDamage;
        frozen = card.frozen;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getFrozen() {
        return frozen;
    }

    public void setFrozen(int frozen) {
        this.frozen = frozen;
    }

    @Override
    public String toString() {
        return "CardInput{"
                +  "mana="
                + mana
                +  ", attackDamage="
                + attackDamage
                + ", health="
                + health
                +  ", description='"
                + description
                + '\''
                + ", colors="
                + colors
                + ", name='"
                +  ""
                + name
                + '\''
                + '}';
    }

    public void useDiscipleAbility(GameCard card) {
        card.health += 2;
    }

    public void useTheRipperAbility(GameCard card) {
        card.attackDamage -= 2;
        if (card.attackDamage < 0) {
            card.attackDamage = 0;
        }
    }

    public void useMirajAbility(GameCard card) {
        int tmp = card.health;
        card.health = health;
        health = tmp;
    }

    public void useTheCursedOneAbility(GameCard card) {
        int newAttackDamage = card.health;
        card.health = card.attackDamage;
        card.attackDamage = newAttackDamage;
    }

    public void addAttackDamage(final int Attackdamage) {
        this.attackDamage += Attackdamage;
    }

    public void addHealth(final int health) {
        this.health += health;
    }

}
