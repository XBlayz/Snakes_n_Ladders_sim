package snakes_n_ladders_sim.simulation.mediator;

public abstract class Component {
    protected Mediator mediator;

    protected Component(Mediator mediator) {
        this.mediator = mediator;
    }
}
