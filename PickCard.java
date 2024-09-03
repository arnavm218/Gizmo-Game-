import java.awt.image.*;
import java.util.*;

public class PickCard extends Card implements Comparable<Card>
{
	private String pickColor1;
	private String pickColor2;
	private int effectType;
	
	public PickCard(int level, String color, BufferedImage image, int point, String pickColor1, int effectType) 
	{
		super(level, color, image, point, "pick");
		this.pickColor1 = pickColor1;
		this.effectType = effectType;
		this.pickColor2 = "";
	}
	
	public PickCard(int level, String color, BufferedImage image, int point, String pickColor1, String pickColor2, int effectType) 
	{
		super(level, color, image, point, "pick");
		this.pickColor1 = pickColor1;
		this.pickColor2 = pickColor2;
		this.effectType = effectType;
	}
	
	public void effect(Player p) //Conditional to check if the marble chosen can be triggered
	{
		if(effectType == 1)
		{	
			System.out.println("Check");
			Marble[] temp = p.getMarbles();
			Marble m = temp[temp.length - 1];
			for(int i = temp.length - 1; i >= 0; i--) {
				if (temp[i] != null) {
					m = temp[i];
				}
			}
			if (m == null) {
				System.out.println("no available marbles");
				return;
			}
			if(m.getColor().equals(pickColor1) || m.getColor().equals(pickColor2)){
				p.draw();
			}
			
		}
		
	}
	public int getEffectNum() {
		return effectType;
	}
	
}
