package deckOfCards;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {

	
	private ArrayList<Card> deck; //makes a blank array list for the deck
	
	public Deck() {       				 //This constructor will populate the deck in the specified order
		deck = new ArrayList<Card>();
		for(Suit s : Suit.values()) {
			for (Rank r : Rank.values()){
				
			Card nextCard = new Card(r, s);
			deck.add(nextCard);
		 }
		}
	}
	
	public void shuffle(Random randomNumberGenerator){
			Collections.shuffle(deck, randomNumberGenerator);		//Shuffle based on the random number input
		}	
		
	public Card dealOneCard() {
			Card firstCard = deck.get(0);			//Store the first card
			deck.remove(0);							//remove the first card
			return firstCard;						//return the stored card
		}
	 
}

