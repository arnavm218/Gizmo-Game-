import java.awt.image.*;
import java.util.*;

public class BuildCard extends Card implements Comparable<Card> {
	private String buildColor1;
	private String buildColor2;
	private int effectType;
	private boolean fromFile = false;
	private boolean isTier2 = false;

	public BuildCard(int level, String color, BufferedImage image, int point, String buildColor1, int effectType) {
		super(level, color, image, point, "build");
		this.buildColor1 = buildColor1;
		this.effectType = effectType;
	}

	public BuildCard(int level, String color, BufferedImage image, int point, String buildColor1, String buildColor2,
			int effectType) {
		super(level, color, image, point, "build");
		this.buildColor1 = buildColor1;
		this.buildColor2 = buildColor2;
		this.effectType = effectType;
	}

	public BuildCard(int level, String color, BufferedImage image, int point, boolean fromFile, int effectType) {
		super(level, color, image, point, "build");
		this.fromFile = fromFile;
		this.effectType = effectType;
	}

	public BuildCard(int level, String color, BufferedImage image, int point, int effectType, boolean isTier2) {
		super(level, color, image, point, "build");
		this.isTier2 = isTier2;
		this.effectType = effectType;
	}

	public void effect(Player p) {
		Card c = GizmosPanel.getCurrentBuildCard();
		if (c.getColor().equals(buildColor1) || c.getColor().equals(buildColor2) || c.getColor().equals("rainbow")) {
			if (effectType == 1) {
				if (GizmosPanel.getPickingNumber() == p.getMaxPick()) {
					p.setMaxPick(p.getMaxPick() + 1);
				}
				p.pick();
				GizmosPanel.setChoice(true);
			}
			if (effectType == 2) {
				if (GizmosPanel.getPickingNumber() == 0) {
					p.setMaxPick(p.getMaxPick() + 1);
				} else {
					p.setMaxPick(p.getMaxPick() + 2);
				}
				p.pick();
				GizmosPanel.setChoice(true);

				/*
				 * Notes: -switching player GUI called by actionNumber, actual switching called
				 * by choice == false need to make choice true
				 */

			}
			if (effectType == 3) {
				p.setVictoryPoints(1);
			}
			if (effectType == 4) {
				p.setVictoryPoints(2);
			}
			if (effectType == 5) {
				p.file();
			}
			if (effectType == 6) {
				p.build();
			}
			if (effectType == 7) {
				p.research();
			}
		}
	}

	public int getEffectNum() {
		return effectType;
	}
}
