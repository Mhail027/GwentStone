package gameprogress;

import cards.GameCard;
import cards.HeroCard;
import input.Input;
import input.StartGameInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public final class Game {
    private static final int NUMBER_OF_PLAYERS = 2;
    private static final int TABLE_ROWS = 4;
    private static final int TABLE_COLS = 5;
    private static final int MAX_RECEIVED_MANA_PER_ROUND = 10;

    private static Game instance;

    private int roundIdx;
    private int playerTurn;
    private int firstPlayerOfRound;
    private Player[] players = new Player[NUMBER_OF_PLAYERS + 1];
    private ArrayList<ArrayList<GameCard>> table;

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
    public static Game init(final Input inputData, final StartGameInput startGameData) {
        Game currGame = getInstance();
        currGame.initHelper(inputData, startGameData);
        return currGame;
    }

    private void initHelper(final Input inputData, final StartGameInput startGameData) {
        // Note some "boring", but needed data.
        roundIdx = 1;
        playerTurn = startGameData.getStartingPlayer();
        firstPlayerOfRound = startGameData.getStartingPlayer();

        // Every player receives the deck and the herro for the current game.
        int playerOneDeckIdx = startGameData.getPlayerOneDeckIdx();
        HeroCard playerOneHerro = startGameData.getPlayerOneHero();
        players[1] = new Player(1, inputData.getPlayerOneDecks().getDeck(playerOneDeckIdx), playerOneHerro);

        int playerTwoDeckIdx = startGameData.getPlayerTwoDeckIdx();
        HeroCard playerTwoHerro = startGameData.getPlayerTwoHero();
        players[2] = new Player(2, inputData.getPlayerTwoDecks().getDeck(playerTwoDeckIdx), playerTwoHerro);

        // Everyone shuffle his deck.
        int shuffleSeed = startGameData.getShuffleSeed();
        for (int i = 1; i <= NUMBER_OF_PLAYERS; ++i) {
            ArrayList<GameCard> playerDeck = players[i].getTableDeck();
            Collections.shuffle(playerDeck, new Random(shuffleSeed));
        }

        // Create space to put the game table.
        table = new ArrayList<>();
        for (int i = 0; i < TABLE_ROWS; ++i)
            table.add(new ArrayList<>());
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public ArrayList<ArrayList<GameCard>> getTable() {
        return table;
    }

    /**
     * Return the player which has the given idx.
     */
    public Player getPlayer(final int idx) {
        return players[idx];

    }

    /**
     * Do the needed actions as a new round to can start:
     * every player receives mana and draws a card.
     */
    public void startRound() {
        playersReceiveMana();
        playersDrawCard();
        everyCardCanAttack();
    }

    private void playersReceiveMana() {
        int receivedMana = roundIdx;
        if (receivedMana > MAX_RECEIVED_MANA_PER_ROUND) {
            receivedMana = MAX_RECEIVED_MANA_PER_ROUND;
        }

        for (int i = 1; i <= NUMBER_OF_PLAYERS; ++i) {
            players[i].receivesMana(receivedMana);
        }
    }

    private void playersDrawCard() {
        for (int i = 1; i <= NUMBER_OF_PLAYERS; ++i) {
            ArrayList<GameCard> tableDeck = players[i].getTableDeck();
            ArrayList<GameCard> handDeck = players[i].getHandDeck();

            if (!tableDeck.isEmpty()) {
                GameCard drawnCard = tableDeck.get(0);
                tableDeck.remove(0);
                handDeck.add(drawnCard);
            }
        }
    }

    private void everyCardCanAttack() {
        for (ArrayList<GameCard> row : table) {
            for (GameCard card : row) {
                card.setUsedAttack(false);
            }
        }

        for (int i = 1; i <= NUMBER_OF_PLAYERS; ++i) {
            players[i].getHero().setUsedAttack(false);
        }
    }

    /**
     * The turn of current player ends.
     */
    public void goNextTurn() {
        // Unfreeze the cards of current player.
        int startRow = 0;
        if (playerTurn == 1) {
            startRow = 2;
        }
        for (int i = startRow; i <= startRow + 1; ++i) {
            ArrayList<GameCard> row = table.get(i);
            for (GameCard card : row) {
                card.setFrozen(false);
            }
        }

        // Go at next person.
        playerTurn += 1;
        if (playerTurn > NUMBER_OF_PLAYERS) {
            playerTurn = 1;
        }

        // Verify if we must start a new round.
        if (playerTurn == firstPlayerOfRound) {
            roundIdx++;
            startRound();
        }
    }

    public int placeCard(final int cardIdx) {
        // Verify if the cardIdx is valid.
        int cardsInHand = players[playerTurn].getHandDeck().size();
        if (cardIdx >= cardsInHand) {
            return 1;
        }

        // Take the wanted card from handDeck.
        GameCard card = players[playerTurn].getHandDeck().get(cardIdx);

        // Verify if we have enough mana for this card.
        if (card.getMana() > players[playerTurn].getMana()) {
            return 2;
        }

        // Verify if it's space on the row.
        int rowIdx = findRowForCard(playerTurn, card.getName());
        if (table.get(rowIdx).size() >= TABLE_COLS) {
            return 3;
        }

        // Place the card on table.
        players[playerTurn].getHandDeck().remove(cardIdx);
        table.get(rowIdx).add(card);
        players[playerTurn].usesMana(card.getMana());

        return 0;
    }

    private int findRowForCard(final int playerIdx, final String cardName) {

        if (cardName.equals("Sentinel") || cardName.equals("Berserker")) {
            return (playerIdx == 1) ? 3 : 0;
        }
        if (cardName.equals("The Cursed One") || cardName.equals("Disciple")) {
            return (playerIdx == 1) ? 3 : 0;
        }

        return (playerIdx == 1) ? 2 : 1;
    }

    public int cardUsesAttack(Coordinates attacker, Coordinates attacked) {
        // Verify if the attacker is a card of the current player.
        if (!tableCardBelongsToPerson(attacker, playerTurn)) {
            return 1;
        }

        // Verify if the attacked is a card of the opponent.
        int opponentIdx = playerTurn % 2 + 1;
        if (!tableCardBelongsToPerson(attacked, opponentIdx)) {
            return 2;
        }

        // Take the attacker card.
        GameCard attackerCard = table.get(attacker.getX()).get(attacker.getY());

        // Verify if the card didn't attack already in this round.
        if (attackerCard.isUsedAttack()) {
            return 3;
        }

        // Verify if the attacker is not frozen.
        if (attackerCard.isFrozen()) {
            return 4;
        }

        // Take the attacked card.
        GameCard attackedCard = table.get(attacked.getX()).get(attacked.getY());

        // If attacked card is not a tank, verify if there's a tank of front row of opponent.
        if (!attackedCard.getName().equals("Goliath") &&  !attackedCard.getName().equals("Warden")) {
            if (playerHasTanks(opponentIdx)) {
                return 5;
            }
        }

        // The attack takes place.
        attackedCard.decreaseHealth(attackerCard.getAttackDamage());
        attackerCard.setUsedAttack(true);
        if (attackedCard.getHealth() <= 0) {
            table.get(attacked.getX()).remove(attacked.getY());
        }

        return 0;
    }

    private boolean playerHasTanks(final int personIdx) {
        int playerFrontRow = (personIdx == 1) ? 2 : 1;
        for (GameCard card : table.get(playerFrontRow)) {
            if (card.getName().equals("Goliath") || card.getName().equals("Warden")) {
                return true;
            }
        }

        return false;
    }

    private boolean tableCardBelongsToPerson(final Coordinates card, final int personIdx) {
        // Coordinates must be positive or 0.
        if (card.getX() < 0 || card.getY() < 0)
            return false;

        // Verify if the card is on a correct row.
        if (personIdx == 1 && card.getX() != 3 && card.getX() != 2) {
                return false;
        }
        if (personIdx == 2 && card.getX() != 1 && card.getX() != 0) {
                return false;
        }

        // Verify if at the given position exists a card.
        if (card.getY() >= table.get(card.getX()).size()) {
            return false;
        }

        return true;
    }

    private boolean coordinatesAreValid(Coordinates card) {
        return ((tableCardBelongsToPerson(card, 1) || tableCardBelongsToPerson(card, 2)));
    }

    public GameCard getCard(Coordinates position) {
        if (coordinatesAreValid(position)) {
            return table.get(position.getX()).get(position.getY());
        }
        return null;
    }

    public int cardUsesAbility(Coordinates attacker, Coordinates attacked) {
        // Verify if the coordinates are valid.
        if (!coordinatesAreValid(attacker) || ! coordinatesAreValid(attacked)) {
            return 6;
        }

        // Take the attacker card.
        GameCard attackerCard = table.get(attacker.getX()).get(attacker.getY());

        // Verify if the attacker is a special card.
        if (!attackerCard.getName().equals("The Ripper") && !attackerCard.getName().equals("Miraj") &&
        !attackerCard.getName().equals("The Cursed One") && !attackerCard.getName().equals("Disciple")) {
            return 7;
        }

        // Verify if the attacker is not frozen.
        if (attackerCard.isFrozen()) {
            return 4;
        }

        // Verify if the card didn't attack already in this round.
        if (attackerCard.isUsedAttack()) {
            return 3;
        }

        // Take the attacked card.
        GameCard attackedCard = table.get(attacked.getX()).get(attacked.getY());

        // Verify if the attacker is Disciple.
        if (attackerCard.getName().equals("Disciple")) {
            // Verify if the attacked is a card of the current player.
            if (!tableCardBelongsToPerson(attacked, playerTurn)) {
                return 8;
            }
            attackerCard.useDiscipleAbility(attackedCard);
            attackerCard.setUsedAttack(true);
            return 0;
        }

        // Verify if the attacked is a card of opponent.
        int opponentIdx = playerTurn % 2 + 1;
        if (!tableCardBelongsToPerson(attacked, opponentIdx)) {
            return 2;
        }

        // If attacked card is not a tank, verify if there's a tank on front row of opponent.
        if (!attackedCard.getName().equals("Goliath") &&  !attackedCard.getName().equals("Warden")) {
            if (playerHasTanks(opponentIdx)) {
                return 5;
            }
        }

        attackerCard.setUsedAttack(true);

        // Verify if the attacker is The Ripper.
        if (attackerCard.getName().equals("The Ripper")) {
            attackerCard.useTheRipperAbility(attackedCard);
            return 0;
        }

        // Verify if the attacker is Miraj.
        if (attackerCard.getName().equals("Miraj")) {
            attackerCard.useMirajAbility(attackedCard);
            return 0;
        }

        // If we get here, the attacker is The Cursed One.
        attackerCard.useTheCursedOneAbility(attackedCard);
        if (attackedCard.getHealth() == 0) {
            table.get(attacked.getX()).remove(attacked.getY());
        }
        return 0;
    }

    public int useAttackOnHero(Coordinates attacker) {
        // Verify if the coordinates are valid.
        if (!coordinatesAreValid(attacker)) {
            return 6;
        }

        // Take the attacker card.
        GameCard attackerCard = table.get(attacker.getX()).get(attacker.getY());

        // Verify if the attacker is not frozen.
        if (attackerCard.isFrozen()) {
            return 4;
        }

        // Verify if the card didn't attack already in this round.
        if (attackerCard.isUsedAttack()) {
            return 3;
        }

        // Verify if there's a tank on front row of the opponent.
        int opponentIdx = playerTurn % 2 + 1;
        if (playerHasTanks(opponentIdx)) {
            return 5;
        }

        // Get the hero of the opponent.
        HeroCard opponentHero = players[opponentIdx].getHero();

        // The attack can take place.
        opponentHero.decreaseHealth(attackerCard.getAttackDamage());

        if (opponentHero.getHealth() <= 0) {
            return 99; //gg
        }

        return 0;
    }

    public int useHeroAbility(final int affectedRow) {
        // Get the herro card.
        HeroCard hero = players[playerTurn].getHero();

        // Verify if the current player has enough mana to use the hero ability.
        if (hero.getMana() > players[playerTurn].getMana()) {
            return 1;
        }

        // Verify if the card didn't attack already in this round.
        if (hero.isUsedAttack()) {
            return 2;
        }

        // Verify if the hero acts on his friends.
        if (hero.getName().equals("General Kocioraw") || hero.getName().equals("King Mudface")) {
            // Verify if the selected row is valid.
            if ((playerTurn == 1 && affectedRow < 2) || (playerTurn == 2 && affectedRow > 1)){
                return 3;
            }

            if (hero.getName().equals("General Kocioraw")) {
                hero.useGeneralKociorawAbility(table.get(affectedRow));
            } else {
                hero.useKingMudfaceAbility(table.get(affectedRow));
            }
        } else {
            // Verify if the selected row is valid.
            if ((playerTurn == 1 && affectedRow > 1) || (playerTurn == 2 && affectedRow < 2)){
                return 4;
            }

            if (hero.getName().equals("Lord Royce")) {
                hero.useLordRoyceAbility(table.get(affectedRow));
            } else {
                hero.useEmpressThorinaAbility(table.get(affectedRow));
            }

        }

        players[playerTurn].usesMana(hero.getMana());
        hero.setUsedAttack(true);

        return 0;
    }


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


}

