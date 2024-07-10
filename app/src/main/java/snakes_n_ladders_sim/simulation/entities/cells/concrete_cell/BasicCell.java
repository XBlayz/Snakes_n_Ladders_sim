package snakes_n_ladders_sim.simulation.entities.cells.concrete_cell;

import snakes_n_ladders_sim.simulation.entities.cells.*;
import snakes_n_ladders_sim.simulation.mediator.*;

public class BasicCell extends Cell {
    public BasicCell(Mediator mediator) {
        super(mediator);
    }

    @Override
    public boolean action() {
        System.out.println("(Basic cell)"); // TODO: replace with logger
        return mediator.sendMessage(this);
    }

    @Override
    public Action getAction() {
        return Action.NONE;
    }

    @Override
    public int getData() {
        return -1;
    }
}
