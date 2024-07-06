package snakes_n_ladders_sim.simulation.entities.cells.concrete_cell;

import snakes_n_ladders_sim.simulation.entities.cells.*;
import snakes_n_ladders_sim.simulation.mediator.*;

public class SnakeOrLadderCell extends Cell {
    private int position;

    public SnakeOrLadderCell(Mediator mediator, int position) {
        super(mediator);

        this.position = position;
    }

    @Override
    public boolean action() {
        return mediator.sendMessage(this);
    }

    @Override
    public Action getAction() {
        return Action.TELEPORT;
    }

    @Override
    public int getData() {
        return position;
    }
}
