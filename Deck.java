import java.util.*;

public class Deck 
{
	private int level;
	private ArrayList<Card> deck;
	
	public Deck()
	{
		deck = new ArrayList<>();
	}
	
	public void addCard(Card c)
	{
		deck.add(c);
	}
	
	public Card removeCard(int i)
	{
		return deck.remove(i);
	}
	
	public Card getCard(int i)
	{
		return deck.get(i);
	}
	
	public int getNumCards()
	{
		return deck.size();
	}
	
	public void shuffle()
	{
		for(int i = 0; i < deck.size(); i++)
		{
			int k = (int)(Math.random() * deck.size());
			Card temp = deck.get(k);
			deck.set(k, deck.get(i));
			deck.set(i, temp);
		}
	}
}
