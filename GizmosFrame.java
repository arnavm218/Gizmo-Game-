import java.awt.*;
import javax.swing.*;
public class GizmosFrame extends JFrame{
	private static final int WIDTH = 1920;
	private static final int HEIGHT = 1080;
	
	private MainMenuPanel start;
	private GizmosPanel mainGame;
	private GameEndPanel endGame;
	
	
	public GizmosFrame(String framename) {
		super(framename);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH,HEIGHT);
		start = new MainMenuPanel(this);
		add(start);
		setVisible(true);
		
	}
	
	public void switchToGame() {
		setVisible(false);
		remove(start);
		mainGame = new GizmosPanel(start.getNumPlayers(), this);
		add(mainGame);
		setVisible(true);
	}
	
	public void switchToEnd() {
		setVisible(false);
		remove(mainGame);
		endGame = new GameEndPanel(this, mainGame.getGame());
		add(endGame);
		setVisible(true);
	}
	public void endToMain() {
		setVisible(false);
		remove(endGame);
		add(mainGame);
		setVisible(true);
	}
}


