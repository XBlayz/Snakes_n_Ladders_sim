package snakes_n_ladders_sim.simulation.entities;

import snakes_n_ladders_sim.simulation.entities.board_build_strategy.BoardBuildStrategy;
import snakes_n_ladders_sim.simulation.entities.cells.*;
import snakes_n_ladders_sim.simulation.mediator.Mediator;

public class Board {
    // Board properties
    private final int rows;
    private final int columns;
    private Cell[][] cells;

    // Board constants
    public final int end;

    public Board(int rows, int columns, BoardBuildStrategy boardBuildStrategy, Mediator mediator) {
        this.rows = rows;
        this.columns = columns;
        this.cells = boardBuildStrategy.buildBoard(rows, columns, mediator);

        this.end = rows * columns;
    }

    public Cell getCell(int position) {
        int c = (position-1) % columns; // Calculate the column index
        int r = (position-1) / rows; // Calculate the row index
        return cells[r][c];
    }
}
