package snakes_n_ladders_sim.simulation.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DiceTest {

    @Test
    void testRoll() {
        Dice dice = new Dice(6, 2, true, true);
        assertEquals(12, dice.maxDice);
        for(int i = 0; i < 100; i++) {
            int roll = dice.roll(false);
            assertTrue(roll >= 1 && roll <= 12);

            roll = dice.roll(true);
            assertTrue(roll >= 1 && roll <= 6);
        }
    }
}
