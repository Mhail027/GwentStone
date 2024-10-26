package cards;

public class HeroCard extends Card {
    private static final int HERO_HEALTH = 30;

    public HeroCard() {
        health = HERO_HEALTH;
    }

    public HeroCard(final HeroCard hero) {
        super(hero);
    }
}
