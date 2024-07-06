package snakes_n_ladders_sim.simulation.entities;

import snakes_n_ladders_sim.simulation.entities.player_message.PlayerMessageType;
import snakes_n_ladders_sim.simulation.mediator.*;

public class Player extends Colleague {
    private int position;
    private int blockedTurns;

    private int doNotStopCards;

    private PlayerMessageType message;

    protected Player(Mediator mediator) {
        super(mediator);

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

            setMessage(PlayerMessageType.DISCARD_DO_NOT_STOP_CARD);
            mediator.sendMessage(this);

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

    private void setMessage(PlayerMessageType message) {
        this.message = message;
    }

    public PlayerMessageType getMessage() {
        return message;
    }
}
