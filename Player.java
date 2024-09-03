import java.util.*;

public class Player implements Comparable<Player> {
	private int order;
	private Marble[] marbles;
	private Card[] archive;
	private int researchAmt;
	private TreeMap<String, TreeSet<Card>> ownedGizmos;
	private boolean isPlaying;
	private int victoryPoints; // physical point cards
	private int totalPoints = 0;
	private int cardCount;
	private int tier3Count; // add whenever a player obtains tier 3 cards
	private boolean canResearch = true;
	private boolean canFile = true;
	private boolean marblesToPoints = false;
	private boolean doublePhysicalVP = false;
	private int level2Discount = 0;
	private int archiveDiscount = 0;
	private int researchDiscount = 0;

	// BOOLEANS HELPING WITH THE GUI
	private ArrayList<Card> researchingCards;
	private boolean picking;
	private boolean researching;
	private boolean filing;
	private boolean converting;
	private boolean building;

	private boolean applyingEffects;
	private String effectsTrigger;
	private int maxPick = 1;

	public Player(int num) {
		order = num;
		researchAmt = 3; // one in archive, five energy marbles
		archive = new Card[1];
		marbles = new Marble[5];
		ownedGizmos = new TreeMap<String, TreeSet<Card>>();
		ownedGizmos.put("build", new TreeSet<Card>());
		ownedGizmos.put("convert", new TreeSet<Card>());
		ownedGizmos.put("upgrade", new TreeSet<Card>());
		ownedGizmos.put("file", new TreeSet<Card>());
		ownedGizmos.put("pick", new TreeSet<Card>());
		researchingCards = null;

	}

	public int compareTo(Player p) {
		return order - p.order;

	}

	public int getCardCount() {
		return cardCount;
	}

	public int getTier3Count() {
		return tier3Count;
	}

	public Map<String, TreeSet<Card>> getInventory() {
		return ownedGizmos;
	}

	public void pick() {// sets the picking PIV to true/false
		picking = !picking;
	}

	public void pick(boolean b) {
		picking = b;
	}

	public void pick(Marble m) {
		// philip needs to do this one!
		for (int i = 0; i < marbles.length; i++) {
			if (marbles[i] == null) {
				marbles[i] = m;
				break;
			}
		}
	}

	public void convert(String convertColor, String newColor, int count) {
		int temp = 0;
		for (int i = 0; i < marbles.length; i++) {
			if (marbles[i] != null && marbles[i].getColor().equals(convertColor) && !marbles[i].isConvertedColor()) {
				marbles[i].setColor(newColor);
				marbles[i].setConvertedColor(true);
				temp++;
				if (temp == count) {
					System.out.println("Marble properties changed");
					break;
				}
			}
		}
	}

	public void multiplyConvert(String color, int multiplier) {
		for (int i = 0; i < marbles.length; i++) {
			if (marbles[i] != null && marbles[i].getColor().equals(color) && !marbles[i].isConvertedNum()) {
				marbles[i].setValue(multiplier);
				marbles[i].setConvertedNum(true);
				System.out.println("Marble properties changed");
				break;
			}
		}
	}

	public void multiplyConvert(String color1, String color2, int multiplier) {
		for (int i = 0; i < marbles.length; i++) {
			if (marbles[i] != null && (marbles[i].getColor().equals(color1)
					|| marbles[i].getColor().equals(color2) && !marbles[i].isConvertedNum())) {
				marbles[i].setValue(multiplier);
				marbles[i].setConvertedNum(true);
				System.out.println("Marble properties changed");
				break;
			}
		}
	}

	public void draw() {
		Marble m = Game.getDispenser().getRandom();
		boolean drawn = false;
		for (int i = 0; i < marbles.length; i++) {
			if (marbles[i] == null) {
				marbles[i] = m;
				drawn = true;
				System.out.println(m + " added to inventory");
				break;
			}
		}
		if (!drawn) {
			Game.getDispenser().returnToDispenser(m);
		}

	}

	public boolean build(Card c) {
		if (c == null) {
			return false;
		}
		int cost = c.getCost();
		if (c.getLevel() == 2) {
			cost -= level2Discount;
		}
		String color = c.getColor();
		boolean canBuild = false;
		int count = 0;
		for (int i = 0; i < marbles.length; i++) {
			if (marbles[i] != null && (marbles[i].getColor().equals(color) || color.equals("rainbow")
					|| marbles[i].getOGColor().equals("color"))) {
				count += marbles[i].getValue();
			}
		} // calculates cost with color
		if (count >= cost) {
			canBuild = true;
			addToInventory(c);
			// start to return the marbles into the dispenser
			int temp = 0;
			for (int i = 0; i < marbles.length; i++) {
				if (marbles[i] != null && (marbles[i].getColor().equals(color) || color.equals("rainbow")
						|| marbles[i].getOGColor().equals("color")) && temp < cost) {
					temp += marbles[i].getValue();
					marbles[i].revert();
					Game.getDispenser().returnToDispenser(marbles[i]);
					marbles[i] = null;
					reorderMarbles(i);
					i--;
				}
			}
		}
		totalPoints += c.getPoints();
		// apply effects automatically if it's an upgrade card
		if (canBuild && c.getTrigger().equals("upgrade")) {
			UpgradeCard up = (UpgradeCard) c;
			up.effect(this);
		} else if (canBuild) {
			applyingEffects = true;
			setEffectsTrigger(c.getTrigger());
		}
		// converts always happen before the building
		return canBuild;
	}

