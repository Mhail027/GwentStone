package cards;

import com.fasterxml.jackson.annotation.JsonIgnore;

public final class GameCard extends Card {
    private int attackDamage;
    @JsonIgnore
    private int usedAtack = 0;
    @JsonIgnore
    private int frozen = 0;

    public GameCard() {
    }

    public GameCard(final GameCard card) {
        super(card);
        attackDamage = card.attackDamage;
        usedAtack = card.usedAtack;
        frozen = card.frozen;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getUsedAtack() {
        return usedAtack;
    }

    public void setUsedAtack(int usedAtack) {
        this.usedAtack = usedAtack;
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
}
