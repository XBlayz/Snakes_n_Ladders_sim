package snakes_n_ladders_sim.simulation;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.List;

import javafx.application.Platform;

import lombok.extern.slf4j.Slf4j;

import snakes_n_ladders_sim.gui.ControllerMatch;
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
    private boolean autoRun = false;
    private boolean waitNextTurn = true;
    private int delay = 1000;

    // Current player properties
    private Player currentPlayer;
    private int currentPlayerIndex;
    private int lastRoll;
    private boolean endTurn;
    private int nRounds = 0;

    // GUI Controller
    private ControllerMatch controller;

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
        } else {
            deck = null;
        }
    }

    @Override
    public void run() {
        log.info("!Simulation started!");

        boolean matchContinues = true;
        int i = -1;
        Platform.runLater(() -> controller.setRound(nRounds + 1)); // Set the round number
        log.info("Round " + (nRounds + 1));
        do {
            if(!autoRun) {
                waitNextTurn(); // Wait for the "NextTurn" or "AutoRunEnable" event
                if(interrupted()) return;
            }
            Platform.runLater(() -> controller.clearText()); // Clear the text on the GUI

            i++;
            if (i >= players.length) {
                i = 0;
                nRounds++;
                if(interrupted()) return;
                Platform.runLater(() -> controller.setRound(nRounds + 1)); // Update the round number
                log.info("Round " + (nRounds + 1));
            }

            currentPlayer = players[i];
            currentPlayerIndex = i;
            Platform.runLater(() -> controller.setCurrentTurn(currentPlayerIndex)); // Update the current player turn
            do {
                if(interrupted()) return;
                matchContinues = turn(); // Execute a player turn and get the result whether the player won the match
                setWaitNextTurn(true);

                waitTime(); // Wait for the specified delay
                if(interrupted()) return;
            } while(!endTurn);

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
            String cardName = card.toString();
            Platform.runLater(() -> controller.appendText("Carta pescata:\n" + cardName));
            log.info("Player " + (currentPlayerIndex+1) + " drew: " + cardName);
        }

        if (action == Action.TELEPORT) { // Snake or ladder cell action
            int oldPos = currentPlayer.getPosition();
            int newPos = currentPlayer.setPosition(data);
            Platform.runLater(() -> controller.movePlayer(oldPos, newPos, currentPlayerIndex)); // Move the player on the board
            return newPos != board.end;
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
                Platform.runLater(() -> controller.appendText("Usa carta:\n" + Card.DO_NOT_STOP.toString()));
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
            Platform.runLater(() -> controller.appendText("Bloccato"));
            log.info("Player " + (currentPlayerIndex + 1) + " is blocked");
            endTurn = true;
            return true; // If the player is blocked, skip the player's turn and return true to indicate that the match continues
        }

        return rollDice(); // Roll the dice and execute the next step
    }

    private boolean rollDice() {
        lastRoll = dice.roll(currentPlayer.getPosition()>=board.end-dice.numberOfSides);
        Platform.runLater(() -> controller.appendText("Dadi: " + lastRoll));
        log.info("Player " + (currentPlayerIndex + 1) + " rolled: " + lastRoll);

        maxDiceRule();
        return movePlayer(); // Move the player and execute the next step
    }

    private boolean movePlayer() {
        int oldPos = currentPlayer.getPosition();
        int position = currentPlayer.move(lastRoll); // Move the player and get the new position

        if(position > board.end) position = currentPlayer.setPosition(position-board.end); // If the player is outside the board, wrap it back to the beginning
        log.info("Player " + (currentPlayerIndex + 1) + " moved to: " + position);
        final int pos = position;
        Platform.runLater(() -> controller.movePlayer(oldPos, pos, currentPlayerIndex)); // Move the player on the board
        if(position == board.end) return false; // Check if the player won the match

        Cell cell = board.getCell(position);
        log.info("Player " + (currentPlayerIndex + 1) + " is on: " + cell.toString());
        return cell.action(); // Execute the action of the cell and return true if the match continues, false if the player has won and the match is over
    }

    private void maxDiceRule() {
        endTurn = lastRoll < dice.maxDice || !dice.isMaxDiceRuleOn; // Check if the player rolled the maximum value and, if the rule is active, the player has another turn
    }

    private synchronized void waitNextTurn() {
        while(waitNextTurn && !autoRun) {
            try {
                log.debug("Match waiting for next turn (WaitNextTurn: " + waitNextTurn + ", AutoRun: " + autoRun + ")");
                wait();
                log.debug("Match waked up (WaitNextTurn: " + waitNextTurn + ", AutoRun: " + autoRun + ")");
            } catch (InterruptedException ie) {
                log.info("Match interrupted");
                setAutoRun(true);
                this.interrupt();
            }
        }
    }

    public synchronized void notifyNextTurn() {
        setWaitNextTurn(false);
        notifyAll();
    }

    public synchronized void setAutoRun(boolean value) {
        autoRun = value;
        if(value) notifyNextTurn();
    }

    private synchronized void setWaitNextTurn(boolean value) {
        waitNextTurn = value;
    }

    private void waitTime() {
        try {
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException ie) {
            log.info("Match interrupted");
            this.interrupt();
        }
    }

    private void printResults(int i) {
        Platform.runLater(() -> controller.printText("Il giocatore " + (i + 1) + " ha vinto!"));
        Platform.runLater(() -> controller.printResults(i));
        log.info("Player " + (i + 1) + " has won!");
    }

    public int getNumberOfPlayers() {
        return players.length;
    }

    public Board getBoard() {
        return board;
    }

    public void setController(ControllerMatch controller) {
        this.controller = controller;
    }
}
