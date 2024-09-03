import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

public class GizmosPanel extends JPanel implements MouseListener {

	private BufferedImage background, toolbar, browntoolbar;

	private BufferedImage rule1, rule2, rule3, rule4, book, next, dispenser, left, right, ring, redMarble, yellowMarble,
			blackMarble, blueMarble;
	private BufferedImage tier1, tier2, tier3;
	private Game game;
	private GizmosFrame frame;

	// HELPER PIVS FOR POPUPS (rules, etc.)
	private ConverterCard convertingCard;
	private TreeSet<Card> effectCards;
	private int actionNumber; // players can only pick ONE action FROM THE TOOLBAR(doesn't count effects)
	private static int pickingNumber;// number of times a player picks
	private static boolean choice;
	private boolean switchPage;
	private boolean rulesDisplaying;
	private static Card currentBuildCard;

	private boolean choosingColor;
	private boolean choosingOriginalColor;
	private String originalColor;

	public GizmosPanel(int i, GizmosFrame frame) {

		try {
			browntoolbar = ImageIO.read(GizmosPanel.class.getResource("/images/BrownToolBar.jpeg"));
			ring = ImageIO.read(GizmosPanel.class.getResource("/images/energyring.png"));
			toolbar = ImageIO.read(GizmosPanel.class.getResource("/images/toolbar.png"));
			background = ImageIO.read(GizmosPanel.class.getResource("/images/bavck.png"));
			rule1 = ImageIO.read(GizmosPanel.class.getResource("/images/page3.png"));
			rule2 = ImageIO.read(GizmosPanel.class.getResource("/images/page4.png"));
			rule3 = ImageIO.read(GizmosPanel.class.getResource("/images/page5.png"));
			rule4 = ImageIO.read(GizmosPanel.class.getResource("/images/page6.png"));
			book = ImageIO.read(GizmosPanel.class.getResource("/images/book.jpg"));
			next = ImageIO.read(GizmosPanel.class.getResource("/images/next.jpg"));
			dispenser = ImageIO.read(GizmosPanel.class.getResource("/images/MarbleDispenser.png"));
			left = ImageIO.read(GizmosPanel.class.getResource("/images/leftarrow.png"));
			right = ImageIO.read(GizmosPanel.class.getResource("/images/rightarrow.png"));
			tier1 = ImageIO.read(GizmosPanel.class.getResource("/images/tier1.png"));
			tier2 = ImageIO.read(GizmosPanel.class.getResource("/images/tier2.png"));
			tier3 = ImageIO.read(GizmosPanel.class.getResource("/images/tier3.png"));
			redMarble = ImageIO.read(GizmosPanel.class.getResource("/images/redmarble.png"));
			yellowMarble = ImageIO.read(GizmosPanel.class.getResource("/images/yellowmarble.png"));
			blackMarble = ImageIO.read(GizmosPanel.class.getResource("/images/blackmarble.png"));
			blueMarble = ImageIO.read(GizmosPanel.class.getResource("/images/bluemarble.png"));

		} catch (Exception E) {
			System.out.println("Exception Error");
			return;
		}
		addMouseListener(this);
		game = new Game(i);
		choice = false;
		actionNumber = 0;
		effectCards = new TreeSet<>();
		this.frame = frame;
		choosingColor = false;
	}

	public void paint(Graphics g) {
		super.paint(g);

		g.drawImage(background, 0, 0, 1920, 1080, null);
		drawToolBar(g);
		drawNextButton(g);

		drawArchive(g);
		drawDispenser(g);
		drawEnergyGrid(g);
		drawScoreBoard(g);
		drawPlayables(g);
		drawPlayerDeck(g);

		if (!choice) {
			drawAction(g);
		} else {
			drawChoice(g);
		}

		// book icon
		g.drawImage(book, 1560, 30, 136, 80, null);
		g.drawRect(1560, 30, 136, 80);

		if (rulesDisplaying) {
			displayRules(g);
		}
	}

	public void drawNextButton(Graphics g) {
		g.drawImage(next, 1726, 30, 136, 80, null);
		g.drawRect(1726, 30, 136, 80);

	}

	public void drawPlayerDeck(Graphics g) {
		Player temp = game.getCurrentPlayer();
		TreeSet<Card> cards = temp.getInventory().get("upgrade");
		int x = 5;
		int y = 125;
		Iterator<Card> it = cards.iterator();
		while (it.hasNext()) {
			g.drawImage(it.next().getImage(), x, y, 180, 180, null);
			y += 40;
		}

		cards = temp.getInventory().get("convert");
		x = 185;
		y = 125;
		it = cards.iterator();
		while (it.hasNext()) {
			g.drawImage(it.next().getImage(), x, y, 180, 180, null);
			y += 40;
		}

		cards = temp.getInventory().get("file");
		x = 375;
		y = 125;
		it = cards.iterator();
		while (it.hasNext()) {
			g.drawImage(it.next().getImage(), x, y, 180, 180, null);
			y += 40;
		}

		cards = temp.getInventory().get("pick");
		x = 585;
		y = 125;
		it = cards.iterator();
		while (it.hasNext()) {
			g.drawImage(it.next().getImage(), x, y, 180, 180, null);
			y += 40;
		}

		cards = temp.getInventory().get("build");
		x = 755;
		y = 125;
		it = cards.iterator();
		while (it.hasNext()) {
			g.drawImage(it.next().getImage(), x, y, 180, 180, null);
			y += 40;
		}
		drawOtherDecks(g);
	}

