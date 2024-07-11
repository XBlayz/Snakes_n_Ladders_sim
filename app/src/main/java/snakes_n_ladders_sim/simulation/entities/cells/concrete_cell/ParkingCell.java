package snakes_n_ladders_sim.simulation.entities.cells.concrete_cell;

import snakes_n_ladders_sim.simulation.entities.cells.*;
import snakes_n_ladders_sim.simulation.mediator.*;

public class ParkingCell extends Cell {
    private int nTurns;

    public ParkingCell(Mediator mediator, int nTurns) {
        super(mediator);

        this.nTurns = nTurns;
    }

    @Override
    public boolean action() {
        return mediator.sendMessage(this);
    }

    @Override
    public Action getAction() {
        return Action.BLOCK;
    }

    @Override
    public int getData() {
        return nTurns;
    }

    @Override
    public String toString() {
        if(nTurns == 1) {
            return "[Bench]";
        }else if(nTurns == 3) {
            return "[Inn]";
        }
        return "[Block (" + nTurns + ")]";
    }
}
