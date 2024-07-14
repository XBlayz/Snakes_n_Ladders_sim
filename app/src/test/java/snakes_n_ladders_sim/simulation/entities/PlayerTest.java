package snakes_n_ladders_sim.simulation.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void testMove() {
        Player player = new Player(null);
        player.move(10);
        assertEquals(10, player.getPosition());
    }

    @Test
    void isBlocked() {
        Player player = new Player(null);
        player.block(3);
        assertTrue(player.isBlocked());
        assertTrue(player.isBlocked());
        assertTrue(player.isBlocked());
        assertFalse(player.isBlocked());

        player.addDoNotStop();
        try {
            player.block(1);
        }catch(NullPointerException e) {
            System.out.println("Null pointer exception is expected");
        }
        assertFalse(player.isBlocked());

        player.block(1);
        assertTrue(player.isBlocked());
        assertFalse(player.isBlocked());
    }
}
