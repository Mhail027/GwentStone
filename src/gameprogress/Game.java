package gameprogress;

import cards.GameCard;
import cards.HeroCard;
import input.Input;
import input.StartGameInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public final class Game {
    private static Game instance;

    private int roundIdx;
    private int currPlayer;
    private int firstPlayerOfRound;
    private final Player[] players = new Player[Constants.NUMBER_OF_PLAYERS + 1];
    private ArrayList<ArrayList<GameCard>> table;

    private int playerOneWins = 0;
    private int playerTwoWins = 0;
    private int gamesPlayed = 0;
    private boolean ended;

    private Game() {
    }

    /**
     * Make sure that this class is SingleTone.
     */
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }

        return instance;
    }

    /**
     * Alternative to a constructor, without to break SingleTone rules.
     */
    public static Game init(final Input inputData, final StartGameInput startGameData,
                            final int gameIdx) {
        Game o = getInstance();

        // If this is the first game, we must reset the stats.
        if (gameIdx == 1) {
            o.resetStats();
        }

        // Note some "boring", but needed data.
        o.roundIdx = 1;
        o.currPlayer = startGameData.getStartingPlayer();
        o.firstPlayerOfRound = o.currPlayer;
        o.ended = false;

        // First player receives the deck and the herro.
        int playerOneDeckIdx = startGameData.getPlayerOneDeckIdx();
        ArrayList<GameCard> playerOneDeck = inputData.getPlayerOneDecks().getDeck(playerOneDeckIdx);
        HeroCard playerOneHerro = startGameData.getPlayerOneHero();
        o.players[1] = new Player(1, playerOneDeck, playerOneHerro);

        // Second player receives the deck and the herro.
        int playerTwoDeckIdx = startGameData.getPlayerTwoDeckIdx();
        ArrayList<GameCard> playerTwoDeck = inputData.getPlayerTwoDecks().getDeck(playerTwoDeckIdx);
        HeroCard playerTwoHerro = startGameData.getPlayerTwoHero();
        o.players[2] = new Player(2, playerTwoDeck, playerTwoHerro);

        // Everyone shuffle his deck.
        int shuffleSeed = startGameData.getShuffleSeed();
        for (int i = 1; i <= Constants.NUMBER_OF_PLAYERS; ++i) {
            o.shuffleDeck(o.players[i].getTableDeck(), shuffleSeed);
        }

        // Create the game table.
        o.createTable();

        return o;
    }

    /**
     * Reset the stats about the games played. until now.
     */
    public void resetStats() {
        playerOneWins = 0;
        playerTwoWins = 0;
        gamesPlayed = 0;
    }

    /**
     * The cards from a deck are shuffled using a seed.
     */
    private void shuffleDeck(final ArrayList<GameCard> deck, final int shuffleSeed) {
        Collections.shuffle(deck, new Random(shuffleSeed));
    }

    /**
     * Allocate memory for the table.
     */
    private void createTable() {
        table = new ArrayList<>();
        for (int i = 0; i < Constants.TABLE_ROWS; ++i) {
            table.add(new ArrayList<>());
        }
    }

    /**
     * Return the player which has the given idx.
     */
    public Player getPlayer(final int idx) {
        return players[idx];
    }

    public int getOpponentIdx() {
        return currPlayer % 2 + 1;
    }

    /**
     * Do the needed actions as a new round to can start.
     */
    public void startRound() {
        playersReceiveMana();
        playersDrawCard();
        everyCardCanAttack();
    }

    /**
     * At the start of every round, every player receives mana.
     * The amount of mana is equal with the round number.
     * Though, it's applied a ceiling for the mana which a player
     * can receive it per round.
     */
    private void playersReceiveMana() {
        int mana = roundIdx;
        if (mana > Constants.MAX_RECEIVED_MANA_PER_ROUND) {
            mana = Constants.MAX_RECEIVED_MANA_PER_ROUND;
        }

        for (int i = 1; i <= Constants.NUMBER_OF_PLAYERS; ++i) {
            players[i].receivesMana(mana);
        }
    }

    /**
     * Every player draw a card from his table deck, if he can.
     */
    private void playersDrawCard() {
        for (int i = 1; i <= Constants.NUMBER_OF_PLAYERS; ++i) {
            if (!players[i].getTableDeck().isEmpty()) {
                GameCard drawnCard = players[i].getTableDeck().get(0);
                players[i].getTableDeck().remove(0);
                players[i].getHandDeck().add(drawnCard);
            }
        }
    }

    /**
     * Mark that every card can use his attack/ability.
     */
    private void everyCardCanAttack() {
        // The cards from table
        for (ArrayList<GameCard> row : table) {
            for (GameCard card : row) {
                card.setUsedAttack(false);
            }
        }

        // The heroes.
        for (int i = 1; i <= Constants.NUMBER_OF_PLAYERS; ++i) {
            players[i].getHero().setUsedAttack(false);
        }
    }

    /**
     * The turn of current player ends.
     */
    public void goNextTurn() {
        // Unfreeze the cards of current player.
        unfreezeCardsOfPlayer(currPlayer);

        // Go at next person.
        currPlayer += 1;
        if (currPlayer > Constants.NUMBER_OF_PLAYERS) {
            currPlayer = 1;
        }

        // Verify if we must start a new round.
        if (currPlayer == firstPlayerOfRound) {
            roundIdx++;
            startRound();
        }
    }

    /**
     * Unfreeze all the cards of a player from table.
     */
    private void unfreezeCardsOfPlayer(final int playerIdx) {
        // We'll unfreeze all frozen cards from 2 rows.
        // We must determinate with which row we start
        int startRow = (playerIdx == 1) ? 2 : 0;

        // Unfreeze.
        for (int i = 0; i <= 1; ++i) {
            for (GameCard card : table.get(startRow + i)) {
                card.setFrozen(false);
            }
        }
    }

    /**
     * Current player puts a card on table.
     *
     * @param cardIdx position of card in hand deck
     * @return null, if the card can be put on table
     *         error, in contrary case
     */
    public String placeCard(final int cardIdx) {
        // Verify if the index of the card is valid.
        if (cardIdx >= players[currPlayer].getHandDeck().size()) {
            return Constants.CARD_IDX_ERROR;
        }

        // Take the wanted card from handDeck.
        GameCard card = players[currPlayer].getHandDeck().get(cardIdx);

        // Verify if we have enough mana for this card.
        if (card.getMana() > players[currPlayer].getMana()) {
            return Constants.MANA_FOR_PLACE_CARD_ERROR;
        }

        // Verify if it's space on the row.
        int rowIdx = findRowForCard(currPlayer, card.getName());
        if (table.get(rowIdx).size() >= Constants.TABLE_COLS) {
            return Constants.FULL_ROW_ERROR;
        }

        // Place the card on table.
        players[currPlayer].usesMana(card.getMana());
        players[currPlayer].getHandDeck().remove(cardIdx);
        table.get(rowIdx).add(card);
        return null;
    }

    /**
     * Find the row on which a player must put the wanted card.
     */
    private int findRowForCard(final int playerIdx, final String cardName) {
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

    /**.
     * A card of current player attacks a card of the opponent.
     * Both cards must be placed on table.
     *
     * @param attacker coordinates of card which attacks
     * @param attacked coordinates of card which is attacked
     * @return null, if the attack took place
     *         error, in contrary case
     */
    public String cardUsesAttack(final Coordinates attacker, final Coordinates attacked) {
        // Verify if the attacked is a card of the opponent.
        if (!tableCardBelongsToPerson(attacked, getOpponentIdx())) {
            return Constants.ATTACK_OWN_CARD_ERROR;
        }

        // Take the attacker card.
        GameCard attackerCard = table.get(attacker.getX()).get(attacker.getY());
        // Take the attacked card.
        GameCard attackedCard = table.get(attacked.getX()).get(attacked.getY());

        // Verify if the card didn't attack already in this round.
        if (attackerCard.isUsedAttack()) {
            return Constants.CARD_ALREADY_ATTACKED_ERROR;
        }
        // Verify if the attacker is not frozen.
        if (attackerCard.isFrozen()) {
            return Constants.CARD_IS_FROZEN_ERROR;
        }
        // Verify that if the opponent has tanks, we attack one of them.
        if (!cardIsTank(attackedCard.getName()) && playerHasTanks(getOpponentIdx())) {
                return Constants.HAS_TANKS_ERROR;
        }

        // The attack takes place.
        attackedCard.decreaseHealth(attackerCard.getAttackDamage());
        attackerCard.setUsedAttack(true);

        // Verify if the attacked died.
        if (attackedCard.getHealth() <= 0) {
            table.get(attacked.getX()).remove(attacked.getY());
        }

        return null;
    }

    /**
     * Verify if a card from the table belongs to the specified player
     *
     * @param card coordinates of the card
     * @param player idx of teh player
     * @return true, if the card belongs to the player
     *         false, in contrary case
     */
    private boolean tableCardBelongsToPerson(final Coordinates card, final int player) {
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
        return card.getY() < table.get(card.getX()).size();
    }

    /**
     * Verify if the given card is a tank.
     *
     * @param card name of the card
     * @return true, if the card is a tank
     *         false, in contrary case
     */
    private boolean cardIsTank(final String card) {
        for (String tank : Constants.TANKS) {
            if (card.equals(tank)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verify if a player has tanks.
     *
     * @param player idx of player
     * @return true, if the player has tanks
     *         false, in contrary case
     */
    private boolean playerHasTanks(final int player) {
        int playerFrontRow = (player == 1) ? 2 : 1;
        for (GameCard card : table.get(playerFrontRow)) {
           for (String tank : Constants.TANKS) {
               if (card.getName().equals(tank)) {
                   return true;
               }
            }
        }
        return false;
    }

    /**
     * Verify if is a card on the table at the given position.
     *
     * @param position the coordinates of the position
     * @return true, if the coordinates are valid
     *         false, in contrary case
     */
    private boolean coordinatesAreValid(final Coordinates position) {
        return ((tableCardBelongsToPerson(position, 1) || tableCardBelongsToPerson(position, 2)));
    }

    /**
     * @param position coordinates of the position
     * @return the card at the specified position from table
     */
    public GameCard getCard(final Coordinates position) {
        if (coordinatesAreValid(position)) {
            return table.get(position.getX()).get(position.getY());
        }
        return null;
    }

    /**
     * A card of current player uses his ability on other card.
     * Both cards must be placed on table.
     *
     * @param attacker coordinates of card which attacks
     * @param attacked coordinates of card which is attacked
     * @return null, if the attacker uses his ability
     *         error, in contrary case
     */
    public String cardUsesAbility(final Coordinates attacker, final Coordinates attacked) {
        // Verify if the coordinates are valid.
        if (!coordinatesAreValid(attacker) || !coordinatesAreValid(attacked)) {
            return Constants.INVALID_COORDINATES_ERROR;
        }

        // Take the attacker card.
        GameCard attackerCard = table.get(attacker.getX()).get(attacker.getY());
        // Take the attacked card.
        GameCard attackedCard = table.get(attacked.getX()).get(attacked.getY());

        // Verify if the attacker is a special card.
        if (!isSpecialCard(attackerCard.getName())) {
            return Constants.SPECIAL_CARD_ERROR;
        }
        // Verify if the attacker is not frozen.
        if (attackerCard.isFrozen()) {
            return Constants.CARD_IS_FROZEN_ERROR;
        }
        // Verify if the card didn't attack already in this round.
        if (attackerCard.isUsedAttack()) {
            return Constants.CARD_ALREADY_ATTACKED_ERROR;
        }

        // Verify if the attacker is Disciple.
        if (attackerCard.getName().equals("Disciple")) {
            // Verify if the attacked is a card of the current player.
            if (!tableCardBelongsToPerson(attacked, currPlayer)) {
                return Constants.ATTACK_ENEMY_CARD_ERROR;
            }

            // Use the ability.
            attackerCard.useDiscipleAbility(attackedCard);
            attackerCard.setUsedAttack(true);
            return null;
        }

        // If we get here, that means that we want to attack the opponent.
        // Verify if the attacked is a card of opponent.
        if (!tableCardBelongsToPerson(attacked, getOpponentIdx())) {
            return Constants.ATTACK_OWN_CARD_ERROR;
        }
        // Verify that if the opponent has tanks, we attack one of them.
        if (!cardIsTank(attackedCard.getName()) && playerHasTanks(getOpponentIdx())) {
            return Constants.HAS_TANKS_ERROR;
        }

        // Use the ability.
        attackerCard.setUsedAttack(true);
        if (attackerCard.getName().equals("The Ripper")) {
            attackerCard.useTheRipperAbility(attackedCard);
        } else if (attackerCard.getName().equals("Miraj")) {
            attackerCard.useMirajAbility(attackedCard);
        } else {
            attackerCard.useTheCursedOneAbility(attackedCard);
        }

        // Verify if the attacked died.
        if (attackedCard.getHealth() == 0) {
            table.get(attacked.getX()).remove(attacked.getY());
        }

        return null;
    }

    /**
     * Verify if a card is special.
     *
     * @param card name of the card
     * @return true, if the card is special
     *         false, in contrary case
     */
    private boolean isSpecialCard(final String card) {
        for (String specialCard : Constants.SPECIAL_CARDS) {
            if (card.equals(specialCard)) {
                return true;
            }
        }
        return false;
    }

    /**
     * A card of current player attacks the herro of the enemy.
     * The card which attacks must be placed on table.
     *
     * @param attacker coordinates of card which attacks
     * @return null, if the attack took place
     *         error, in contrary case
     *         GAME_ENDED, if the current player killed the enemy hero
     */
    public String useAttackOnHero(final Coordinates attacker) {
        // Verify if the coordinates are valid.
        if (!coordinatesAreValid(attacker)) {
            return Constants.INVALID_COORDINATES_ERROR;
        }

        // Take the attacker card.
        GameCard attackerCard = table.get(attacker.getX()).get(attacker.getY());
        // Get the hero of the opponent.
        HeroCard opponentHero = players[getOpponentIdx()].getHero();

        // Verify if the attacker is not frozen.
        if (attackerCard.isFrozen()) {
            return Constants.CARD_IS_FROZEN_ERROR;
        }
        // Verify if the card didn't attack already in this round.
        if (attackerCard.isUsedAttack()) {
            return Constants.CARD_ALREADY_ATTACKED_ERROR;
        }
        // Verify if there's a tank on front row of the opponent.
        if (playerHasTanks(getOpponentIdx())) {
            return Constants.HAS_TANKS_ERROR;
        }

        // The attack can take place.
        opponentHero.decreaseHealth(attackerCard.getAttackDamage());
        // Verify if the hero died.
        if (opponentHero.getHealth() <= 0) {
            return Constants.GAME_ENDED;
        }

        return null;
    }

    /**
     * The herro of current player use his ability
     * @param affectedRow idx of the row which will be affected
     * @return null, if the hero uses his ability
     *         error, in contrary case
     */
    public String useHeroAbility(final int affectedRow) {
        // Get the herro card.
        HeroCard hero = players[currPlayer].getHero();

        // Verify if the current player has enough mana to use the hero ability.
        if (hero.getMana() > players[currPlayer].getMana()) {
            return Constants.MANA_FOR_HERO_ABILITY_ERROR;
        }
        // Verify if the card didn't attack already in this round.
        if (hero.isUsedAttack()) {
            return Constants.HERO_ALREADY_ATTACKED_ERROR;
        }

        // Use the ability.
        if (hero.getName().equals("General Kocioraw") || hero.getName().equals("King Mudface")) {
            // Verify if the selected row is valid.
            if ((currPlayer == 1 && affectedRow < 2) || (currPlayer == 2 && affectedRow > 1)) {
                return Constants.ATTACK_OPPONENT_ROW_ERROR;
            }

            if (hero.getName().equals("General Kocioraw")) {
                hero.useGeneralKociorawAbility(table.get(affectedRow));
            } else {
                hero.useKingMudfaceAbility(table.get(affectedRow));
            }
        } else {
            // Verify if the selected row is valid.
            if ((currPlayer == 1 && affectedRow > 1) || (currPlayer == 2 && affectedRow < 2)) {
                return Constants.ATTACK_OWN_ROW_ERROR;
            }

            if (hero.getName().equals("Lord Royce")) {
                hero.useLordRoyceAbility(table.get(affectedRow));
            } else {
                hero.useEmpressThorinaAbility(table.get(affectedRow));
            }

        }
        players[currPlayer].usesMana(hero.getMana());
        hero.setUsedAttack(true);

        return null;
    }

    /**
     * @return ArrayList with the frozen cards from table
     */
    public ArrayList<GameCard> getFrozenCards() {
        ArrayList<GameCard> frozenCards = new ArrayList<>(0);
        for (ArrayList<GameCard> row : table) {
            for (GameCard card : row) {
                if (card.isFrozen()) {
                    frozenCards.add(card);
                }
            }
        }
        return frozenCards;
    }

    /**
     * Update the statistics about the games played.
     */
    public void currPlayerWon() {
        gamesPlayed++;
        ended = true;
        if (currPlayer == 1) {
            playerOneWins++;
        } else {
            playerTwoWins++;
        }
    }

    public int getCurrPlayer() {
        return currPlayer;
    }

    public ArrayList<ArrayList<GameCard>> getTable() {
        return table;
    }

    public int getPlayerOneWins() {
        return playerOneWins;
    }

    public int getPlayerTwoWins() {
        return playerTwoWins;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public boolean isEnded() {
        return ended;
    }
}

