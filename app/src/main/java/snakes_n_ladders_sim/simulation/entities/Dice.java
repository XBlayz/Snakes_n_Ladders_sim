package snakes_n_ladders_sim.simulation.entities;

import java.util.Random;

public class Dice {
    // Dice properties
    public final int numberOfSides;
    public final int numberOfDice;
    private final Random rng;

    // Dice constants
    public final int maxDice;

    // Dice rules
    public final boolean isSingleDiceRuleOn;
    public final boolean isMaxDiceRuleOn;

    public Dice(int numberOfSides, int numberOfDice, boolean isSingleDiceRuleOn, boolean isMaxDiceRuleOn) {
        this.numberOfSides = numberOfSides;
        this.numberOfDice = numberOfDice;
        rng = new Random(); // TODO: set seed

        maxDice = numberOfSides * numberOfDice;

        this.isSingleDiceRuleOn = isSingleDiceRuleOn;
        this.isMaxDiceRuleOn = isMaxDiceRuleOn;
    }

    public int roll(boolean isBoardLastNCells) {
        int roll = 0;
        if(isSingleDiceRuleOn && isBoardLastNCells) {
            roll = rng.nextInt(numberOfSides) + 1; // Single dice rule
        }else{
            for (int i = 0; i < numberOfDice; i++) {
                roll += rng.nextInt(numberOfSides) + 1; // Normal dice roll
            }
        }

        return roll;
    }
}
