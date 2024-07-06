package snakes_n_ladders_sim.simulation;

import java.util.concurrent.TimeUnit;

import snakes_n_ladders_sim.simulation.entities.*;
import snakes_n_ladders_sim.simulation.entities.cards.Card;
import snakes_n_ladders_sim.simulation.entities.cells.Action;
import snakes_n_ladders_sim.simulation.mediator.Mediator;

public class Match extends Thread implements Mediator {
    private Player[] players;
    private Board board;
    private Dice dice;
    private Deck deck;

    private boolean autoRun = true;
    private int delay = 500;

    private Player currentPlayer;
    private int lastRoll;
    private boolean endTurn;

    public Match() {
        // TODO
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
            currentPlayer = players[i];
            do {
                matchContinues = turn(); // Execute a player turn and get the result whether the player won the match
            } while(!endTurn);

            waitTime(); // Wait for the specified delay
        } while(matchContinues); // Check if the player won the match

        printResults(i); // Print the match results
    }

    @Override
    public boolean sendMessage(Action action, int data) {
        Card card = null;
        if (action == Action.DRAW_CARD) {
            card = deck.drawCard();
        }

        if (action == Action.TELEPORT) {
            currentPlayer.setPosition(data);
            return currentPlayer.getPosition() != board.end;
        }else if (action == Action.REROLL || card == Card.DICE) {
            return rollDice();
        }else if(action == Action.JUMP || card == Card.SPRING) {
            return movePlayer();
        }else if(action == Action.BLOCK) {
            currentPlayer.block(data);
            return currentPlayer.getPosition() != board.end;
        }else if(card == Card.BENCH) {
            currentPlayer.block(1);
            return currentPlayer.getPosition() != board.end;
        }else if(card == Card.INN){
            currentPlayer.block(3);
            return currentPlayer.getPosition() != board.end;
        }else if(card == Card.DO_NOT_STOP) {
            currentPlayer.addDoNotStop();
        }

        return true;
    }

    /**
     * Execute a game turn
     *
     * @param player The player who is taking the turn
     * @return True if the match continues, false if the player has won and the match is over
     */
    private boolean turn() {
        if(currentPlayer.isBlocked()) return true; // If the player is blocked, skip the player's turn and return true to indicate that the match continues

        return rollDice(); // Roll the dice and execute the next step
    }

    private boolean rollDice() {
        lastRoll = dice.roll(currentPlayer.getPosition());
        maxDiceRule();
        return movePlayer(); // Move the player and execute the next step
    }

    private boolean movePlayer() {
        int position = currentPlayer.move(lastRoll); // Move the player and get the new position

        if(position == board.end) return false; // Check if the player won the match
        return board.getCellAsColleague(position).action(); // Execute the action of the cell and return true if the match continues, false if the player has won and the match is over
    }

    private void maxDiceRule() {
        endTurn = lastRoll < dice.maxValue() || !dice.isMaxDiceRule(); // Check if the player rolled the maximum value and, if the rule is active, the player has another turn
    }

    private void waitNextTurn() {
        // TODO: wait for the click of the "NextTurn" button or "AutoRun"
    }

    private void waitTime() {
        try {
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            System.err.println("Error: " + ie); // TODO: replace with logger
        }
    }

    private void printResults(int i) {
        // TODO: show the match results
        System.out.println("Player " + (i + 1) + " has won!"); // TODO: replace with logger
    }
}