	public boolean archive(Card c) {
		// philip needs to do this one!
		for (int i = 0; i < archive.length; i++) {
			if (archive[i] == null) {
				archive[i] = c;
				return true;
			}
		}
		return false;
	}

	public void upgrade(int addMarbleInv, int addArchiveInv, int addResearchInv) {
		// philip needs to do this one!
		Marble[] temp = new Marble[marbles.length + addMarbleInv];
		for (int i = 0; i < marbles.length; i++) {
			temp[i] = marbles[i];
		}
		marbles = temp;
		Card[] archiveTemp = new Card[archive.length + addArchiveInv];
		for (int i = 0; i < archive.length; i++) {
			archiveTemp[i] = archive[i];
		}
		archive = archiveTemp;
		researchAmt += addResearchInv;
	}

	public void reorderMarbles(int start) {
		for (int i = start; i < marbles.length - 1; i++) {
			marbles[i] = marbles[i + 1];
		}
		marbles[marbles.length - 1] = null;
	}

	public void reorderArchive(int start) {
		for (int i = start; i < archive.length - 1; i++) {
			archive[i] = archive[i + 1];
		}
		archive[archive.length - 1] = null;

	}

	public void applyEffects() {

	}
	// GETTERS + SETTERS

	public void setVictoryPoints(int i) {
		victoryPoints += i;
	}

	public int getLevel2Discount() {
		return level2Discount;
	}

	public void setLevel2Discount(int level2Discount) {
		this.level2Discount = level2Discount;
	}

	public void setCanFile(Boolean b) {
		canFile = !canFile;
	}

	public void setCanResearch(Boolean b) {
		canResearch = !canResearch;
	}

	public void setMarblesToPoints() {
		marblesToPoints = !marblesToPoints;
	}

	public void setDoublePhysicalVP() {
		doublePhysicalVP = !doublePhysicalVP;
	}

	public void setArchiveDiscount(int i) {
		archiveDiscount = i;
	}

	public int getArchiveDiscount() {
		return archiveDiscount;
	}

	public void setResearchDiscount(int i) {
		researchDiscount = i;
	}

	public int getResearchDiscount() {
		return researchDiscount;
	}

	// get methods for GUI Popups/Status
	public boolean getResearchPopup() {
		return researching;
	}

	public boolean getFilingPopup() {
		return filing;
	}

	public boolean getConvertPopup() {
		return converting;
	}

	public boolean getBuildingPopup() {
		return building;
	}

	public boolean getPickingPopup() {
		return picking;
	}

	public void research() {
		// philip needs to do this one!
		researching = !researching;
	}

	public void file() {
		filing = !filing;
	}

	public void convert() {
		converting = !converting;
	}

	public void convert(boolean b) {
		converting = b;
	}

	public void build() {
		// philip needs to do this one!
		building = !building;
	}

	public void build(boolean b) {
		building = b;
	}

	public String toString() {
		return "Player " + order;
	}

	public int getVictoryPoints() {
		return victoryPoints;
	}

	public void addToInventory(Card c) {
		TreeSet<Card> temp = ownedGizmos.get(c.getTrigger());
		temp.add(c);
		if (c.getLevel() == 3) {
			tier3Count++;
		}
		cardCount++;
	}

	public Card[] getArchive() {
		return archive;
	}

	public Marble[] getMarbles() {
		return marbles;
	}

	public ArrayList<Card> getResearchingCards() {
		return researchingCards;
	}

	public void setResearchingCards(ArrayList<Card> arr) {
		researchingCards = arr;
	}

	public int getResearchAmount() {
		return researchAmt;
	}

	public boolean isApplyingEffects() {
		return applyingEffects;
	}

	public void setApplyingEffects() {
		applyingEffects = !applyingEffects;
	}

	public void setApplyingEffects(boolean b) {
		applyingEffects = b;
	}

	public String getEffectsTrigger() {
		return effectsTrigger;
	}

	public void setEffectsTrigger(String effectsTrigger) {
		this.effectsTrigger = effectsTrigger;
	}

	public int getMaxPick() {
		return maxPick;
	}

	public void setMaxPick(int maxPick) {
		this.maxPick = maxPick;
	}

	public void file(boolean b) {
		filing = false;

	}

	public void research(boolean b) {
		researching = false;

	}

	public boolean canFile() {
		return canFile;
	}

	public boolean canResearch() {
		return canResearch;
	}
	public String getMarbleCounts() {
		int blue = 0;
		int black = 0;
		int red = 0;
		int yellow = 0;
		for (int i = 0; i < marbles.length; i++) {
			if (marbles[i] != null && marbles[i].getOGColor().equals("blue")) {
				blue++;
			}
			if (marbles[i] != null && marbles[i].getOGColor().equals("red")) {
				red++;
			}
			if (marbles[i] != null && marbles[i].getOGColor().equals("black")) {
				black++;
			}
			if (marbles[i] != null && marbles[i].getOGColor().equals("yellow")) {
				yellow++;
			}
		}
		return "Marbles(r,b,g,y): " + red + "," + blue + "," + black + "," + yellow;
	}
	public void updateVictoryPoints() {
		if (doublePhysicalVP) {
			totalPoints += (victoryPoints * 2);
		}
		else {
			totalPoints += victoryPoints;
		}
		
		if (marblesToPoints) {
			int temp = marbles.length;
			for (int i = 0; i < marbles.length; i++) {
				if (marbles[i] == null) {
					temp = i;
					break;
				}
			}
			totalPoints += temp;
		}
	}
	public int getTotalPoints() {
		return totalPoints;
	}
}
