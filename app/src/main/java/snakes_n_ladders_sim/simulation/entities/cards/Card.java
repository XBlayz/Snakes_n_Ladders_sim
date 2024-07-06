package snakes_n_ladders_sim.simulation.entities.cards;

public enum Card {
    BENCH(true),
    INN(true),
    DICE(true),
    SPRING(true),
    DO_NOT_STOP(false);

    public final boolean isInstant;

    Card(boolean isInstant) {
        this.isInstant = isInstant;
    }
}
