import java.util.ArrayList;

public class Hand {
    
    public int size; //Amount of cards this hand can have
    public ArrayList<Card> cards = new ArrayList<>(); //Arraylist for storing the cards

    //Constructor for a hand that takes a size value
    public Hand(int size) {
        this.size = size;
    }

    //Add a card to this hand if there is space.
    public void addCard(Card card) {
        if (this.cards.size() < this.size) {
            this.cards.add(card);
        }
    }

    //Print all cards in this hand
    public void printCards() {
        cards.forEach(card -> {
            String cardName = card.toString();
            System.out.println(cardName.substring(0, 1).toUpperCase() + cardName.substring(1));
        });
        System.out.println();
    }

    //Return whether this hand has no cards
    public boolean isEmpty() {
        return this.cards.isEmpty();
    }

    //Return whether this hand has any cards with the given value
    public boolean hasCardWithValue(Card.Value value) {
        for (Card card : cards) {
            if (card.value.equals(value)) {
                return true;
            }
        }
        return false;
    }

    //Return the number of cards in this hand with the given value
    public int countMatchingCards(Card.Value cardValue) {
        int matchingCardCount = 0;
        for (Card card : this.cards) {
            if (card.value.equals(cardValue)) {
                matchingCardCount++;
            }
        }
        return matchingCardCount;
    }

    //Give all cards in this hand with the given value to another hand
    public void giveCardsWithValueTo(Card.Value cardValue, Hand otherHand) {
        for (Card card : this.cards) {
            if (card.value.equals(cardValue)) {
                otherHand.cards.add(card);
            }
        }
        this.removeCardsWithValue(cardValue);
    }

    //Remove all cards with the given value from this hand
    public void removeCardsWithValue(Card.Value cardValue) {
        ArrayList<Card> cardsToRemove = new ArrayList<>();
        for (Card card : this.cards) {
            if (card.value.equals(cardValue)) {
                cardsToRemove.add(card);
            }
        }

        for (Card card : cardsToRemove) {
            this.cards.remove(card);
        }
    }
}
