import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
public class MainMenuPanel extends JPanel implements MouseListener {
	private BufferedImage startScreen, choosePlayerScreen, rule1, rule2;
	
	private int numPlayers;
	
	private boolean changeScreen;
	private GizmosFrame frame;
	
	public MainMenuPanel(GizmosFrame frame) {
		try {
			startScreen = ImageIO.read(MainMenuPanel.class.getResource("/images/GizmosBackground.png"));
			choosePlayerScreen = ImageIO.read(MainMenuPanel.class.getResource("/images/ChoosePlayers.png"));
			rule1 = ImageIO.read(MainMenuPanel.class.getResource("/images/page3.png"));
			rule2 = ImageIO.read(MainMenuPanel.class.getResource("/images/page4.png"));
		}
		catch(Exception E) {
			System.out.println("ERROR");
		}
		addMouseListener(this);
		numPlayers = 1;
		
		changeScreen = false;
		this.frame = frame;
		
	}
	
	public void paint(Graphics g) {
		if(!changeScreen) {
		drawBackground(g);
		}
		else {
		drawNumberOfPlayers(g);
		}
	}
	
	
	
	public void drawBackground(Graphics g) {
		g.drawImage(startScreen,0,0, getWidth(), getHeight(), null);
		g.setColor(new Color(34, 42, 161));
		g.fillRect(800, 500, 300, 150);
		g.setFont(new Font("SansSerif", Font.ITALIC, 60));
		g.setColor(Color.WHITE);
		g.drawString("START", 850, 600);
	}
	public void drawNumberOfPlayers(Graphics g) {
		g.drawImage(choosePlayerScreen, 0, 0, getWidth(), getHeight(), null);
		g.setColor(Color.white);
		g.setFont(new Font("SansSerif", Font.ITALIC, 70));
		g.drawString("CHOOSE THE NUMBER OF PLAYERS",400, 300);
		g.setColor(new Color(237, 236, 235));
		
		g.fillRect(1300,500, 300, 150);
		g.fillRect(400,500, 300, 150);
		g.fillRect(850, 700, 300, 150);
		
		g.setColor(Color.black);
		g.drawString("2 Players", 400,600);
		g.drawString("3 Players", 1300,600);
		g.drawString("4 Players", 850,800);
		
	}
	public void displayRules(Graphics g) {
		
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
		System.out.println(x+","+y);
		
		if (x >=800 && x <= 1100 && y >= 500 && y <= 600 && !changeScreen) {
			changeScreen = true;
			repaint();
		}
		
		if (x >= 400 && x <= 700 && y >= 500 && y <= 650 && changeScreen) {
			numPlayers = 2;
			changePanel();
			
		}
		if (x >= 1300 && x <= 1600 && y >= 500 && y <=650 && changeScreen) {
			numPlayers = 3;
		
			changePanel();
		}
		if (x >= 850 && x <= 1150 && y >= 700 && y <= 850 && changeScreen) {
			numPlayers = 4;
			changePanel();
		}
		
	}
	
	public int getNumPlayers() {
		return numPlayers;
	}
	public void changePanel() {
		frame.switchToGame();
	}
}
