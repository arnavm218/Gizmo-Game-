import java.awt.image.*;
import java.util.*;
public class FileCard extends Card implements Comparable<Card>
{
	private int effectType; //result
	
	public FileCard(int level, String color, BufferedImage image, int point, int effectType)
	{
		super(level, color, image, point, "file");
		this.effectType = effectType;
	}	
	
	public void effect(Player p)
	{
		if(effectType == 1)
		{
			p.pick();
			if (GizmosPanel.getPickingNumber() == p.getMaxPick()) {
				p.setMaxPick(p.getMaxPick()  + 1);
			}
			GizmosPanel.setChoice(true);
			System.out.println("CHECK - basic file effect");
		}
		if(effectType == 2)
		{
			p.setVictoryPoints(1);
		}
		if(effectType == 3)
		{
			p.draw();
			p.draw();
			p.draw();
		}
		if(effectType == 4)
		{
			//beginning card
			p.draw();
			System.out.println("CHECK - UPGRADE");
		}
	}
	public int getEffectNum() {
		return effectType;
	}
}