	public void drawOtherDecks(Graphics g) {
		Iterator<Player> it = game.getAllPlayers().iterator();
		TreeSet<Player> temp = new TreeSet<>();
		while (it.hasNext()) {
			temp.add(it.next());
		}
		temp.remove(game.getCurrentPlayer());
		it = temp.iterator();
		int x = 1340;
		int y = 175;

		for (int i = 0; i < temp.size(); i++) {
			Player other = it.next();
			TreeSet<Card> cards = other.getInventory().get("upgrade");
			Iterator<Card> inner = cards.iterator();
			while (inner.hasNext()) {
				g.drawImage(inner.next().getImage(), x, y, 100, 100, null);
				y += 25;
			}
			x += 100;
			y = 175 + (300 * i);
			cards = other.getInventory().get("convert");
			inner = cards.iterator();
			while (inner.hasNext()) {
				g.drawImage(inner.next().getImage(), x, y, 100, 100, null);
				y += 25;
			}
			x += 100;
			y = 175 + (300 * i);
			cards = other.getInventory().get("file");
			inner = cards.iterator();
			while (inner.hasNext()) {
				g.drawImage(inner.next().getImage(), x, y, 100, 100, null);
				y += 25;
			}
			x += 100;
			y = 175 + (300 * i);
			cards = other.getInventory().get("pick");
			inner = cards.iterator();
			while (inner.hasNext()) {
				g.drawImage(inner.next().getImage(), x, y, 100, 100, null);
				y += 25;
			}
			x += 100;
			y = 175 + (300 * i);
			cards = other.getInventory().get("build");
			inner = cards.iterator();
			while (inner.hasNext()) {
				g.drawImage(inner.next().getImage(), x, y, 100, 100, null);
				y += 25;
			}
			x = 1340;
			y = 175 + (300 * i);

		}
	}

