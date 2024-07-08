package snakes_n_ladders_sim.simulation.entities;

import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import snakes_n_ladders_sim.simulation.entities.cards.Card;
import snakes_n_ladders_sim.utility.*;

public class Deck {
    // Cards collection
    private Queue<Card> cards = new LinkedList<>();

    public Deck(List<Tuple<Card, Integer>> cardList) {
        Random rng = new Random(System.currentTimeMillis()); // Initialize random number generator

        while (!cardList.isEmpty()) {
            Tuple<Card, Integer> cardAndN = cardList.get(rng.nextInt(cardList.size())); // Pick a random card

            cards.add(cardAndN.getX()); // Add the card to the deck

            if(cardAndN.getY() > 1){ // If the number of the card is greater than 1:
                cardAndN.setY(cardAndN.getY() - 1); // Reduce the number of the card by 1
            }else{
                cardList.remove(cardAndN); // Else, remove the card from the list
            }

            // Repeat until the list is empty and all the cards have been added to the deck shuffled
        }
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
