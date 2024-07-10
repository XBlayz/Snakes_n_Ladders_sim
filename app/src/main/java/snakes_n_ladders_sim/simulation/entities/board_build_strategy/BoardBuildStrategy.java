package snakes_n_ladders_sim.simulation.entities.board_build_strategy;

import snakes_n_ladders_sim.simulation.entities.cells.Cell;
import snakes_n_ladders_sim.simulation.mediator.Mediator;

public interface BoardBuildStrategy {
    Cell[][] buildBoard(int rows, int columns, Mediator mediator);
}
