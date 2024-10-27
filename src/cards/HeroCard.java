package cards;

import java.util.ArrayList;

public class HeroCard extends Card {
    private static final int HERO_HEALTH = 30;

    public HeroCard() {
        health = HERO_HEALTH;
    }

    public HeroCard(final HeroCard hero) {
        super(hero);
    }

    public void useKociorawAbility(ArrayList<GameCard> affectedRow) {
        for (GameCard card : affectedRow) {
            card.addAttackDamage(1);
        }
    }

    public void useKingMudfaceAbility(ArrayList<GameCard> affectedRow) {
        for (GameCard card : affectedRow) {
            card.addHealth(1);
        }
    }

    public void useLordRoyceAbility(ArrayList<GameCard> affectedRow) {
        for (GameCard card : affectedRow) {
            card.setFrozen(1);
        }
    }

    public void useEmpressThorinaAbility(ArrayList<GameCard> affectedRow) {
        GameCard healthiestCard = affectedRow.get(0);
        int healthiestCardIdx = 0, colIdx = 0;

        for (GameCard card : affectedRow) {
            if (card.getHealth() > healthiestCard.getHealth()) {
                healthiestCard = card;
                healthiestCardIdx = colIdx;
            }
            colIdx++;
        }

        affectedRow.remove(healthiestCardIdx);
    }
}
