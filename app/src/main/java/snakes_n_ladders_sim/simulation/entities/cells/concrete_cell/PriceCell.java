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
        if (isSpring) {
            return mediator.sendMessage(Action.JUMP, -1);
        }
        return mediator.sendMessage(Action.REROLL, -1);
    }
}
