import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

public class GameEndPanel extends JPanel implements MouseListener {
	private boolean changeScreen;
	private BufferedImage background, back;
	private Game game;
	private GizmosFrame frame;
	private ArrayList<String> allScores = new ArrayList<>();

	public GameEndPanel(GizmosFrame frame, Game game) {
		this.game = game;
		this.frame = frame;
		try {
			back = ImageIO.read(GameEndPanel.class.getResource("/images/endback.png"));
			background = ImageIO.read(GameEndPanel.class.getResource("/images/bavck.png"));
		} catch (Exception e) {
			System.out.println("error");
		}
		addMouseListener(this);
		changeScreen = false;
	}

	public void paint(Graphics g) {
		drawBackground(g);
		back(g);
		drawRankings(g);
		calculate(g);
		drawWinner(g);
	}

	public void drawBackground(Graphics g) {
		g.drawImage(background, 0, 0, 1920, 1080, null);
	}

	public void back(Graphics g) {
		g.drawImage(back, 40, 40, 175, 100, null);
		g.drawRect(40, 40, 175, 100);
	}

	public void drawRankings(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(300, 325, 1300, 575);
		g.setColor(Color.black);
		g.drawRect(300, 325, 1300, 100);
		g.drawRect(300, 425, 1300, 125);
		g.drawRect(300, 550, 1300, 125);
		g.drawRect(300, 675, 1300, 125);
		g.drawRect(300, 800, 1300, 100);

		g.drawLine(600, 325, 600, 900);
		g.drawLine(850, 325, 850, 900);
		g.drawLine(1100, 325, 1100, 900);
		g.drawLine(1350, 325, 1350, 900);
		g.drawLine(1600, 325, 1600, 900);

		g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
		g.drawString("Player 1", 625, 400);
		g.drawString("Player 2", 875, 400);
		g.drawString("Player 3", 1125, 400);
		g.drawString("Player 4", 1375, 400);

		g.drawString("Gizmos Card", 315, 475);
		g.drawString("Count", 390, 525);
		g.drawString("Victory", 360, 600);
		g.drawString("Points", 370, 650);
		g.drawString("Energy Left", 315, 745);
		g.drawString("WINNER: ", 380, 860);

	}

	public void calculate(Graphics g) {
		g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
		int x = 650;
		int y = 500;
		Iterator<Player> it = game.getAllPlayers().iterator();
		while (it.hasNext()) {
			Player p = it.next();
			p.updateVictoryPoints();
			g.drawString(p.getCardCount() + "", x, y);
			y += 125;
			g.drawString(p.getTotalPoints() + "", x, y);
			y+= 125;
			//algorithm for how many marbles a player has
			int temp = p.getMarbles().length;
			for (int i = 0; i < temp; i++) {
				if (p.getMarbles()[i] == null) {
					temp = i;
					break;
				}
			}
			//end of algorithm
			g.drawString(temp + "", x, y);
			x += 250;
			y = 500;
		}

	}
	
	public void drawWinner(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(600, 801, 1000, 98);
		g.setColor(Color.black);
		g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
		int temp = 0;
		Iterator<Player> it = game.getAllPlayers().iterator();
		while (it.hasNext()) {
			Player p = it.next();
			if (p.getTotalPoints() > temp) {
				temp = p.getTotalPoints();
			}
		}
		ArrayList<Player> winners = new ArrayList<>();
		it = game.getAllPlayers().iterator();
		while (it.hasNext()) {
			Player p = it.next();
			if (p.getTotalPoints() == temp) {
				winners.add(p);
			}
		}
		if (winners.size() == 1) {
			g.drawString(winners.get(0) + " WINS!", 800, 860);
			return;
		}
		//only for victory points ^^^
		
		//counting number of cards 
		temp = 0;
		for (int i = 0; i < winners.size(); i++) {
			if (winners.get(i).getCardCount() > temp) {
				temp = winners.get(i).getCardCount();
			}
		}
		
		for (int i = 0; i < winners.size(); i++) {
			if (winners.get(i).getCardCount() < temp) {
				winners.remove(i);
				i--;
			}
		}
		if (winners.size() == 1) {
			g.drawString(winners.get(0) + " WINS!", 800, 860);
			return;
		}
		
		//marble count
		temp = 0;
		for (int i = 0; i < winners.size(); i++) {
			int marb = winners.get(i).getMarbles().length;
			for (int j = 0; j < winners.get(i).getMarbles().length; j++) {
				if (winners.get(i).getMarbles()[j] == null) {
					marb = j;
					break;
				}
			}
			if (marb > temp) {
				marb = temp;
			}
		} //getting max marble count
		
		for (int i = 0; i < winners.size(); i++) {
			int marb = winners.get(i).getMarbles().length;
			for (int j = 0; j < winners.get(i).getMarbles().length; j++) {
				if (winners.get(i).getMarbles()[j] == null) {
					marb = j;
					break;
				}
			}
			if (marb < temp) {
				winners.remove(i);
				i--;
			}
		} //removing players that have less than the max
		
		if (winners.size() == 1) {
			g.drawString(winners.get(0) + " WINS!", 800, 860);
			return;
		}
		else {
			g.drawString(winners.get(winners.size() - 1) + " WINS!", 800, 860);
			return;
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

	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		System.out.println(x + "," + y);
		if (x >= 40 && x <= 205 && y >= 40 && y <= 140) {
			frame.endToMain();
		}
	}
}
