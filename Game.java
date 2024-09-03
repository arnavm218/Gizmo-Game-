import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;

public class Game {
	private Player currentPlayer;
	private TreeSet<Player> allPlayers;
	private static Deck tier1;
	private static Deck tier2;
	private static Deck tier3;
	private static TreeMap<Integer, ArrayList<Card>> playableCards;
	private boolean lastRound;
	
	private static MarbleDispenser dispenser; 
	
	private boolean end;
	private int turn;

	public Game(int numPlayers) 
	{
		tier1 = new Deck();
		tier2 = new Deck();
		tier3 = new Deck();
		playableCards = new TreeMap<Integer, ArrayList<Card>>();
		playableCards.put(1, new ArrayList<Card>());
		playableCards.put(2, new ArrayList<Card>());
		playableCards.put(3, new ArrayList<Card>());
		
		allPlayers = new TreeSet<>();
		for (int i = 1; i <= numPlayers; i++) {
			allPlayers.add(new Player(i));
		}
		//instantiate tier1, tier2, tier3
		makeCards();
		tier1.shuffle();
		tier2.shuffle();
		tier3.shuffle();
		for (int i = tier3.getNumCards() - 1; i >= 16; i--) {
			tier3.removeCard(i);
		}
		turn = 1;
		makePlayables();
		Iterator<Player> it = allPlayers.iterator();
		currentPlayer = it.next();
		
		FileCard startCard = null;
		try{
			BufferedImage cardImage = ImageIO.read(Game.class.getResource("/images/beginning_card.png"));
			startCard = new FileCard(0, "rainbow", cardImage, 0, 4);
		}
		catch(Exception E) {
			System.out.println("starter card error");
		}
		it = allPlayers.iterator();
		while(it.hasNext()) {
			Player temp = it.next();
			temp.addToInventory(startCard);
		}
		
		dispenser = new MarbleDispenser();
	}

