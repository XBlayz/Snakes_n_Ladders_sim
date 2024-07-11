package snakes_n_ladders_sim.simulation.entities.board_build_strategy.concrete_builder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import snakes_n_ladders_sim.simulation.entities.cells.Cell;
import snakes_n_ladders_sim.simulation.entities.cells.concrete_cell.BasicCell;

class RandomBoardBuilderTest {

    @Test
    void test() {
        for (float j = 0; j < 0.5; j+=0.1f) { // Testing different filling rate (0.0f, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f)
            for (int i = 0; i < 10; i++) { // Create 10 random boards for each different filling rate
                int k = 0;
                try {
                    RandomBoardBuilder builder = new RandomBoardBuilder(true, true, true, j);

                    for(Cell[] rows : builder.buildBoard(10, 10, null)) {
                        for(Cell c : rows) {
                            if(c instanceof BasicCell) k++; // Count the number of basic cells
                            if(c == null) fail("Cell not initialized"); // If a cell is null, fail the test
                        }
                    }
                    assertTrue((1-j)*100 <= k); // Check if the percentage of special cells is less than equal to the filling rate
                }catch(Exception e) {
                    fail("Test failed with fillingRatio = " + j); // Fail the test if an exception is thrown
                }
            }
        }
    }
}
