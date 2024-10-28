package gameprogress;

public final class Constants {
    public static final String CARD_IDX_ERROR =
            "Number of card is bigger than number of cards from hand.";
    public static final String MANA_FOR_PLACE_CARD_ERROR =
            "Not enough mana to place card on table.";
    public static final String FULL_ROW_ERROR =
            "Cannot place card on table since row is full.";
    public static final String ATTACK_OWN_CARD_ERROR =
            "Attacked card does not belong to the enemy.";
    public static final String CARD_ALREADY_ATTACKED_ERROR =
            "Attacker card has already attacked this turn.";
    public static final String CARD_IS_FROZEN_ERROR =
            "Attacker card is frozen.";
    public static final String HAS_TANKS_ERROR =
            "Attacked card is not of type 'Tank'.";
    public static final String INVALID_COORDINATES_ERROR =
            "The coordinates of the cards are wrong";
    public static final String SPECIAL_CARD_ERROR =
            "Attacker must be a special card.";
    public static final String ATTACK_ENEMY_CARD_ERROR =
            "Attacked card does not belong to the current player.";
    public static final String MANA_FOR_HERO_ABILITY_ERROR =
            "Not enough mana to use hero's ability.";
    public static final String HERO_ALREADY_ATTACKED_ERROR =
            "Hero has already attacked this turn.";
    public static final String ATTACK_OPPONENT_ROW_ERROR =
            "Selected row does not belong to the current player.";
    public static final String ATTACK_OWN_ROW_ERROR =
            "Selected row does not belong to the enemy.";
    public static final String GAME_ENDED =
            "Game ended";

    public static final String[] CARDS_ON_BACK_ROW = {"Sentinel", "Berserker",
            "The Cursed One", "Disciple"};
    public static final String[] SPECIAL_CARDS = {"Disciple", "The Cursed One",
            "The Ripper", "Miraj"};
    public static final String[] TANKS = {"Goliath", "Warden"};

    public static final int NUMBER_OF_PLAYERS = 2;
    public static final int TABLE_ROWS = 4;
    public static final int TABLE_COLS = 5;
    public static final int MAX_RECEIVED_MANA_PER_ROUND = 10;

    private Constants() {

    }
}