	public boolean checkIfOver() {
		Iterator<Player> it = allPlayers.iterator();
		boolean lastRound = false;
		Player p = null;
		while (it.hasNext()) {
			p = it.next();

			if (p.getCardCount() >= 16 || p.getTier3Count() >= 4) {
				lastRound = true;
			}
		}
		if (lastRound && currentPlayer == p) {
			return true;
		}
		else {
			return false;
		}
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer() {
		turn +=1;
		if (turn == allPlayers.size() + 1) {
			turn = 1;
		}
		Player temp = null;
		Iterator<Player> it = allPlayers.iterator();
		for (int i = 0; i < turn; i++) {
			temp = it.next();
		}
		currentPlayer = temp;
	}

	public TreeSet<Player> getAllPlayers() {
		return allPlayers;
	}
	
	//instantiate cards
	public void makeCards()
	{	
		try
		{
			//TIER 1
			//red
			BufferedImage cardImage = ImageIO.read(Game.class.getResource("/images/1_red_1.png"));
			tier1.addCard(new FileCard(1, "red", cardImage, 1, 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_red_2.jpg"));
			tier1.addCard(new PickCard(1, "red", cardImage, 1, "blue", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_red_3.jpg"));
			tier1.addCard(new PickCard(1, "red", cardImage, 1, "yellow", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_red_4.jpg"));
			tier1.addCard(new BuildCard(1, "red", cardImage, 1, "black", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_red_5.jpg"));
			tier1.addCard(new BuildCard(1, "red", cardImage, 1, "yellow", 3));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_red_6.jpg"));
			tier1.addCard(new ConverterCard(1, "red", cardImage, 1, "black", 1, 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_red_7.jpg"));
			tier1.addCard(new ConverterCard(1, "red", cardImage, 1, "blue", 1, 1));			
			cardImage = ImageIO.read(Game.class.getResource("/images/1_red_8.jpg"));
			tier1.addCard(new UpgradeCard(1, "red", cardImage, 1, 1, 1, 0, 1)); 
			cardImage = ImageIO.read(Game.class.getResource("/images/1_red_9.jpg"));
			tier1.addCard(new UpgradeCard(1, "red", cardImage, 1, 1, 1, 1, 0));
			//yellow
			cardImage = ImageIO.read(Game.class.getResource("/images/1_yellow_10.jpg"));
			tier1.addCard(new FileCard(1, "yellow", cardImage, 1, 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_yellow_11.jpg"));
			tier1.addCard(new PickCard(1, "yellow", cardImage, 1, "red", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_yellow_12.jpg"));
			tier1.addCard(new PickCard(1, "yellow", cardImage, 1, "black", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_yellow_13.jpg"));
			tier1.addCard(new BuildCard(1, "yellow", cardImage, 1, "red", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_yellow_14.jpg"));
			tier1.addCard(new BuildCard(1, "yellow", cardImage, 1, "blue", 3));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_yellow_15.jpg"));
			tier1.addCard(new ConverterCard(1, "yellow", cardImage, 1, "blue", 1, 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_yellow_16.jpg"));
			tier1.addCard(new ConverterCard(1, "yellow", cardImage, 1, "black", 1, 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_yellow_17.jpg"));
			tier1.addCard(new UpgradeCard(1, "yellow", cardImage, 1, 1, 1, 0, 1)); 
			cardImage = ImageIO.read(Game.class.getResource("/images/1_yellow_18.jpg"));
			tier1.addCard(new UpgradeCard(1, "yellow", cardImage, 1, 1, 1, 1, 0));
			//blue
			cardImage = ImageIO.read(Game.class.getResource("/images/1_blue_19.jpg"));
			tier1.addCard(new FileCard(1, "blue", cardImage, 1, 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_blue_20.jpg"));
			tier1.addCard(new PickCard(1, "blue", cardImage, 1, "black", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_blue_21.jpg"));
			tier1.addCard(new PickCard(1, "blue", cardImage, 1, "red", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_blue_22.jpg"));
			tier1.addCard(new BuildCard(1, "blue", cardImage, 1, "yellow", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_blue_23.jpg"));
			tier1.addCard(new BuildCard(1, "blue", cardImage, 1, "black", 3));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_blue_24.jpg"));
			tier1.addCard(new ConverterCard(1, "blue", cardImage, 1, "red", 1, 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_blue_25.jpg"));
			tier1.addCard(new ConverterCard(1, "blue", cardImage, 1, "yellow", 1, 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_blue_26.jpg"));
			tier1.addCard(new UpgradeCard(1, "blue", cardImage, 1, 1, 1, 0, 1)); 
			cardImage = ImageIO.read(Game.class.getResource("/images/1_blue_27.jpg"));
			tier1.addCard(new UpgradeCard(1, "blue", cardImage, 1, 1, 1, 1, 0));
			//black
			cardImage = ImageIO.read(Game.class.getResource("/images/1_black_28.jpg"));
			tier1.addCard(new FileCard(1, "black", cardImage, 1, 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_black_29.jpg"));
			tier1.addCard(new PickCard(1, "black", cardImage, 1, "yellow", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_black_30.jpg"));
			tier1.addCard(new PickCard(1, "black", cardImage, 1, "blue", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_black_31.jpg"));
			tier1.addCard(new BuildCard(1, "black", cardImage, 1, "blue", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_black_32.jpg"));
			tier1.addCard(new BuildCard(1, "black", cardImage, 1, "red", 3));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_black_33.jpg"));
			tier1.addCard(new ConverterCard(1, "black", cardImage, 1, "yellow", 1, 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_black_34.jpg"));
			tier1.addCard(new ConverterCard(1, "black", cardImage, 1, "red", 1, 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/1_black_35.jpg"));
			tier1.addCard(new UpgradeCard(1, "black", cardImage, 1, 1, 1, 0, 1)); 
			cardImage = ImageIO.read(Game.class.getResource("/images/1_black_36.jpg"));
			tier1.addCard(new UpgradeCard(1, "black", cardImage, 1, 1, 1, 1, 0));
			
			
			//TIER 2
			//red
			cardImage = ImageIO.read(Game.class.getResource("/images/2_red_37.jpg"));
			tier2.addCard(new PickCard(2, "red", cardImage, 2, "blue", "black", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_red_38.jpg"));
			tier2.addCard(new BuildCard(2, "red", cardImage, 3, "yellow", "black", 3));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_red_39.jpg"));
			tier2.addCard(new BuildCard(2, "red", cardImage, 2, "blue", "yellow", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_red_40.jpg"));
			tier2.addCard(new BuildCard(2, "red", cardImage, 2, "blue", "black", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_red_41.jpg"));
			tier2.addCard(new BuildCard(2, "red", cardImage, 3, true, 2));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_red_42.jpg"));
			tier2.addCard(new ConverterCard(2, "red", cardImage, 2, "yellow", "yellow", 2, 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_red_43.jpg"));
			tier2.addCard(new ConverterCard(2, "red", cardImage, 3, "black", 3, 2));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_red_44.jpg"));
			tier2.addCard(new ConverterCard(2, "red", cardImage, 3, "blue", 3, 2));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_red_45.jpg"));
			tier2.addCard(new UpgradeCard(1, "red", cardImage, 3, 1, 2, 1, 2));
			//yellow
			cardImage = ImageIO.read(Game.class.getResource("/images/2_yellow_46.jpg"));
			tier2.addCard(new PickCard(2, "yellow", cardImage, 2, "red", "blue", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_yellow_47.jpg"));
			tier2.addCard(new BuildCard(2, "yellow", cardImage, 3, "red", "blue", 3));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_yellow_48.jpg"));
			tier2.addCard(new BuildCard(2, "yellow", cardImage, 2, "blue", "black", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_yellow_49.jpg"));
			tier2.addCard(new BuildCard(2, "yellow", cardImage, 2, "black", "red", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_yellow_50.jpg"));
			tier2.addCard(new BuildCard(2, "yellow", cardImage, 3, true, 2));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_yellow_51.jpg"));
			tier2.addCard(new ConverterCard(2, "yellow", cardImage, 2, "red", "red", 2, 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_yellow_52.jpg"));
			tier2.addCard(new ConverterCard(2, "yellow", cardImage, 3, "blue", 3, 2));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_yellow_53.jpg"));
			tier2.addCard(new ConverterCard(2, "yellow", cardImage, 3, "black", 3, 2));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_yellow_54.jpg"));
			tier2.addCard(new UpgradeCard(1, "yellow", cardImage, 3, 1, 2, 1, 2));
			//blue
			cardImage = ImageIO.read(Game.class.getResource("/images/2_blue_55.jpg"));
			tier2.addCard(new PickCard(2, "blue", cardImage, 2, "yellow", "black", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_blue_56.jpg"));
			tier2.addCard(new BuildCard(2, "blue", cardImage, 3, "black", "red", 3));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_blue_57.jpg"));
			tier2.addCard(new BuildCard(2, "blue", cardImage, 2, "yellow", "red", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_blue_58.jpg"));
			tier2.addCard(new BuildCard(2, "blue", cardImage, 2, "yellow", "black", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_blue_59.jpg"));
			tier2.addCard(new BuildCard(2, "blue", cardImage, 3, true, 2));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_blue_60.jpg"));
			tier2.addCard(new ConverterCard(2, "blue", cardImage, 2, "black", "black", 2, 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_blue_61.jpg"));
			tier2.addCard(new ConverterCard(2, "blue", cardImage, 3, "red", 3, 2));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_blue_62.jpg"));
			tier2.addCard(new ConverterCard(2, "blue", cardImage, 3, "yellow", 3, 2));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_blue_63.jpg"));
			tier2.addCard(new UpgradeCard(1, "blue", cardImage, 3, 1, 2, 1, 2));
			//black
			cardImage = ImageIO.read(Game.class.getResource("/images/2_black_64.jpg"));
			tier2.addCard(new PickCard(2, "black", cardImage, 2, "yellow", "red", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_black_65.jpg"));
			tier2.addCard(new BuildCard(2, "black", cardImage, 3, "blue", "yellow", 3));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_black_66.jpg"));
			tier2.addCard(new BuildCard(2, "black", cardImage, 2, "red", "blue", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_black_67.jpg"));
			tier2.addCard(new BuildCard(2, "black", cardImage, 2, "yellow", "red", 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_black_68.jpg"));
			tier2.addCard(new BuildCard(2, "black", cardImage, 3, true, 2));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_black_69.jpg"));
			tier2.addCard(new ConverterCard(2, "black", cardImage, 2, "blue", "blue", 2, 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_black_70.jpg"));
			tier2.addCard(new ConverterCard(2, "black", cardImage, 3, "yellow", 3, 2));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_black_71.jpg"));
			tier2.addCard(new ConverterCard(2, "black", cardImage, 3, "red", 3, 2));
			cardImage = ImageIO.read(Game.class.getResource("/images/2_black_72.jpg"));
			tier2.addCard(new UpgradeCard(1, "black", cardImage, 3, 1, 2, 1, 2));
			
			
			//TIER 3
			//red
			cardImage = ImageIO.read(Game.class.getResource("/images/3_red_73.jpg"));
			tier3.addCard(new FileCard(3, "red", cardImage, 4, 2));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_red_74.jpg"));
			tier3.addCard(new ConverterCard(3, "red", cardImage, 4, "rainbow", 5, 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_red_75.jpg"));
			tier3.addCard(new UpgradeCard(3, "red", cardImage, 7, 4, 3, 0, 0, 0));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_red_76.jpg"));
			tier3.addCard(new BuildCard(3, "red", cardImage, 5, "yellow", "black", 4));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_red_77.jpg"));
			tier3.addCard(new BuildCard(3, "red", cardImage, 5, true, 4));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_red_78.jpg"));
			tier3.addCard(new UpgradeCard(3, "red", cardImage, 5, 7, 0, 0, 0));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_red_79.jpg"));
			tier3.addCard(new BuildCard(3, "red", cardImage, 6, 2, true));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_red_80.jpg"));
			tier3.addCard(new BuildCard(3, "red", cardImage, 7, "blue", "black", 7));
			//yellow
			cardImage = ImageIO.read(Game.class.getResource("/images/3_yellow_81.jpg"));
			tier3.addCard(new FileCard(3, "yellow", cardImage, 4, 3));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_yellow_82.jpg"));
			tier3.addCard(new ConverterCard(3, "yellow", cardImage, 4, "rainbow", 5, 1));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_yellow_83.jpg"));
			tier3.addCard(new UpgradeCard(3, "yellow", cardImage, 8, 4, 4, 0, 0, 0));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_yellow_84.jpg"));
			tier3.addCard(new BuildCard(3, "yellow", cardImage, 5, "red", "black", 5));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_yellow_85.jpg"));
			tier3.addCard(new BuildCard(3, "yellow", cardImage, 5, true, 4));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_yellow_86.jpg"));
			tier3.addCard(new UpgradeCard(3, "yellow", cardImage, 5, 2, 0, 0, 0));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_yellow_87.jpg"));
			tier3.addCard(new BuildCard(3, "yellow", cardImage, 6, "blue", "black", 6));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_yellow_88.jpg"));
			//blue
			cardImage = ImageIO.read(Game.class.getResource("/images/3_blue_89.jpg"));
			tier3.addCard(new FileCard(3, "blue", cardImage, 4, 3));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_blue_90.jpg"));
			tier3.addCard(new UpgradeCard(3, "blue", cardImage, 4, 1, 4, 0, 0));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_blue_91.jpg"));
			tier3.addCard(new UpgradeCard(3, "blue", cardImage, 7, 4, 3, 0, 0, 0));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_blue_92.jpg"));
			tier3.addCard(new ConverterCard(3, "blue", cardImage, 5, "black", "red", 4, 2));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_blue_93.jpg"));
			tier3.addCard(new UpgradeCard(3, "blue", cardImage, 5, 2, 0, 0, 0));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_blue_94.jpg"));
			tier3.addCard(new UpgradeCard(3, "blue", cardImage, 5, 7, 0, 0, 0));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_blue_95.jpg"));
			tier3.addCard(new BuildCard(3, "blue", cardImage, 6, "yellow", "red", 6));
			//cardImage = ImageIO.read(Game.class.getResource("/images/3_blue_96.jpg"));  missing
			cardImage = ImageIO.read(Game.class.getResource("/images/3_blue_97.jpg"));
			tier3.addCard(new BuildCard(3, "blue", cardImage, 7, "yellow", "red", 7));
			//black
			cardImage = ImageIO.read(Game.class.getResource("/images/3_black_98.jpg"));
			tier3.addCard(new FileCard(3, "black", cardImage, 4, 2));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_black_99.jpg"));
			tier3.addCard(new BuildCard(3, "black", cardImage, 5, "blue", "yellow", 5));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_black_100.jpg"));
			tier3.addCard(new UpgradeCard(3, "black", cardImage, 4, 1, 4, 0, 0));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_black_101.jpg"));
			tier3.addCard(new UpgradeCard(3, "black", cardImage, 8, 4, 4, 0, 0, 0));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_black_102.jpg"));
			tier3.addCard(new BuildCard(3, "red", cardImage, 5, "red", "blue", 4));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_black_103.jpg"));
			tier3.addCard(new ConverterCard(3, "blue", cardImage, 5, "blue", "yellow", 4, 2));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_black_104.jpg"));
			tier3.addCard(new BuildCard(3, "black", cardImage, 6, 2, true));
			//rainbow
			cardImage = ImageIO.read(Game.class.getResource("/images/3_rainbow_105.jpg"));
			tier3.addCard(new UpgradeCard(3, "rainbow", cardImage, 0, 6, 0, 0, 0));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_rainbow_106.jpg"));
			tier3.addCard(new UpgradeCard(3, "rainbow", cardImage, 0, 6, 0, 0, 0));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_rainbow_107.jpg"));
			tier3.addCard(new UpgradeCard(3, "rainbow", cardImage, 0, 5, 0, 0, 0));
			cardImage = ImageIO.read(Game.class.getResource("/images/3_rainbow_108.jpg"));
			tier3.addCard(new UpgradeCard(3, "rainbow", cardImage, 0, 5, 0, 0, 0));
		}
		catch(Exception E) 
		{
			System.out.println("error with cards");
		}
	}
	public void makePlayables()
	{
		int numNeeded = 4 - playableCards.get(1).size();
		for(int i = 0; i < numNeeded; i++)
		{
			playableCards.get(1).add(tier1.removeCard(0));
		}
		numNeeded = 3 - playableCards.get(2).size();
		for(int i = 0; i < numNeeded; i++)
		{
			playableCards.get(2).add(tier2.removeCard(0));
		}
		numNeeded = 2 - playableCards.get(3).size();
		for(int i = 0; i < numNeeded; i++)
		{
			playableCards.get(3).add(tier3.removeCard(0));
		}
	}
	public void play()
	{
		
	}
	public void endGame() {
		Player lastPlayer = null;
		Iterator<Player> it = allPlayers.iterator();
		while (it.hasNext()) {
			lastPlayer = it.next();
		}
		if (currentPlayer == lastPlayer) {
			end = true;
		}
	}
	public TreeMap<Player, Integer> getScoreBoard(){
		TreeMap<Player, Integer> scoreboard = new TreeMap<>();
		Iterator<Player> it = allPlayers.iterator();
		while (it.hasNext()) {
			Player temp = it.next();
			scoreboard.put(temp, temp.getVictoryPoints());
		}
		return scoreboard;
	}
	
	
	
	//GETTERS + SETTERS?
	public static MarbleDispenser getDispenser() {
		return dispenser;
	}
	
	public Deck getDeck(int i)
	{
		if(i == 1)
			return tier1;
		if(i == 2)
			return tier2;
		if(i == 3)
			return tier3;
		return null;
	}
	
	public TreeMap<Integer, ArrayList<Card>> getPlayables()
	{
		return playableCards;
	}
	
	public ArrayList<Card> getResearchCards(int tier){
		ArrayList<Card> researchCards = new ArrayList<>();
		for (int i = 0; i < currentPlayer.getResearchAmount(); i++) {
			if (tier == 1) {
				researchCards.add(tier1.removeCard(0));
			}
			if (tier == 2) {
				researchCards.add(tier2.removeCard(0));
			}
			if (tier == 3) {
				researchCards.add(tier3.removeCard(0));
			}
		}
		return researchCards;
	}
}
