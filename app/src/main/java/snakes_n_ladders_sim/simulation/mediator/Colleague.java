package snakes_n_ladders_sim.simulation.mediator;

public abstract class Colleague {
    protected Mediator mediator;

    protected Colleague(Mediator mediator) {
        this.mediator = mediator;
    }
}
