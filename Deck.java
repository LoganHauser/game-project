import java.util.ArrayList;

public class Deck {

    private ArrayList<Card> cards = new ArrayList<>(); //Empty list for storing cards

    //Constructor for a deck object
    public Deck() {
        this.initialize(); //Initialize the deck
    }

    //Resets the deck with one of each card
    private void initialize() {

        this.cards.clear(); //Clear the deck in case it already has cards

        //For each combination of suit and value, add a card with those values to the deck
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Value value : Card.Value.values()) {
                this.cards.add(new Card(value, suit));
            }
        }
    }

    //Randomize the deck
    public void shuffle() {
        Card cardToSwap;
        int indexToSwapTo;                        //For each card in the deck, replace it with a random card,
        for (int i = 0; i < cards.size(); i++) {  //then replace the random card with the original card.
            cardToSwap = cards.get(i); //Store the current card
            indexToSwapTo = this.getRandomCardIndex(); //Store the index that this card will be swapped to

            this.cards.set(i, cards.get(indexToSwapTo)); //Replace the current card with the card at the random index
            this.cards.set(indexToSwapTo, cardToSwap); //Replace the card at the random index with the current card
        }
    }

    //Get the index of a random card. Used in the shuffle method.
    private int getRandomCardIndex() {
        return (int) Math.floor(Math.random() * cards.size());
    }

    //Fill a hand with the max amount of cards it can hold
    public void addCardsToHand(Hand hand) {
        for (int i = 0; i < hand.size; i++) {
            this.addCardToHand(hand);
        }
    }

    //Add the first card in the deck to a hand
    public void addCardToHand(Hand hand) {
        hand.addCard(cards.get(0)); //Add first card to the hand
        this.cards.remove(cards.get(0)); //Remove the card from the deck
    }

    //Get the amount of cards left in this deck
    public int getNumberOfCards() {
        return this.cards.size();
    }
}
