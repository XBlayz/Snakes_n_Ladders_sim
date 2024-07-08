package snakes_n_ladders_sim.simulation.entities;

import java.util.Queue;
import java.util.LinkedList;

import snakes_n_ladders_sim.simulation.entities.cards.Card;

public class Deck {
    // Cards collection
    private Queue<Card> cards = new LinkedList<>();

    public Deck() {
        //TODO
    }

    public Card drawCard() {
        Card drawnCard = cards.poll();
        if (drawnCard.isInstant) { // If card is instant, discard it immediately
            cards.add(drawnCard);
        }
        return drawnCard;
    }

    public void discardCard(Card card) {
        cards.add(card);
    }

}
