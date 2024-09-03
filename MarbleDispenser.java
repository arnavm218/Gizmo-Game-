import java.util.*;

public class MarbleDispenser {
	private ArrayList<Marble> drawables;
	private Marble[] pickables;
	public MarbleDispenser() {
		drawables = new ArrayList<>();
		pickables = new Marble[6];
		for(int i = 0; i < 8; i++) {
			drawables.add(new Marble("black"));
		}
		for(int i = 0; i < 8; i++) {
			drawables.add(new Marble("yellow"));
		}
		for(int i = 0; i < 8; i++) {
			drawables.add(new Marble("red"));
		}
		for(int i = 0; i < 8; i++) {
			drawables.add(new Marble("blue"));
		}
		shuffle();
		for(int i = 0; i < 6; i++) {
			pickables[i] = drawables.remove(0);
		}
		
		
	}
	public void shuffle()
	{
		for(int i = 0; i < drawables.size(); i++)
		{
			int k = (int)(Math.random() * drawables.size());
			Marble temp = drawables.get(k);
			drawables.set(k, drawables.get(i));
			drawables.set(i, temp);
		}
	}
	public Marble getRandom() {
		int rand = (int)(Math.random() * drawables.size());
		return drawables.remove(rand);
	}
	public Marble pick(int num) {
		Marble chosen = null;
		if (num >= 0 && num <=5) {
			chosen = pickables[num];
		}
		for (int i = num; i < pickables.length - 1; i++) {
			pickables[i] = pickables[i+1];
		}
		pickables[5] = drawables.remove((int)(Math.random() * drawables.size()));
		return chosen;
	}
	public void updatePicks() {
		//Method not needed, implemented in pick already
	}
	public void returnToDispenser(Marble m) {
		drawables.add((int)(Math.random() * drawables.size()), m);
	}
	public Marble[] getPickables() {
		return pickables;
	}
}
