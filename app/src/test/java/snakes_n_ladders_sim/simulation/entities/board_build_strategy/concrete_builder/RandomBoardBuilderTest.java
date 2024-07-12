package snakes_n_ladders_sim.simulation.entities.board_build_strategy.concrete_builder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import snakes_n_ladders_sim.simulation.entities.cells.Cell;
import snakes_n_ladders_sim.simulation.entities.cells.concrete_cell.BasicCell;

class RandomBoardBuilderTest {

    @Test
    void testFilling() {
        for (float j = 0; j < 0.5; j+=0.1f) { // Testing different filling rate (0.0f, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f)
            for (int i = 0; i < 10; i++) { // Create 10 random boards for each different filling rate
                int k = 0;
                try {
                    RandomBoardBuilder builder = new RandomBoardBuilder(true, true, true, j);

                    Cell[][] board = builder.buildBoard(100, 100, null);
                    for(Cell[] rows : board) {
                        for(Cell c : rows) {
                            if(c instanceof BasicCell) k++; // Count the number of basic cells
                            if(c == null) fail("Cell not initialized"); // If a cell is null, fail the test
                        }
                    }
                    assertTrue((1-j)*100 <= k); // Check if the percentage of special cells is less than equal to the filling rate
                }catch(Exception e) {
                    e.printStackTrace();
                    fail("Test failed with fillingRatio = " + j); // Fail the test if an exception is thrown
                }
            }
        }
    }

    @Test
    void testDimensions() {
        // Test different board dimensions
        for (int i = 3; i < 10; i++) {
            for (int j = 3; j < 10; j++) {
                try {
                    RandomBoardBuilder builder = new RandomBoardBuilder(true, true, true, j);
                    assertTrue(builder.buildBoard(i, j, null).length == i && builder.buildBoard(i, j, null)[0].length == j); // Check if the dimensions are correct
                }catch(Exception e) {
                    e.printStackTrace();
                    fail("Test failed with dimensions: r" + i + ", c" + j); // Fail the test if an exception is thrown
                }
            }
        }
    }
}
