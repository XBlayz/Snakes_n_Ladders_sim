package snakes_n_ladders_sim.simulation.mediator;

import snakes_n_ladders_sim.simulation.entities.cells.Action;

public interface Mediator {
    boolean sendMessage(Action action, int data);
}
