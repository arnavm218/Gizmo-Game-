import java.awt.image.*;
import java.util.*;

public class UpgradeCard extends Card implements Comparable<Card>
{
	private int addMarbleInv;
	private int addArchiveInv;
	private int addResearchInv;
	private int effectType;
	
	public UpgradeCard(int level, String color, BufferedImage image, int point, int effectType, int addMarbleInv, int addArchiveInv, int addResearchInv) 
	{
		super(level, color, image, point, "upgrade");
		this.effectType = effectType;
		this.addMarbleInv = addMarbleInv;
		this.addArchiveInv = addArchiveInv;
		this.addResearchInv = addResearchInv;
	}
	
	public UpgradeCard(int level, String color, BufferedImage image, int point, int cost, int effectType, int addMarbleInv, int addArchiveInv, int addResearchInv) 
	{
		super(level, color, image, point, cost, "upgrade");
		this.effectType = effectType;
		this.addMarbleInv = addMarbleInv;
		this.addArchiveInv = addArchiveInv;
		this.addResearchInv = addResearchInv;
	}
	
	public void effect(Player p)
	{
		if(effectType == 1)
		{
			p.upgrade(addMarbleInv, addArchiveInv, addResearchInv);
		}
		if(effectType == 2)
		{
			p.setLevel2Discount(1);
		}
		if(effectType == 3)
		{
			p.setCanFile(false);
		}
		if(effectType == 4)
		{
			p.setCanResearch(false);
		}
		if(effectType == 5)
		{
			p.setMarblesToPoints();
		}
		if(effectType == 6)
		{
			p.setDoublePhysicalVP();
		}
		if(effectType == 7)
		{
			p.setArchiveDiscount(1);
		}
		if(effectType == 8)
		{
			p.setResearchDiscount(1);
		}
	}
	public int getEffectNum() {
		return effectType;
	}
}
