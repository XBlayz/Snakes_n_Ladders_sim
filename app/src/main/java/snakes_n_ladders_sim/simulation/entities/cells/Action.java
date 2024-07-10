package snakes_n_ladders_sim.simulation.entities.cells;

public enum Action {
    NONE("NONE"),
    TELEPORT("TP"),
    REROLL("REROLL"),
    JUMP("JUMP"),
    BLOCK("BLOCK"),
    DRAW_CARD("DRAW_CARD");

    private final String name;

    Action(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
