package snakes_n_ladders_sim.simulation.entities.cells.concrete_cell;

import snakes_n_ladders_sim.simulation.entities.cells.*;
import snakes_n_ladders_sim.simulation.mediator.*;

public class SnakeOrLadderCell extends Cell {
    private int position;
    private boolean isLadder;

    public SnakeOrLadderCell(Mediator mediator, int position, boolean isLadder) {
        super(mediator);

        this.position = position;
        this.isLadder = isLadder;
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

    public boolean isLadder() {
        return isLadder;
    }

    @Override
    public String toString() {
        if(isLadder) {
            return "[Ladder (" + position + ")]";
        }
        return "[Snake (" + position + ")]";
    }
}
