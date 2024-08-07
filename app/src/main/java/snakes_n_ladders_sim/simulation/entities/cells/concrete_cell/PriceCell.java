package snakes_n_ladders_sim.simulation.entities.cells.concrete_cell;

import snakes_n_ladders_sim.simulation.entities.cells.*;
import snakes_n_ladders_sim.simulation.mediator.*;

public class PriceCell extends Cell {
    private boolean isSpring;

    public PriceCell(Mediator mediator, boolean isSpring) {
        super(mediator);

        this.isSpring = isSpring;
    }

    @Override
    public boolean action() {
        return mediator.sendMessage(this);
    }

    @Override
    public Action getAction() {
        if (isSpring) {
            return Action.JUMP;
        }
        return Action.REROLL;
    }

    @Override
    public int getData() {
        return -1;
    }

    @Override
    public String toString() {
        if(isSpring) {
            return "[Spring]";
        }
        return "[Reroll]";
    }
}