	public void drawToolBar(Graphics g) {
		g.drawImage(toolbar, 5, 55, 1063, 72, null);
		if (game.getCurrentPlayer().toString().equals("Player 1")) {
			g.drawImage(browntoolbar, 5, 55, 1063, 72, null);
		}

		g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 50));
		g.setColor(new Color(237, 236, 235));
		g.drawString(game.getCurrentPlayer().toString(), 30, 50);

		Iterator<Player> it = game.getAllPlayers().iterator();
		TreeSet<Player> temp = new TreeSet<>();
		while (it.hasNext()) {
			temp.add(it.next());
		}
		temp.remove(game.getCurrentPlayer());

		g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 36));
		it = temp.iterator();
		int y = 120;
		for (int i = 0; i < temp.size(); i++) {
			Player other = it.next();
			g.drawString(other.toString(), 1340, y);
			if (other.toString().equals("Player 1")) {
				g.drawImage(browntoolbar, 1340, y, 570, 60, null);
			} else {
				g.drawImage(toolbar, 1340, y, 570, 60, null);
			}
			y = y + 300;
		}
	}

	public void drawArchive(Graphics g) {
		g.setColor(new Color(237, 236, 235));
		g.fillRect(1075, 40, 260, 500);
		g.setColor(Color.black);
		g.drawRect(1075, 40, 260, 500);

		g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
		g.drawString("Archive", 1115, 80);
		g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		g.drawString("Victory Points Collected:", 1115, 500);
		g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
		g.drawString(game.getCurrentPlayer().getVictoryPoints() + "", 1200, 520);

		// currentPlayer's archived cards
		Player current = game.getCurrentPlayer();
		Card[] archive = current.getArchive();
		int x = 1090;
		int y = 90;
		for (int i = 0; i < archive.length; i++) {
			if (archive[i] != null) {
				g.drawImage(archive[i].getImage(), x, y, 120, 120, null);
				y += 120;
			}
			if (i % 3 == 0 && i != 0) {
				y = 90;
				x = 1210;
			}
		}
	}

	public void drawScoreBoard(Graphics g) {
		g.setColor(new Color(237, 236, 235));
		g.fillRect(1075, 800, 260, 190);
		g.drawRect(1075, 800, 260, 190);
		g.setColor(Color.BLACK);
		g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
		g.drawString("SCORES: ", 1080, 820);
		TreeSet<Player> set = game.getAllPlayers();
		int x = 1080;
		int y = 840;
		Iterator<Player> it = set.iterator();
		while (it.hasNext()) {
			Player temp = it.next();
			g.drawString(
					temp.toString() + ": " + temp.getCardCount() + " cards, " + temp.getVictoryPoints() + " points", x,
					y);
			g.drawString(temp.getMarbleCounts(), x, y + 15);
			y += 40;
		}

	}

	public void drawDispenser(Graphics g) {
		// dispenser image
		g.drawImage(dispenser, -30, 525, 400, 550, null);
		// pickable marbles
		Marble[] pickables = Game.getDispenser().getPickables();
		g.setColor(new Color(237, 236, 235));
		g.fillRect(325, 600, 60, 360);
		g.setColor(Color.black);
		g.drawRect(325, 600, 60, 360);
		g.drawLine(325, 660, 385, 660);
		g.drawLine(325, 720, 385, 720);
		g.drawLine(325, 780, 385, 780);
		g.drawLine(325, 840, 385, 840);
		g.drawLine(325, 900, 385, 900);
		int x = 325;
		int y = 600;
		for (int i = 0; i < 6; i++) {
			if (pickables[i] != null) {
				g.drawImage(pickables[i].getImage(), x, y, 60, 60, null);
			}
			y += 60;
		}
	}

	public void drawPlayables(Graphics g) {
		TreeMap<Integer, ArrayList<Card>> map = game.getPlayables();
		g.drawImage(tier1, 400, 600, 120, 120, null);
		ArrayList<Card> temp = map.get(1);
		int x = 520;
		int y = 600;

		for (int i = 0; i < temp.size(); i++) {
			g.drawImage(temp.get(i).getImage(), x, y, 120, 120, null);
			x += 120;
		}
		g.drawImage(tier2, 400, 720, 120, 120, null);
		temp = map.get(2);
		x = 520;
		y = 720;
		for (int i = 0; i < temp.size(); i++) {
			g.drawImage(temp.get(i).getImage(), x, y, 120, 120, null);
			x += 120;
		}
		g.drawImage(tier3, 400, 840, 120, 120, null);
		temp = map.get(3);
		x = 520;
		y = 840;
		for (int i = 0; i < temp.size(); i++) {
			g.drawImage(temp.get(i).getImage(), x, y, 120, 120, null);
			x += 120;
		}

	}

	public void drawEnergyGrid(Graphics g) {
		g.drawImage(ring, 1075, 540, 260, 260, null);
		Player current = game.getCurrentPlayer();
		int x = 1110;
		int y = 630;
		for (int i = 0; i < current.getMarbles().length; i++) {
			if (current.getMarbles()[i] != null) {
				g.drawImage(current.getMarbles()[i].getImage(), x, y, 50, 50, null);
				x += 50;
			}
			if (i % 3 == 0 && i != 0) {
				y += 50;
				x = 1110;
			}
		}
	}

	public void displayRules(Graphics g) {

		g.setColor(new Color(196, 165, 134));
		g.drawRect(60, 85, 1800, 910);
		g.fillRect(60, 85, 1800, 910);
		if (!switchPage) {
			g.drawImage(rule1, 279, 105, 681, 870, null);
			g.drawImage(rule2, 960, 105, 681, 870, null);
		} else {
			g.drawImage(rule3, 279, 105, 681, 870, null);
			g.drawImage(rule4, 960, 105, 681, 870, null);
		}

		// arrows
		g.setColor(Color.black);
		g.drawRect(1705, 520, 80, 100);
		g.drawRect(135, 520, 80, 100);
		g.drawImage(right, 1705, 520, 80, 100, null);
		g.drawImage(left, 135, 520, 80, 100, null);

	}

	public void drawAction(Graphics g) {
		if (actionNumber == 0) {
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 50));
			g.setColor(Color.red);
			g.drawString("CHOOSE BETWEEN ACTIONS", 400, 50);

			if (game.getCurrentPlayer().getArchive()[game.getCurrentPlayer().getArchive().length - 1] == null
					&& game.getCurrentPlayer().canFile()) {
				g.drawRect(380, 60, 200, 60);

			}
			if (game.getCurrentPlayer().getMarbles()[game.getCurrentPlayer().getMarbles().length - 1] == null) {
				g.drawRect(580, 60, 180, 60);
			}
			if (game.getCurrentPlayer().getMarbles()[0] != null) {
				g.drawRect(760, 60, 180, 60);
			}
			if (game.getCurrentPlayer().canResearch()) {
				g.drawRect(940, 60, 125, 60);
				g.setColor(Color.BLACK);
			}
		} else {
			g.setColor(new Color(217, 171, 63));// brown
			g.fillRect(80, 120, 1180, 340);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 50));
			g.setColor(Color.white);
			g.drawString("SWITCH TO NEXT PLAYER'S TURN", 200, 325);
			g.setColor(Color.black);
		}
	}

	public void drawChoice(Graphics g) {

		if (game.getCurrentPlayer().isApplyingEffects()) {
			effectsPopUp(g);
		} else if (game.getCurrentPlayer().getConvertPopup()) {
			drawConverterPopup(g);
		} else if (game.getCurrentPlayer().getPickingPopup()) {
			int[] xPoints = { 580, 760, 910, 1260, 1260, 80, 80, 330 };
			int[] yPoints = { 120, 120, 180, 180, 460, 460, 180, 180 };
			g.setColor(new Color(217, 171, 63));// brown
			g.fillPolygon(xPoints, yPoints, 8);
			g.setColor(Color.black);
			g.drawPolygon(xPoints, yPoints, 8);
			g.setColor(Color.white);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 45));
			g.drawString("Pick from the 6 marbles on the grid below.", 125, 325);
			g.setColor(Color.black);
		} else if (game.getCurrentPlayer().getFilingPopup()) {
			int[] xPoints = { 380, 580, 910, 1260, 1260, 80, 80, 330 };
			int[] yPoints = { 120, 120, 180, 180, 460, 460, 180, 180 };
			g.setColor(new Color(217, 171, 63));// brown
			g.fillPolygon(xPoints, yPoints, 8);
			g.setColor(Color.black);
			g.drawPolygon(xPoints, yPoints, 8);
			g.setColor(Color.white);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 45));
			g.drawString("Choose a card from the decks to archive.", 100, 325);
			g.setColor(Color.black);
		} else if (game.getCurrentPlayer().getBuildingPopup()) {
			int[] xPoints = { 760, 940, 910, 1075, 1075, 80, 80, 330 };
			int[] yPoints = { 120, 120, 180, 180, 460, 460, 180, 180 };
			g.setColor(new Color(217, 171, 63));// brown
			g.fillPolygon(xPoints, yPoints, 8);
			g.setColor(Color.black);
			g.drawPolygon(xPoints, yPoints, 8);
			g.setColor(Color.white);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 45));
			g.drawString("Choose and Build a card", 125, 280);
			g.drawString("(From your archive or the decks)", 100, 325);
			if (game.getCurrentPlayer().getInventory().get("convert").size() > 0) {
				g.drawRect(900, 190, 100, 50);
				g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 25));
				g.drawString("convert", 900, 220);
			}

		} else if (game.getCurrentPlayer().getResearchPopup()) {
			int[] xPoints = { 940, 1065, 910, 1260, 1260, 80, 80, 330 };
			int[] yPoints = { 120, 120, 180, 180, 460, 460, 180, 180 };
			g.setColor(new Color(217, 171, 63));// brown
			g.fillPolygon(xPoints, yPoints, 8);
			g.setColor(Color.black);
			g.drawPolygon(xPoints, yPoints, 8);
			g.setColor(Color.white);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 45));
			g.drawString("Choose a tier to research", 325, 280);
			g.setColor(Color.black);
			g.drawRect(200, 350, 200, 100);
			g.drawString("TIER 1", 225, 425);
			g.drawRect(550, 350, 200, 100);
			g.drawString("TIER 2", 575, 425);
			g.drawRect(900, 350, 200, 100);
			g.drawString("TIER 3", 925, 425);
		} else if (game.getCurrentPlayer().getResearchingCards() != null
				&& game.getCurrentPlayer().getResearchingCards().size() > 1) {
			int[] xPoints = { 940, 1065, 910, 1260, 1260, 80, 80, 330 };
			int[] yPoints = { 120, 120, 180, 180, 460, 460, 180, 180 };
			g.setColor(new Color(217, 171, 63));// brown
			g.fillPolygon(xPoints, yPoints, 8);
			g.setColor(Color.black);
			g.drawPolygon(xPoints, yPoints, 8);
			g.setColor(Color.white);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 45));
			g.drawString("Choose a card to file or build", 325, 280);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
			g.drawString("click outside of the cards to cancel research action", 500, 300);
			g.setColor(Color.black);
			ArrayList<Card> options = game.getCurrentPlayer().getResearchingCards();
			System.out.println(options.size());
			int x = 150;
			int y = 300;
			for (int i = 0; i < options.size(); i++) {
				g.drawImage(options.get(i).getImage(), x, y, 120, 120, null);
				x += 200;
			}
		} else if (game.getCurrentPlayer().getResearchingCards() != null
				&& game.getCurrentPlayer().getResearchingCards().size() == 1) {
			int[] xPoints = { 940, 1065, 910, 1260, 1260, 80, 80, 330 };
			int[] yPoints = { 120, 120, 180, 180, 460, 460, 180, 180 };
			g.setColor(new Color(217, 171, 63));// brown
			g.fillPolygon(xPoints, yPoints, 8);
			g.setColor(Color.black);
			g.drawPolygon(xPoints, yPoints, 8);
			g.setColor(Color.white);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 45));
			g.drawString("File or Build the card?", 400, 280);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
			g.drawString("click outside of the cards to cancel file/build action", 500, 300);
			g.setColor(Color.black);
			g.drawImage(game.getCurrentPlayer().getResearchingCards().get(0).getImage(), 280, 320, 120, 120, null);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 45));
			g.drawRect(500, 340, 120, 60);
			g.drawString("FILE", 505, 390);
			g.drawRect(700, 340, 140, 60);
			g.drawString("BUILD", 700, 390);
			g.drawRect(900, 340, 120, 60);
			g.drawString("CONV", 905, 390);
		}

	}

	public void effectsPopUp(Graphics g) {
		int x = 0;
		int y = 0;
		int temp = 0;
		Player current = game.getCurrentPlayer();
		TreeSet<Card> options = effectCards;

		if (current.getEffectsTrigger().equals("build")) {
			x = 760;
			temp = 940;
		}
		if (current.getEffectsTrigger().equals("file")) {
			x = 380;
			temp = 580;
		}
		if (current.getEffectsTrigger().equals("pick")) {
			x = 580;
			temp = 760;
		}
		int[] xPoints = { x, temp, 910, 1260, 1260, 80, 80, 330 };
		int[] yPoints = { 120, 120, 180, 180, 460, 460, 180, 180 };
		g.setColor(new Color(217, 171, 63));// brown
		g.fillPolygon(xPoints, yPoints, 8);
		g.setColor(Color.black);
		g.drawPolygon(xPoints, yPoints, 8);
		g.setColor(Color.white);
		g.drawString("Pick Cards to Apply Effects", 400, 280);
		g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		g.drawString("click outside of the cards to cancel effect action", 500, 300);
		x = 150;
		y = 300;

		Iterator<Card> it = options.iterator();
		while (it.hasNext()) {
			Card c = it.next();
			g.drawImage(c.getImage(), x, y, 120, 120, null);
			x += 200;

		}
		// NOTE: choice now becomes false AFTER effects are done
		// NOTE 2: use iterator to remove the options card from the total set?
	}

	public void nextPlayer(Graphics g) {

	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		System.out.println(x + ", " + y);
//displaying rules
		if (x >= 1560 && x <= 1696 && y >= 30 && y <= 110) {
			rulesDisplaying = !rulesDisplaying;
			System.out.println("DISPLAYING RULES");
			repaint();
		}
		if (rulesDisplaying) {
			if ((x >= 1705 && x <= 1785 && y >= 520 && y <= 620) || (x >= 135 && x <= 215 && y >= 520 && y <= 620)) {
				switchPage = !switchPage;
				repaint();
			}
		}
//switching to next player
		if (x >= 1726 && x <= 1862 && y >= 30 && y <= 80 && !choice && !game.checkIfOver()) {
			System.out.println("SWITCHING TO NEXT PLAYER'S TURN");
			game.getCurrentPlayer().setMaxPick(1);
			game.setCurrentPlayer();
			actionNumber = 0;
			choice = false;
			pickingNumber = 0;
			game.getCurrentPlayer().pick(false);
			game.getCurrentPlayer().build(false);
			game.getCurrentPlayer().setApplyingEffects(false);
			game.getCurrentPlayer().convert(false);
			game.getCurrentPlayer().file(false);
			game.getCurrentPlayer().research(false);
			effectCards.clear();
			repaint();
		} else if (x >= 1726 && x <= 1862 && y >= 30 && y <= 80 && !choice && game.checkIfOver()) {
			frame.switchToEnd(); // if the game is over, switch to leaderboards
		}
// PICKING A CHOICE
		if (!choice && x >= 380 && x <= 1065 && y >= 60 && y <= 120 && actionNumber == 0) {
			choice = true;
			if (x >= 380 && x <= 580 && y >= 60 && y <= 120
					&& game.getCurrentPlayer().getArchive()[game.getCurrentPlayer().getArchive().length - 1] == null) {
				game.getCurrentPlayer().file();
			} else if (x >= 580 && x <= 760 && y >= 60 && y <= 120
					&& game.getCurrentPlayer().getMarbles()[game.getCurrentPlayer().getMarbles().length - 1] == null) {
				game.getCurrentPlayer().pick();
			} else if (x >= 760 && x <= 940 && y >= 60 && y <= 120 && game.getCurrentPlayer().getMarbles()[0] != null) {
				game.getCurrentPlayer().build();
			} else if (x >= 940 && x <= 1065 && y >= 60 && y <= 120) {
				game.getCurrentPlayer().research();
			} else {
				choice = false;
				repaint();
			}
			System.out.println("PLAYER PICKED A CHOICE");
			actionNumber++;
			repaint();
		}
// PICKING
		else if (game.getCurrentPlayer().getPickingPopup() && choice && x >= 325 && x <= 385 && y >= 600 && y <= 960
				&& pickingNumber < game.getCurrentPlayer().getMaxPick()) {
			if (x >= 325 && x <= 385 && y >= 600 && y <= 660) {
				game.getCurrentPlayer().pick(Game.getDispenser().pick(0));
			} else if (x >= 325 && x <= 385 && y >= 660 && y <= 720) {
				game.getCurrentPlayer().pick(Game.getDispenser().pick(1));
			} else if (x >= 325 && x <= 385 && y >= 720 && y <= 780) {
				game.getCurrentPlayer().pick(Game.getDispenser().pick(2));
			} else if (x >= 325 && x <= 385 && y >= 780 && y <= 840) {
				game.getCurrentPlayer().pick(Game.getDispenser().pick(3));
			} else if (x >= 325 && x <= 385 && y >= 840 && y <= 900) {
				game.getCurrentPlayer().pick(Game.getDispenser().pick(4));
			} else if (x >= 325 && x <= 385 && y >= 900 && y <= 960) {
				game.getCurrentPlayer().pick(Game.getDispenser().pick(5));
			}
			System.out.println("PLAYER PICKED A MARBLE");
			game.getCurrentPlayer().setApplyingEffects();
			game.getCurrentPlayer().setEffectsTrigger("pick");
			createEffectCards("pick");
			pickingNumber++;
			if (pickingNumber >= game.getCurrentPlayer().getMaxPick()) {
				game.getCurrentPlayer().pick(false);
				if (effectCards.size() > 0) {
					game.getCurrentPlayer().setApplyingEffects(true);
				}
			}

			repaint();
		} else if (game.getCurrentPlayer().getFilingPopup() && choice
				&& ((x >= 520 && x <= 1000 && y >= 600 && y <= 720) || (x >= 520 && x <= 880 && y >= 720 && y <= 840)
						|| (x >= 520 && x <= 760 && y >= 840 && y <= 960))) {
// FILING
			// 1st row
			if (x >= 520 && x <= 640 && y >= 600 && y <= 720) {
				game.getCurrentPlayer().archive(game.getPlayables().get(1).remove(0));
			} else if (x >= 640 && x <= 760 && y >= 600 && y <= 720) {
				game.getCurrentPlayer().archive(game.getPlayables().get(1).remove(1));
			} else if (x >= 760 && x <= 880 && y >= 600 && y <= 720) {
				game.getCurrentPlayer().archive(game.getPlayables().get(1).remove(2));
			} else if (x >= 880 && x <= 1000 && y >= 600 && y <= 720) {
				game.getCurrentPlayer().archive(game.getPlayables().get(1).remove(3));
			}
			// 2nd row
			else if (x >= 520 && x <= 640 && y >= 720 && y <= 840) {
				game.getCurrentPlayer().archive(game.getPlayables().get(2).remove(0));
			} else if (x >= 640 && x <= 760 && y >= 720 && y <= 840) {
				game.getCurrentPlayer().archive(game.getPlayables().get(2).remove(1));
			} else if (x >= 760 && x <= 880 && y >= 720 && y <= 840) {
				game.getCurrentPlayer().archive(game.getPlayables().get(2).remove(2));
			}
			// 3rd row
			else if (x >= 520 && x <= 640 && y >= 840 && y <= 960) {
				game.getCurrentPlayer().archive(game.getPlayables().get(3).remove(0));
			} else if (x >= 640 && x <= 760 && y >= 840 && y <= 960) {
				game.getCurrentPlayer().archive(game.getPlayables().get(3).remove(1));
			}
			System.out.println("PLAYER FILED A CARD");
			game.makePlayables();
			game.getCurrentPlayer().file();
			game.getCurrentPlayer().setApplyingEffects();
			game.getCurrentPlayer().setEffectsTrigger("file");
			createEffectCards("file");
			repaint();
//build
		} else if (game.getCurrentPlayer().getBuildingPopup() && choice
				&& ((x >= 520 && x <= 1000 && y >= 600 && y <= 720) || (x >= 520 && x <= 880 && y >= 720 && y <= 840)
						|| (x >= 520 && x <= 760 && y >= 840 && y <= 960)
						|| (x >= 1090 && x <= 1330 && y >= 90 && y <= 450))) {
// BUILDING
			// 1st row
			if (x >= 520 && x <= 640 && y >= 600 && y <= 720) {
				if (game.getCurrentPlayer().build(game.getPlayables().get(1).get(0))) {
					currentBuildCard = game.getPlayables().get(1).get(0);
					game.getPlayables().get(1).remove(0);
					game.makePlayables();
				}
			} else if (x >= 640 && x <= 760 && y >= 600 && y <= 720) {
				if (game.getCurrentPlayer().build(game.getPlayables().get(1).get(1))) {
					currentBuildCard = game.getPlayables().get(1).get(1);
					game.getPlayables().get(1).remove(1);
					game.makePlayables();
				}
			} else if (x >= 760 && x <= 880 && y >= 600 && y <= 720) {
				if (game.getCurrentPlayer().build(game.getPlayables().get(1).get(2))) {
					currentBuildCard = game.getPlayables().get(1).get(2);
					game.getPlayables().get(1).remove(2);
					game.makePlayables();
				}
			} else if (x >= 880 && x <= 1000 && y >= 600 && y <= 720) {
				if (game.getCurrentPlayer().build(game.getPlayables().get(1).get(3))) {
					currentBuildCard = game.getPlayables().get(1).get(3);
					game.getPlayables().get(1).remove(3);
					game.makePlayables();
				}
			}
			// 2nd row
			else if (x >= 520 && x <= 640 && y >= 720 && y <= 840) {
				if (game.getCurrentPlayer().build(game.getPlayables().get(2).get(0))) {
					currentBuildCard = game.getPlayables().get(2).get(0);
					game.getPlayables().get(2).remove(0);
					game.makePlayables();
				}
			} else if (x >= 640 && x <= 760 && y >= 720 && y <= 840) {
				if (game.getCurrentPlayer().build(game.getPlayables().get(2).get(1))) {
					currentBuildCard = game.getPlayables().get(2).get(1);
					game.getPlayables().get(2).remove(1);
					game.makePlayables();
				}
			} else if (x >= 760 && x <= 880 && y >= 720 && y <= 840) {
				if (game.getCurrentPlayer().build(game.getPlayables().get(2).get(2))) {
					currentBuildCard = game.getPlayables().get(2).get(2);
					game.getPlayables().get(2).remove(2);
					game.makePlayables();
				}
			}
			// 3rd row
			else if (x >= 520 && x <= 640 && y >= 840 && y <= 960) {
				if (game.getCurrentPlayer().build(game.getPlayables().get(3).get(0))) {
					currentBuildCard = game.getPlayables().get(3).get(0);
					game.getPlayables().get(3).remove(0);
					game.makePlayables();
				}
				;
			} else if (x >= 640 && x <= 760 && y >= 840 && y <= 960) {
				if (game.getCurrentPlayer().build(game.getPlayables().get(3).get(1))) {
					currentBuildCard = game.getPlayables().get(3).get(1);
					game.getPlayables().get(3).remove(1);
					game.makePlayables();
				}
			}
			// archived cards
			else if (x >= 1090 && x <= 1210 && y >= 90 && y <= 210 && 0 < game.getCurrentPlayer().getArchive().length) {
				if (game.getCurrentPlayer().build(game.getCurrentPlayer().getArchive()[0])) {
					currentBuildCard = game.getCurrentPlayer().getArchive()[0];
					game.getCurrentPlayer().getArchive()[0] = null;
					game.getCurrentPlayer().reorderArchive(0);
				}
			} else if (x >= 1090 && x <= 1210 && y >= 210 && y <= 330
					&& 1 < game.getCurrentPlayer().getArchive().length) {
				if (game.getCurrentPlayer().build(game.getCurrentPlayer().getArchive()[1])) {
					currentBuildCard = game.getCurrentPlayer().getArchive()[1];
					game.getCurrentPlayer().getArchive()[1] = null;
					game.getCurrentPlayer().reorderArchive(1);
				}
			} else if (x >= 1090 && x <= 1210 && y >= 330 && y <= 450
					&& 2 < game.getCurrentPlayer().getArchive().length) {
				if (game.getCurrentPlayer().build(game.getCurrentPlayer().getArchive()[2])) {
					currentBuildCard = game.getCurrentPlayer().getArchive()[2];
					game.getCurrentPlayer().getArchive()[2] = null;
					game.getCurrentPlayer().reorderArchive(2);
				}
			} else if (x >= 1210 && x <= 1330 && y >= 90 && y <= 210
					&& 3 < game.getCurrentPlayer().getArchive().length) {
				if (game.getCurrentPlayer().build(game.getCurrentPlayer().getArchive()[3])) {
					currentBuildCard = game.getCurrentPlayer().getArchive()[3];
					game.getCurrentPlayer().getArchive()[3] = null;
					game.getCurrentPlayer().reorderArchive(3);
				}
			} else if (x >= 1210 && x <= 1330 && y >= 210 && y <= 330
					&& 4 < game.getCurrentPlayer().getArchive().length) {
				if (game.getCurrentPlayer().build(game.getCurrentPlayer().getArchive()[4])) {
					currentBuildCard = game.getCurrentPlayer().getArchive()[4];
					game.getCurrentPlayer().getArchive()[4] = null;
					game.getCurrentPlayer().reorderArchive(4);
				}
			} else if (x >= 1210 && x <= 1330 && y >= 330 && y <= 210
					&& 5 < game.getCurrentPlayer().getArchive().length) {
				if (game.getCurrentPlayer().build(game.getCurrentPlayer().getArchive()[5])) {
					currentBuildCard = game.getCurrentPlayer().getArchive()[5];
					game.getCurrentPlayer().getArchive()[5] = null;
					game.getCurrentPlayer().reorderArchive(5);
				}
			}
			game.getCurrentPlayer().build();
			game.getCurrentPlayer().setApplyingEffects(true);
			game.getCurrentPlayer().setEffectsTrigger("build");
			createEffectCards("build");
			repaint();
		} else if (game.getCurrentPlayer().getBuildingPopup() && (x >= 900 && x <= 1000 && y >= 190 && y <= 240
				&& game.getCurrentPlayer().getInventory().get("convert").size() > 0)) {
			Marble[] temp = game.getCurrentPlayer().getMarbles();
			for (int i = 0; i < temp.length; i++) {
				if (temp[i] != null) {
					temp[i].revert();
				}
			}
			game.getCurrentPlayer().convert(true);
			game.getCurrentPlayer().build(false);
			createEffectCards("convert");
			repaint();

		} else if (game.getCurrentPlayer().getResearchPopup()) {

// research popup #1
			if (x >= 200 && x <= 400 && y >= 350 & y <= 450) {
				game.getCurrentPlayer().setResearchingCards(game.getResearchCards(1));
			} else if (x >= 550 && x <= 750 && y >= 350 && y <= 450) {
				game.getCurrentPlayer().setResearchingCards(game.getResearchCards(2));
			} else if (x >= 900 && x <= 1100 && y >= 350 && y <= 450) {
				game.getCurrentPlayer().setResearchingCards(game.getResearchCards(3));
			}
			game.getCurrentPlayer().research();
			repaint();
		} else if (game.getCurrentPlayer().getResearchingCards() != null
				&& game.getCurrentPlayer().getResearchingCards().size() > 1 && x >= 80 && x <= 1260 && y >= 180
				&& y <= 460) {
			// research popup #2
			int temp = -1;
			if (x >= 150 && x <= 270 && y >= 300 && y <= 420) {
				temp = 0;
			} else if (x >= 350 && x <= 470 && y >= 300 && y <= 420) {
				temp = 1;
			}
			if (x >= 550 && x <= 670 && y >= 300 && y <= 420) {
				temp = 2;
			}
			if (x >= 750 && x <= 870 && y >= 300 && y <= 420) {
				temp = 3;
			}
			if (x >= 950 && x <= 1070 && y >= 300 && y <= 420) {
				temp = 4;
			}
			if (x >= 1150 && x <= 1270 && y >= 300 && y <= 420) {
				temp = 5;
			}
			ArrayList<Card> researchCards = game.getCurrentPlayer().getResearchingCards();
			int tier = researchCards.get(0).getLevel();
			for (int i = researchCards.size() - 1; i >= 0; i--) {
				if (temp != i) { // add back into each deck
					game.getDeck(tier).addCard(researchCards.remove(i));
				}
			}
			if (researchCards.size() == 0) {
				choice = false;
				game.getCurrentPlayer().setResearchingCards(null);
			}
			repaint();
		} else if (game.getCurrentPlayer().getResearchingCards() != null
				&& game.getCurrentPlayer().getResearchingCards().size() == 1 && x >= 80 && x <= 1260 && y >= 180
				&& y <= 460 && !game.getCurrentPlayer().getConvertPopup()) { // 3rd research popup

			if (x >= 500 && x <= 620 && y >= 340 && y <= 400) {
				if (game.getCurrentPlayer().getArchive()[game.getCurrentPlayer().getArchive().length - 1] == null) {
					game.getCurrentPlayer().archive(game.getCurrentPlayer().getResearchingCards().get(0));
					game.getCurrentPlayer().setApplyingEffects(true);
					game.getCurrentPlayer().setEffectsTrigger("file");
					createEffectCards("file");
					System.out.println("PLAYER ARCHIVED FROM RESEARCH");
				} else {
					ArrayList<Card> researchCards = game.getCurrentPlayer().getResearchingCards();
					int tier = researchCards.get(0).getLevel();
					game.getDeck(tier).addCard(researchCards.remove(0));
					System.out.println("archive failed from research");
					choice = false;
				}
			} else if (x >= 700 && x <= 840 && y >= 340 && y <= 400) {
				if (game.getCurrentPlayer().build(game.getCurrentPlayer().getResearchingCards().get(0))) {
					game.getCurrentPlayer().setApplyingEffects(true);
					game.getCurrentPlayer().setEffectsTrigger("build");
					createEffectCards("build");
					System.out.println("PLAYER BUILT FROM RESEARCH");

				} else {
					ArrayList<Card> researchCards = game.getCurrentPlayer().getResearchingCards();
					int tier = researchCards.get(0).getLevel();
					game.getDeck(tier).addCard(researchCards.remove(0));
					System.out.println("building failed from research");
					choice = false;
				}
			} else if (x >= 900 && x <= 1020 && y >= 340 && y <= 400) {
				game.getCurrentPlayer().convert(true);
				Marble[] temp = game.getCurrentPlayer().getMarbles();
				for (int i = 0; i < temp.length; i++) {
					if (temp[i] != null) {
						temp[i].revert();
					}
				}
				createEffectCards("convert");
				repaint();
				return;
			}

			game.getCurrentPlayer().getResearchingCards().remove(0);
			repaint();
		} else if (game.getCurrentPlayer().isApplyingEffects() && x >= 80 && x <= 1260 && y >= 180 && y <= 460) {
//effects
			int temp = 0;
			if (x >= 150 && x <= 270 && y >= 300 && y <= 420) {
				temp = 1;
			} else if (x >= 350 && x <= 470 && y >= 300 && y <= 420) {
				temp = 2;
			} else if (x >= 550 && x <= 670 && y >= 300 && y <= 420) {
				temp = 3;
			} else if (x >= 750 && x <= 870 && y >= 300 && y <= 420) {
				temp = 4;
			} else if (x >= 950 && x <= 1070 && y >= 300 && y <= 420) {
				temp = 5;
			} else if (x >= 1150 && x <= 1270 && y >= 300 && y <= 420) {
				temp = 6;
			}
			Player current = game.getCurrentPlayer();
			if (temp > effectCards.size() || temp == 0) {
				current.setApplyingEffects();
				effectCards.clear();
				choice = false;
				repaint();
				return;
			}
			Iterator<Card> it = effectCards.iterator();
			Card c = it.next();
			for (int i = 1; i < temp; i++) {
				c = it.next();
			}

			c.effect(current);

			System.out.println(c + " " + c.getEffectNum() + c.getTrigger());
			it.remove();
			if (effectCards.size() == 0) {
				current.setApplyingEffects(false);
				effectCards.clear();
				choice = false;
				if (current.getPickingPopup() || current.getBuildingPopup() || current.getFilingPopup()) {
					choice = true;
				}
			}
			repaint();
		}
//converter mouseclickers

		else if (game.getCurrentPlayer().getConvertPopup() && x >= 80 && x <= 1260 && y >= 120 && y <= 460) {
			Player current = game.getCurrentPlayer();
			if (!choosingColor) {
				int temp = 0;
				if (x >= 150 && x <= 270 && y >= 260 && y <= 380) {
					temp = 1;
				} else if (x >= 350 && x <= 470 && y >= 260 && y <= 380) {
					temp = 2;
				} else if (x >= 550 && x <= 670 && y >= 260 && y <= 380) {
					temp = 3;
				} else if (x >= 750 && x <= 870 && y >= 260 && y <= 380) {
					temp = 4;
				} else if (x >= 950 && x <= 1070 && y >= 260 && y <= 380) {
					temp = 5;
				} else if (x >= 1150 && x <= 1270 && y >= 260 && y <= 380) {
					temp = 6;
				}

				if (temp > effectCards.size() || temp == 0) {
					current.convert(false);
					effectCards.clear();
					if (game.getCurrentPlayer().getResearchingCards().size() != 1) {
						current.build(true);
					}
					repaint();
					return;
				}
				Iterator<Card> it = effectCards.iterator();
				ConverterCard c = (ConverterCard) it.next();
				for (int i = 1; i < temp; i++) {
					c = (ConverterCard) it.next();
				}
				System.out.println("picked card: " + c);
				if (c.getEffectNum() == 1 || c.getEffectNum() == 2) {
					choosingColor = true;
					convertingCard = c;

				} else if (c.getEffectNum() == 5) {
					choosingOriginalColor = true;
					convertingCard = c;
				} else {

					c.effect(current);
					System.out.println(c + " " + c.getEffectNum() + c.getTrigger());
					it.remove();
				}

			} else if (choosingOriginalColor) {
				if (x >= 350 && x <= 550 && y >= 300 && y <= 360) {
					originalColor = "blue";
				} else if (x >= 550 && x <= 750 && y >= 300 && y <= 360) {
					originalColor = "red";
				} else if (x >= 350 && x <= 550 && y >= 370 && y <= 430) {
					originalColor = "black";
				} else if (x >= 550 && x <= 750 && y >= 370 && y <= 430) {
					originalColor = "yellow";
				} else {
					game.getCurrentPlayer().convert(false);

				}
				choosingOriginalColor = false;
				choosingColor = true;
				repaint();

			} else if (choosingColor) {
				if (convertingCard.getEffectNum() != 5) {
					if (x >= 350 && x <= 550 && y >= 300 && y <= 360) {
						convertingCard.effect(current, "blue");
					}
					if (x >= 550 && x <= 750 && y >= 300 && y <= 360) {
						convertingCard.effect(current, "red");
					}
					if (x >= 350 && x <= 550 && y >= 370 && y <= 430) {
						convertingCard.effect(current, "black");
					}
					if (x >= 550 && x <= 750 && y >= 370 && y <= 430) {
						convertingCard.effect(current, "yellow");
					}
				} else if (convertingCard.getEffectNum() == 5) {
					if (x >= 350 && x <= 550 && y >= 300 && y <= 360) {
						convertingCard.effect(current, originalColor, "blue");
					}
					if (x >= 550 && x <= 750 && y >= 300 && y <= 360) {
						convertingCard.effect(current, originalColor, "red");
					}
					if (x >= 350 && x <= 550 && y >= 370 && y <= 430) {
						convertingCard.effect(current, originalColor, "black");
					}
					if (x >= 550 && x <= 750 && y >= 370 && y <= 430) {
						convertingCard.effect(current, originalColor, "yellow");
					}
				}
				effectCards.remove(convertingCard);
				choosingColor = false;

			}

			if (effectCards.size() == 0) {
				current.convert(false);
				if (game.getCurrentPlayer().getResearchingCards().size() != 1) {
					current.build(true);
				}
				effectCards.clear();
			}
			repaint();

		}

		else {
			choice = false;
			repaint();
		}
	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void createEffectCards(String type) {
		effectCards.clear();
		TreeSet<Card> temp = game.getCurrentPlayer().getInventory().get(type);
		Iterator<Card> it = temp.iterator();
		while (it.hasNext()) {
			effectCards.add(it.next());
		}
		if (effectCards.size() == 0) {
			game.getCurrentPlayer().setApplyingEffects(false);
			choice = false;
		}
		System.out.println("Effect Cards: " + effectCards);
	}

	public static Card getCurrentBuildCard() {
		return currentBuildCard;
	}

	public void setCurrentBuildCard(Card c) {
		currentBuildCard = c;
	}

	public static void setChoice(boolean c) {
		choice = c;
	}

	public Game getGame() {
		return game;
	}

	public void drawConverterPopup(Graphics g) {
		int[] xPoints = { 200, 370, 480, 1260, 1260, 80, 80, 80 };
		int[] yPoints = { 120, 120, 180, 180, 460, 460, 180, 180 };
		g.setColor(new Color(217, 171, 63));// brown
		g.fillPolygon(xPoints, yPoints, 8);
		g.setColor(Color.black);
		g.drawPolygon(xPoints, yPoints, 8);

		if (!choosingColor) {
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 25));
			g.setColor(Color.white);
			g.drawString("Pick Cards to Apply Effects", 400, 220);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
			g.drawString("click outside of the cards to cancel effects action", 500, 240);

			int x = 150;
			int y = 260;
			Iterator<Card> iter = effectCards.iterator();
			while (iter.hasNext()) {
				g.drawImage(iter.next().getImage(), x, y, 120, 120, null);
				x += 200;
			}
			Marble[] tempM = game.getCurrentPlayer().getMarbles();
			x = 100;
			y = 380;
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 25));
			for (int i = 0; i < tempM.length; i++) {
				if (tempM[i] != null) {
					g.drawImage(tempM[i].getImage(), x, y, 50, 50, null);
					if (tempM[i].getValue() > 1) {
						g.drawString(tempM[i].getValue() + "", x, y + 25);
					}
					if (!tempM[i].getColor().equals(tempM[i].getOGColor())) {
						g.drawString(tempM[i].getColor(), x, y + 50);
					}
				}
				x += 60;
			}
		}
		if (choosingColor) {
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
			g.drawString("Pick color to convert to", 400, 220);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
			g.drawString("click outside of the cards to cancel effects action", 500, 240);
			g.setColor(Color.blue);
			g.fillRect(350, 300, 200, 60);
			g.setColor(Color.red);
			g.fillRect(550, 300, 200, 60);
			g.setColor(Color.black);
			g.fillRect(350, 370, 200, 60);
			g.setColor(Color.yellow);
			g.fillRect(550, 370, 200, 60);
		}

		g.setColor(Color.black);
		if (choosingOriginalColor) {
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
			g.drawString("Pick color to convert from", 400, 220);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
			g.drawString("click outside of the cards to cancel effects action", 500, 240);
			g.setColor(Color.blue);
			g.fillRect(350, 300, 200, 60);
			g.setColor(Color.red);
			g.fillRect(550, 300, 200, 60);
			g.setColor(Color.black);
			g.fillRect(350, 370, 200, 60);
			g.setColor(Color.yellow);
			g.fillRect(550, 370, 200, 60);
		}
	}

	public static int getPickingNumber() {
		return pickingNumber;
	}
}
