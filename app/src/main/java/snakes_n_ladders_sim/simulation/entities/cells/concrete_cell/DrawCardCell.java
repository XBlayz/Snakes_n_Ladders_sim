package snakes_n_ladders_sim.simulation.entities.cells.concrete_cell;

import snakes_n_ladders_sim.simulation.entities.cells.*;
import snakes_n_ladders_sim.simulation.mediator.*;

public class DrawCardCell extends Cell {
    public DrawCardCell(Mediator mediator) {
        super(mediator);
    }

    @Override
    public boolean action() {
        return mediator.sendMessage(Action.DRAW_CARD, -1);
    }
}
