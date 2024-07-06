package snakes_n_ladders_sim.simulation.entities.cells;

import snakes_n_ladders_sim.simulation.mediator.*;

public abstract class Cell extends Colleague {
    protected Cell(Mediator mediator) {
        super(mediator);
    }
}
