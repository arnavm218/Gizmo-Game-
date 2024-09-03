import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;

public class Marble {
	private String color;
	private String ogColor;
	private int value;
	private BufferedImage image;
	private boolean convertedColor = false;
	private boolean convertedNum = false;

	public Marble(String color) {
		this.color = color;
		ogColor = color;
		value = 1;
		try{
		if (color.equals("black")) {
			image = ImageIO.read(Marble.class.getResource("/images/blackmarble.png"));
		}
		if (color.equals("yellow")) {
			image = ImageIO.read(Marble.class.getResource("/images/yellowmarble.png"));
		}
		if (color.equals("blue")) {
			image = ImageIO.read(Marble.class.getResource("/images/bluemarble.png"));
		}
		if (color.equals("red")) {
			image = ImageIO.read(Marble.class.getResource("/images/redmarble.png"));
		}
		}
		catch(Exception E) {
			System.out.println("Error - Marbles");
		}
	}

	public BufferedImage getImage() {
		return image;
	}
	public String getOGColor() {
		return ogColor;
	}
	public String getColor() {
		return color;
	}

	public void setValue(int i) {
		value = i;
	}

	public int getValue() {
		return value;
	}
	public String toString() {
		return color;
	}

	public void setColor(String newColor) {
		color = newColor;	
		setConvertedColor(true);
	}
	
	public void revert()
	{
		color = ogColor;
		value = 1;
		convertedColor = false; 
		setConvertedNum(false);
		System.out.println("revert marbles");
		
	}

	public boolean isConvertedColor() {
		return convertedColor;
	}

	public void setConvertedColor(boolean converted) {
		this.convertedColor = converted;
	}

	public boolean isConvertedNum() {
		return convertedNum;
	}

	public void setConvertedNum(boolean convertedNum) {
		this.convertedNum = convertedNum;
	}
	
}
