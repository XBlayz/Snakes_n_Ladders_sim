package snakes_n_ladders_sim.simulation.entities;

import snakes_n_ladders_sim.simulation.entities.cells.*;

public class Board {
    private int rows;
    private int columns;
    private Cell[][] cells;

    public final int end;

    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.cells = new Cell[rows][columns];

        this.end = rows * columns - 1;

        // TODO
    }

    public Cell getCell(int position) {
        int c = position % columns;
        int r = position / rows;
        return cells[r][c];
    }
}
