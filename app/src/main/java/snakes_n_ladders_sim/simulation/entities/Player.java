package snakes_n_ladders_sim.simulation.entities;

public class Player {
    private int position;
    private int blockedTurns;

    private int doNotStopCards;

    protected Player() {
        position = 0;
        blockedTurns = 0;
        doNotStopCards = 0;
    }

    public int move(int movement) {
        position += movement;
        return getPosition();
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void block(int turns) {
        if(doNotStopCards > 0) {
            doNotStopCards--;
            // TODO: Put card in the deck
            return;
        }

        blockedTurns += turns;
    }

    public boolean isBlocked() {
        boolean isBlock = blockedTurns > 0;
        if(isBlock) blockedTurns--;

        return isBlock;
    }

    public void addDoNotStop() {
        doNotStopCards++;
    }
}
