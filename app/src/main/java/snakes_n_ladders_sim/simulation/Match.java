package snakes_n_ladders_sim.simulation;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import snakes_n_ladders_sim.simulation.entities.*;
import snakes_n_ladders_sim.simulation.entities.board_build_strategy.BoardBuildStrategy;
import snakes_n_ladders_sim.simulation.entities.cards.*;
import snakes_n_ladders_sim.simulation.entities.cells.*;
import snakes_n_ladders_sim.simulation.entities.player_message.PlayerMessageType;
import snakes_n_ladders_sim.simulation.mediator.*;
import snakes_n_ladders_sim.utility.Tuple;

@Slf4j
public class Match extends Thread implements Mediator {
    // Match entities
    private final Player[] players;
    private final Board board;
    private final Dice dice;
    private final Deck deck;

    // Turn advancement settings
    private boolean autoRun = true;
    private int delay = 500;

    // Current player properties
    private Player currentPlayer;
    private int currentPlayerIndex;
    private int lastRoll;
    private boolean endTurn;

    public Match(int nPlayer, int rows, int columns, BoardBuildStrategy boardBuildStrategy, boolean isCardsOn, boolean isExtraCardsOn, int numberOfSides, int numberOfDice, boolean isSingleDiceRuleOn, boolean isMaxDiceRuleOn, int numberOfEachCard) {
        // Create all players
        players = new Player[nPlayer];
        for (int i = 0; i < nPlayer; i++) {
            players[i] = new Player(this);
        }

        // Create the board
        board = new Board(rows, columns, boardBuildStrategy, this);

        // Create the dice
        dice = new Dice(numberOfSides, numberOfDice, isSingleDiceRuleOn, isMaxDiceRuleOn);

        // Create the deck
        List<Tuple<Card, Integer>> cardList = new ArrayList<>();
        if(isCardsOn) {
            cardList.add(new Tuple<>(Card.BENCH, Integer.valueOf(numberOfEachCard)));
            cardList.add(new Tuple<>(Card.INN, Integer.valueOf(numberOfEachCard)));
            cardList.add(new Tuple<>(Card.DICE, Integer.valueOf(numberOfEachCard)));
            cardList.add(new Tuple<>(Card.SPRING, Integer.valueOf(numberOfEachCard)));
        }
        if (isExtraCardsOn) {
            cardList.add(new Tuple<>(Card.DO_NOT_STOP, Integer.valueOf(numberOfEachCard)));
        }
        if (!cardList.isEmpty()) {
            deck = new Deck(cardList);
            log.info(deck.toString());
        } else {
            deck = null;
        }
    }

    @Override
    public void run() {
        boolean matchContinues = true;
        int i = -1;
        do {
            if(!autoRun) {
                waitNextTurn(); // Wait for the "NextTurn" or "AutoRunEnable" event
            }

            i++;
            if (i >= players.length) {
                i = 0;
            }

            currentPlayer = players[i];
            currentPlayerIndex = i;
            do {
                matchContinues = turn(); // Execute a player turn and get the result whether the player won the match
            } while(!endTurn);

            waitTime(); // Wait for the specified delay
        } while(matchContinues); // Check if the player won the match

        printResults(i); // Print the match results
    }

    @Override
    public boolean sendMessage(Colleague colleague) {
        if(colleague instanceof Cell) {
            Cell cell = (Cell)colleague;
            return reactToCell(cell.getAction(), cell.getData()); // Execute the action of the cell
        }else if(colleague instanceof Player) {
            Player player = (Player)colleague;
            return reactToPlayer(player.getMessage()); // Execute the action of the player
        }

        throw new UnsupportedOperationException("Unsupported message from type: " + colleague.getClass().getName());
    }

    private boolean reactToCell(Action action, int data) {
        Card card = null;
        if (action == Action.DRAW_CARD && deck != null) {
            card = deck.drawCard(); // Draw a card
            log.info("Player " + (currentPlayerIndex+1) + " drew: " + card.toString());
        }

        if (action == Action.TELEPORT) { // Snake or ladder cell action
            currentPlayer.setPosition(data);
            return currentPlayer.getPosition() != board.end;
        }else if (action == Action.REROLL || card == Card.DICE) { // Price cell action (dice) or dice card
            return rollDice();
        }else if(action == Action.JUMP || card == Card.SPRING) { // Price cell action (spring) or spring card
            return movePlayer();
        }else if(action == Action.BLOCK) { // Parking cell action
            currentPlayer.block(data);
            return currentPlayer.getPosition() != board.end;
        }else if(card == Card.BENCH) { // Bench card draw
            currentPlayer.block(1);
            return currentPlayer.getPosition() != board.end;
        }else if(card == Card.INN){ // Inn card draw
            currentPlayer.block(3);
            return currentPlayer.getPosition() != board.end;
        }else if(card == Card.DO_NOT_STOP) { // Do not stop card draw
            currentPlayer.addDoNotStop();
        }

        return true;
    }

    private boolean reactToPlayer(PlayerMessageType message) {
        switch (message) {
            case DISCARD_DO_NOT_STOP_CARD: // Do not stop card discard in the bottom of the deck
                deck.discardCard(Card.DO_NOT_STOP);
                log.info("Player " + (currentPlayerIndex + 1) + " discarded: " + Card.DO_NOT_STOP.toString());
                return true;

            default:
                throw new UnsupportedOperationException("Unimplemented message: " + message);
        }
    }

    /**
     * Execute a game turn
     *
     * @param player The player who is taking the turn
     * @return True if the match continues, false if the player has won and the match is over
     */
    private boolean turn() {
        if(currentPlayer.isBlocked()) {
            log.info("Player " + (currentPlayerIndex + 1) + " is blocked");
            endTurn = true;
            return true; // If the player is blocked, skip the player's turn and return true to indicate that the match continues
        }

        return rollDice(); // Roll the dice and execute the next step
    }

    private boolean rollDice() {
        lastRoll = dice.roll(currentPlayer.getPosition()>=board.end-dice.numberOfSides);
        log.info("Player " + (currentPlayerIndex + 1) + " rolled: " + lastRoll);

        maxDiceRule();
        return movePlayer(); // Move the player and execute the next step
    }

    private boolean movePlayer() {
        int position = currentPlayer.move(lastRoll); // Move the player and get the new position
        log.info("Player " + (currentPlayerIndex + 1) + " moved to: " + position);

        if(position == board.end) return false; // Check if the player won the match
        if(position > board.end) position = currentPlayer.setPosition(position-board.end); // If the player is outside the board, wrap it back to the beginning

        Cell cell = board.getCell(position);
        log.info("Player " + (currentPlayerIndex + 1) + " is on: " + cell.toString());
        return cell.action(); // Execute the action of the cell and return true if the match continues, false if the player has won and the match is over
    }

    private void maxDiceRule() {
        endTurn = lastRoll < dice.maxDice || !dice.isMaxDiceRuleOn; // Check if the player rolled the maximum value and, if the rule is active, the player has another turn
    }

    private void waitNextTurn() {
        // TODO: wait for the click of the "NextTurn" button or "AutoRun"
    }

    private void waitTime() {
        try {
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            log.error("Error: " + ie);
        }
    }

    private void printResults(int i) {
        // TODO: show the match results
        log.info("Player " + (i + 1) + " has won!");
    }
}
