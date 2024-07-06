package snakes_n_ladders_sim.simulation.entities.cells.concrete_cell;

import snakes_n_ladders_sim.simulation.entities.cells.*;
import snakes_n_ladders_sim.simulation.mediator.*;

public class BasicCell extends Cell {
    public BasicCell(Mediator mediator) {
        super(mediator);
    }

    @Override
    public boolean action() {
        return mediator.sendMessage(Action.NONE, -1);
    }
}
