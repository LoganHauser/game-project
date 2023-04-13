import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    //Scanner for reading user input
    private static Scanner inputScanner = new Scanner(System.in);
    //ArrayList for storing the two players
    private static ArrayList<Player> players = new ArrayList<>();
    //Deck of cards for the game
    private static Deck deck = new Deck();
    
    public static void main(String[] args) {
        //Shuffle the deck
        deck.shuffle();

        System.out.println("\nGo Fish card game\n");

        //Ask for the name of player 1 to be inputted
        System.out.println("Enter name for player 1:");
        players.add(new Player(inputScanner.nextLine()));
        System.out.println();
        //Ask for the name of player 2 to be inputted
        System.out.println("Enter name for player 2:");
        players.add(new Player(inputScanner.nextLine()));
        System.out.println();

        //Loop 7 times
        for (int i = 0; i < 7; i++) {
            //Give each player a card from the deck and check if they already have a match of 4
            players.forEach(player -> {
                deck.addCardToHand(player.hand);
                Card.Value matchedValue = player.checkForMatch();
                //If the player made a match, print which card value they matched
                if (matchedValue != null) {
                    System.out.println(player.name + " made a match of 4 " + capitalizeString(pluralizeValue(matchedValue)) + ".");
                }
            });
        }

        //Set the current player to player 1
        Player currentPlayer = players.get(0);

        //Loop until a player wins the game
        while (true) {
            //Print which player's turn it is
            System.out.println(currentPlayer.name + "'s turn.");

            /* 
            If the other player did not have the card the current player asked for,
            their turn is over. Set the current player to the other player.
            */
            if (!handleTurn(currentPlayer)) {
                currentPlayer = getOtherPlayer(currentPlayer);
            }
            //If there are no more cards in the deck, check which player won
            if (deck.getNumberOfCards() <= 0) {
                //Print each player's amount of matches
                players.forEach(player -> {
                    System.out.println(player.name + " has " + player.matches.size() + " matches.");
                });
                System.out.println();

                //Store each player
                Player player1 = players.get(0);
                Player player2 = players.get(1);
                //Store each player's amount of matches
                int player1MatchesCount = player1.matches.size();
                int player2MatchesCount = player2.matches.size();
                //Store the player who won
                Player winningPlayer = player1MatchesCount > player2MatchesCount ? player1 : player2;
                //Print which player won the game
                System.out.println(winningPlayer.name + " won the game!");
                //Break from the loop to finish the game
                break;
            }
        }
    }

    //Handle the current players turn
    private static boolean handleTurn(Player currentPlayer) {
        //Store the other player
        Player otherPlayer = getOtherPlayer(currentPlayer);
        
        //If the current player's hand is empty
        if (currentPlayer.hand.isEmpty()) {
            //Give the player a card from the deck and check if they made a match of 4
            deck.addCardToHand(currentPlayer.hand);
            Card.Value matchedValue = currentPlayer.checkForMatch();
            if (matchedValue == null) {
                System.out.println("Your hand was empty so you drew a card from the deck.");
            } else {
                System.out.println("Your hand was empty. You drew a card from the deck and made a match of 4 " + capitalizeString(pluralizeValue(matchedValue)) + ".");
            }
        }

        //Print the current player's cards
        System.out.println("Your cards:");
        currentPlayer.printCards();

        //String to store input from the current player
        String inputString;
        //Card value that the current player is asking for from the other player
        Card.Value requestedCardValue;
        //Loop until the current player enters a valid card value
        while (true) {
            //Tell the player what to input
            System.out.println("Enter a card value you want to ask for from " + otherPlayer.name + ". Enter \"matches\" to see each player's matches.\n");

            //Get the input from the current player
            inputString = inputScanner.nextLine();
            //If the player inputted "matches"
            if (inputString.toLowerCase().equals("matches")) {
                System.out.println();
                //For each player
                players.forEach(player -> {
                    //If the player has matches, print them
                    if (player.matches.size() > 0) {
                        System.out.println(player.name + "'s matches:");
                        player.matches.forEach(match -> System.out.println(capitalizeString(pluralizeValue(match))));
                        System.out.println();
                    } else { //If the player does not have any matches, print that they don't have any
                        System.out.println(player.name + " does not have any matches.\n");
                    }
                });
                //Start again from the beginning of the loop
                continue;
            }
            
            //Try to get the card value the player is asking for
            try {
                requestedCardValue = Enum.valueOf(Card.Value.class, inputString.toUpperCase());
            } catch (IllegalArgumentException e) { //If the inputted value is not a valid card value
                //If the player entered a card value in numerical form
                if (Card.numberMap.containsKey(inputString)) {
                    //Get the card value the player is asking for after converting the number to word form
                    requestedCardValue = Enum.valueOf(Card.Value.class, Card.numberMap.get(inputString));
                } else { //If the player did not enter a valid card value
                    //Tell the player to enter a valid card value
                    System.out.println("Please enter a valid card value.\n");
                    //Try again from the beginning of the loop
                    continue;
                }
            }

            //Once the player has entered a valid card value, check if they have that card value in their hand
            if (currentPlayer.hasCardWithValue(requestedCardValue)) {
                //If they do, break from the loop
                break;
            } else { //If the current player does not have this card value in their own hand
                //Tell them that they can't ask for a card value they don't have
                System.out.println("You cannot ask for a card value that you do not have in your hand.\n");
            }
        }
        //Print some empty lines
        for (int i = 0; i < 15; i++) {
            System.out.println();
        }
        //If the other player has the card value that the current player is asking for
        if (otherPlayer.hasCardWithValue(requestedCardValue)) {
            //Store the amount of cards the other player has with the requested value
            int matchingCardCount = otherPlayer.countMatchingCards(requestedCardValue);
            //Print how many cards the other player had with the requested value
            if (matchingCardCount > 1) {
                System.out.println(otherPlayer.name + " had " + matchingCardCount + " " + capitalizeString(pluralizeValue(requestedCardValue)) + ".");
            } else {
                System.out.println(otherPlayer.name + " had " + matchingCardCount + " " + capitalizeString(requestedCardValue.toString().toLowerCase()) + ".");
            }
            //Give all cards with the requested value from the other player to the current player
            otherPlayer.giveCardsWithValueTo(requestedCardValue, currentPlayer);
            //Check if the current player made a match of 4
            Card.Value matchedValue = currentPlayer.checkForMatch();
            //If the current player made a match, print which card value they matched
            if (matchedValue != null) {
                System.out.println("You made a match of 4 " + capitalizeString(pluralizeValue(matchedValue)) + ".");
            }
            System.out.println();
            //The player gets to go again, so return true
            return true;
        } else { //If the other player does not have the card value that the current player is asking for
            //Print that the other player does not have any cards with this value
            System.out.println(otherPlayer.name + " did not have any " + capitalizeString(pluralizeValue(requestedCardValue)) + ".");
            //Give the player a card from the deck
            deck.addCardToHand(currentPlayer.hand);
            //Check if the player made a match of 4
            Card.Value matchedValue = currentPlayer.checkForMatch();
            if (matchedValue == null) {
                System.out.println("You drew a card from the deck.");
            } else {
                System.out.println("You drew a card from the deck and made a match of 4 " + capitalizeString(pluralizeValue(matchedValue)) + ".");
            }
            System.out.println();
            //The player's turn is over, so return false
            return false;
        }
    }

    //Get the other player
    private static Player getOtherPlayer(Player currentPlayer) {
        return players.indexOf(currentPlayer) == 0 ? players.get(1) : players.get(0);
    }

    //Return the capitalized version of the inputted string
    private static String capitalizeString(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    //Return the pluralized string version of a card value
    private static String pluralizeValue(Card.Value cardValue) {
        String string = cardValue.toString().toLowerCase();
        return string + (string.charAt(string.length() - 1) == 'x' ? "es" : "s");
    }
}
