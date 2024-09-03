import java.awt.image.*;
import java.util.*;

public class Card implements Comparable<Card>
{
	private int level;
	private String color;
	private BufferedImage image;
	private int point;
	private String triggerType;
	private int cost;
	
	
	public Card(int level, String color, BufferedImage image, int point, String triggerType)
	{
		this.level = level;
		this.color = color;
		this.image = image;
		cost = point;
		this.point = point;
		this.triggerType = triggerType;
	}
	public Card(int level, String color, BufferedImage image, int point, int cost, String triggerType)
	{
		this.level = level;
		this.color = color;
		this.image = image;
		this.cost = cost;
		this.point = point;
		this.triggerType = triggerType;
	}
	
	public BufferedImage getImage()
	{
		return image;
	}
	
	public void effect(Player player)
	{
		//over-ridden
	}
	
	public void effect(Player player, String color1, String color2) {
		//over-ridden in buildCard
	}
	
	public String toString()
	{
		return "(" + level + ", " + color + ", " + point + ")";
	}
	public String getTrigger() {
		return triggerType;
	}

	public int getEffectNum() {
		return 0;
	}
	public int compareTo(Card o) {
		int num = this.level - o.level;
		if (num == 0) {
			num = this.point - o.point;
			if (num == 0) {
				num = this.color.compareTo(o.color);
				if (num == 0) {
					num = this.getEffectNum() - o.getEffectNum();
				}
			}
		}
		return num;
	}
	public int getCost() {
		return cost;
	}
	public String getColor() {
		return color;
	}
	
	public int getLevel() {
		return level;
	}
	public int getPoints()
	{
		return point;
	}
}
