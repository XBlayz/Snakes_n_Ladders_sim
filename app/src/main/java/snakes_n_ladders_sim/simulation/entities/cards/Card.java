package snakes_n_ladders_sim.simulation.entities.cards;

public enum Card {
    BENCH(true, "|Bench|"),
    INN(true, "|Inn|"),
    DICE(true, "|Dice|"),
    SPRING(true, "|Spring|"),
    DO_NOT_STOP(false, "|Do Not Stop|");

    public final boolean isInstant;
    private String name;

    Card(boolean isInstant, String name) {
        this.isInstant = isInstant;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
