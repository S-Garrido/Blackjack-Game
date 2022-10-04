package blackjack;

import java.util.ArrayList;
import java.util.Random;

import deckOfCards.*;

public class BlackjackModel {
	
	private ArrayList<Card> dealerCards;
	private ArrayList<Card> playerCards;
	private Deck deck;
	
	public ArrayList<Card> getDealerCards(){							//Making Deep Copy to avoid Privacy Leak
		ArrayList<Card> copyDealerCards = new ArrayList<Card>();	
		for(Card c : dealerCards) {
			 Card newDealerCard = new Card(c.getRank(), c.getSuit()) ;
			copyDealerCards.add(newDealerCard);
		}
		return copyDealerCards;
	}
	
	public ArrayList<Card> getPlayerCards(){							//Making Deep Copy to avoid Privacy Leak
		ArrayList<Card> copyPlayerCards = new ArrayList<Card>();	
		for(Card c : playerCards) {
			 Card newPlayerCard = new Card(c.getRank(), c.getSuit()) ;
			copyPlayerCards.add(newPlayerCard);
		}
		return copyPlayerCards;
	}
	
	public void setDealerCards(ArrayList<Card> cards) {					//Making Deep Copy to avoid Privacy Leak
		ArrayList<Card> copyDealerSetCards = new ArrayList<Card>();	
		for(Card c : cards) {
			 Card newDealerCard = new Card(c.getRank(), c.getSuit()) ;
			copyDealerSetCards.add(newDealerCard);
		}
		dealerCards = new ArrayList<>();
		dealerCards = copyDealerSetCards;
	}
	
	public void setPlayerCards(ArrayList<Card> cards) {					//Making Deep Copy to avoid Privacy Leak
		ArrayList<Card> copyPlayerSetCards = new ArrayList<Card>();	
		for(Card c : cards) {
			 Card newDealerCard = new Card(c.getRank(), c.getSuit()) ;
			copyPlayerSetCards.add(newDealerCard);
		}
		playerCards = copyPlayerSetCards;
	}
	
	public void createAndShuffleDeck(Random random) {  				
		deck = new Deck();												//Assigning the deck variable to the class
		deck.shuffle(random);											//Using the shuffle method to shuffle in the deck class the deck
	}
		
	public void initialPlayerCards() {
		playerCards = new ArrayList<>();
		playerCards.add(deck.dealOneCard());							//Player gets hand dealt first
		playerCards.add(deck.dealOneCard());
	}
	
	public void initialDealerCards() {
		dealerCards = new ArrayList<>();
		dealerCards.add(deck.dealOneCard());							//Dealer gets hand dealt second
		dealerCards.add(deck.dealOneCard());
	}

	
	public void playerTakeCard() {
		playerCards.add(deck.dealOneCard());							//Player takes a card
	}
	
	public void dealerTakeCard() {
		dealerCards.add(deck.dealOneCard());							//Dealer takes a card
	}
	
	public static ArrayList<Integer> possibleHandValues(ArrayList<Card> hand){
		ArrayList<Integer> possibleValues = new ArrayList<Integer>();	 	//Our Array List of possible values that we will return
		
		Integer firstValue = 0;												//Lowest possible value
		Integer secondValue = 0;											//Highest possible value
		Integer acesCount = 0;												//Track number of aces
		
		for( Card c : hand) {
			if(c.getRank().getValue() == 1) {							//Checks for Aces (will compute Aces Last)
				acesCount += 1;
			}
			
			else {												
		  firstValue += c.getRank().getValue();							//Default value adder (no aces)
			}
		}
		
								//Now that all normal cards have been counted, we start logic for all aces
		if(acesCount > 0) {								//Checks for at least 1 ace
			firstValue += 1;								//Ace can be a one
			secondValue = firstValue + 10;					//Only the first Ace can be an 11 since 2 11's is a bust
		}
		
		if(acesCount > 1) {								//Checks for a 2nd, 3rd and 4th Ace
			firstValue += acesCount - 1;				//We add one less than the acesCount since aces at this point   
			secondValue += acesCount - 1;				//can only be worth 1, and we already used an ace.
		}
		
			//Now that we have our possible 1st and 2nd values all we need is logic to determine which to put into the possibleValues  ArrayList
		if(firstValue <= 21 && secondValue <= 21 && secondValue != 0) { 	//We only add both Values if they're BOTH less than 21
			possibleValues.add(firstValue);
			possibleValues.add(secondValue);
		}
		
		else {
			possibleValues.add(firstValue);
		}
		
		return possibleValues; 							//Return the Array list of possible values
	}
	
	public static HandAssessment assessHand(ArrayList<Card> hand) {
		if(hand == null || hand.size() < 2 ) {
			return HandAssessment.INSUFFICIENT_CARDS;	//Less than 2 cards or null hand is no good
		}
		else if(hand.size() == 2 && hand.get(0).getRank().getValue() == 1 && hand.get(1).getRank().getValue() == 10 || hand.size() == 2 && hand.get(1).getRank().getValue() == 1 && hand.get(0).getRank().getValue() == 10  ) {
			return HandAssessment.NATURAL_BLACKJACK;	// if the user has an ace and a value 10 card they have natural black Jack
		}
		else if(possibleHandValues(hand).get(0) > 21) { //Bust if min hand value is > 21
			return HandAssessment.BUST;
		}
		else {
			return HandAssessment.NORMAL;
		}
	}
	
	public GameResult gameAssessment() {
		if ( assessHand(playerCards) == HandAssessment.NATURAL_BLACKJACK && assessHand(dealerCards) != HandAssessment.NATURAL_BLACKJACK) {
			return GameResult.NATURAL_BLACKJACK; //Play has Natural BlackJack and dealer doesn't
			}
		
		else if( assessHand(playerCards) == HandAssessment.NATURAL_BLACKJACK && assessHand(dealerCards) == HandAssessment.NATURAL_BLACKJACK ) {
			return GameResult.PUSH;				//Both have Natural BlackJack
				}
		
		else if(assessHand(playerCards) == HandAssessment.BUST){
			return GameResult.PLAYER_LOST;		//Player Busted
				}
		
		else if (assessHand(dealerCards) == HandAssessment.BUST && assessHand(playerCards) != HandAssessment.BUST ){
			return GameResult.PLAYER_WON;		//Dealer busts and player does not
				}
		
		else if(possibleHandValues(playerCards).get(possibleHandValues(playerCards).size() - 1) > possibleHandValues(dealerCards).get(possibleHandValues(dealerCards).size() - 1)) {
			return GameResult.PLAYER_WON;		//Player hand > dealer hand
				}
		
		else if( possibleHandValues(playerCards).get(possibleHandValues(playerCards).size() - 1) < possibleHandValues(dealerCards).get(possibleHandValues(dealerCards).size() - 1)) {
			return GameResult.PLAYER_LOST;			//Player hand < dealer hand
				}
		
		else {
			return GameResult.PUSH;					// Tie
				}
	}
	
	public boolean dealerShouldTakeCard() {
		if(possibleHandValues(dealerCards).get(possibleHandValues(dealerCards).size() - 1) < 17) {
			return true;							//Dealer takes card if maximum hand is < 17
		}
		else if(possibleHandValues(dealerCards).size() == 2 && possibleHandValues(dealerCards).get(0) == 7 && possibleHandValues(dealerCards).get(1) == 17) {
			return true;							//Dealer takes card if has ace and hand value is either 7 or 17
		}
		else {
			return false;							//Dealer stops taking cards if hand smallest hand is 17 or highest hand is 17 but lowest can't be 7
		}
	}
	
}
