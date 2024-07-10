package snakes_n_ladders_sim.simulation.entities.cells.concrete_cell;

import snakes_n_ladders_sim.simulation.entities.cells.*;
import snakes_n_ladders_sim.simulation.mediator.*;

public class DrawCardCell extends Cell {
    public DrawCardCell(Mediator mediator) {
        super(mediator);
    }

    @Override
    public boolean action() {
        System.out.println("(Draw card cell)"); // TODO: replace with logger
        return mediator.sendMessage(this);
    }

    @Override
    public Action getAction() {
        return Action.DRAW_CARD;
    }

    @Override
    public int getData() {
        return -1;
    }
}
