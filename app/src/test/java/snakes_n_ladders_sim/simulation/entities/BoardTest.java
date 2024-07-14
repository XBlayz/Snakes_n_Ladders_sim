package snakes_n_ladders_sim.simulation.entities;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import snakes_n_ladders_sim.simulation.entities.board_build_strategy.concrete_builder.RandomBoardBuilder;
import snakes_n_ladders_sim.simulation.entities.cells.Cell;

class BoardTest {

    @ParameterizedTest
    @MethodSource("getDimensions")
    void testGetCell(int[] dimensions) {
        Board board = new Board(dimensions[0], dimensions[1], new RandomBoardBuilder(true, true, true), null);
        Cell[][] cells = board.getCells();

        for (Cell[] row : cells) {
            for (Cell cell : row) {
                System.out.print(cell.getAction() + " ");
            }
            System.out.println();
        }
        System.out.println();

        for (int i = 0; i < dimensions[0]; i++) {
            for (int j = 0; j < dimensions[1]; j++) {
                assertEquals(cells[i][j].getAction(), board.getCell((i*dimensions[1]+j+1)).getAction());
            }
        }
    }

    static Stream<int[]> getDimensions() {
        return Stream.of(new int[] {3, 3}, new int[] {3, 4}, new int[] {5, 5}, new int[] {7, 3}, new int[] {10, 10}, new int[] {15, 5}, new int[] {15, 15}, new int[] {5, 15});
    }
}
