package snakes_n_ladders_sim.simulation.entities;

import snakes_n_ladders_sim.simulation.entities.player_message.PlayerMessageType;
import snakes_n_ladders_sim.simulation.mediator.*;

public class Player extends Colleague {
    // Player properties
    private int position;
    private int blockedTurns;

    // Player cards
    private int doNotStopCards;

    // Player message (mediator)
    private PlayerMessageType message;

    public Player(Mediator mediator) {
        super(mediator);

        position = 0;
        blockedTurns = 0;
        doNotStopCards = 0;
    }

    public int move(int movement) {
        position += movement;
        return getPosition();
    }

    public int setPosition(int position) {
        this.position = position;
        return getPosition();
    }

    public int getPosition() {
        return position;
    }

    public void block(int turns) {
        if(doNotStopCards > 0) { // Check if the player has do not stop cards
            doNotStopCards--; // Remove a do not stop card

            setMessage(PlayerMessageType.DISCARD_DO_NOT_STOP_CARD); // Set the message to sand to the mediator
            mediator.sendMessage(this); // Send the message to the mediator for discarding a do not stop card

            return;
        }

        blockedTurns += turns; // Add the turns to the blocked turns
    }

    public boolean isBlocked() {
        boolean isBlock = blockedTurns > 0; // Check if the player is blocked
        if(isBlock) blockedTurns--; // Decrement the blocked turns

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
