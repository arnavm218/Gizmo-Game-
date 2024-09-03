import java.awt.image.*;
import java.util.*;

public class ConverterCard  extends Card implements Comparable<Card>
{
	private String convertColor1;
	private String convertColor2;
	private int effectType;
	private int multiplier;
	
	public ConverterCard(int level, String color, BufferedImage image, int point, String convertColor1, int effectType, int multiplier) 
	{
		super(level, color, image, point, "convert");
		this.convertColor1 = convertColor1;
		this.effectType = effectType;
		this.multiplier = multiplier;
	}
	
	public ConverterCard(int level, String color, BufferedImage image, int point, String convertColor1, String convertColor2, int effectType, int multiplier) 
	{
		super(level, color, image, point, "convert");
		this.convertColor1 = convertColor1;
		this.convertColor2 = convertColor2;
		this.effectType = effectType;
		this.multiplier = multiplier;
	}
	
	public void effect(Player p)
	{
		if(effectType == 3)
		{
			p.multiplyConvert(convertColor1, multiplier);
		}
		if(effectType == 4)
		{
			p.multiplyConvert(convertColor1, convertColor2, multiplier);
			
		}
	}
	public void effect(Player p, String color1) {
		if(effectType == 1)
		{
			p.convert(convertColor1, color1, 1);
		}
		if(effectType == 2)
		{
			p.convert(convertColor1, color1, 2);
		}
	}
	public void effect(Player p, String color1, String color2) {
		if (effectType == 5) {
			p.convert(color1, color2, 1);
		}
	}
	public int getEffectNum() {
		return effectType;
	} 
	public String getConvertColor1()
	{
		return convertColor1;
	}
	public String getConvertColor2()
	{
		return convertColor2;
	}
}
