package snakes_n_ladders_sim.simulation.entities;

import snakes_n_ladders_sim.simulation.entities.cells.*;
import snakes_n_ladders_sim.simulation.mediator.Colleague;

public class Board {
    private int rows;
    private int columns;
    private Colleague[][] cells;

    public final int end;

    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.cells = new Cell[rows][columns];

        this.end = rows * columns - 1;

        // TODO
    }

    public Colleague getCellAsColleague(int position) {
        int c = position % columns;
        int r = position / rows;
        return cells[r][c];
    }
}
