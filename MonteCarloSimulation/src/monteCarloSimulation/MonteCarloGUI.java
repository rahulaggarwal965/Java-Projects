package monteCarloSimulation;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import gameEngine.GameEngine;

public class MonteCarloGUI {

	public static Font titleFont = new Font("SansSerif", Font.BOLD, 100);
	public static Font labelFont = new Font("SansSerif", Font.ITALIC, 20);
	
	public void init() throws Exception {
		
	}
	
	public void render(int gameState, Graphics2D g2d) {
		if(gameState == 0) {
			drawCenteredString(g2d, "MonteCarloSimulation", titleFont, GameEngine.displayWidth/2, 80);
			drawCenteredString(g2d, "Rahul Aggarwal", labelFont, GameEngine.displayWidth/2, 150);
			drawCenteredString(g2d, "Press Enter or Click to Play", labelFont, GameEngine.displayWidth/2, 900);
		} else if(gameState == 1) {
			drawCenteredString(g2d, "Press B to go Back and Press ESC to Pause", labelFont, 1000, 950);
		}
	}
	
	public static void drawCenteredString(Graphics2D g2d, String s, Font font, int x, int y) {
		FontMetrics m = g2d.getFontMetrics(font);
		int width = m.stringWidth(s);
		int height = m.getHeight();
		g2d.setFont(font);
		g2d.setColor(Color.white);
		g2d.drawString(s, x - width/2, y - height/2 + m.getAscent());
	}
	
	public static void drawCenteredString(Graphics2D g2d, String s, Font font, int x, int y, Color col) {
		FontMetrics m = g2d.getFontMetrics(font);
		int width = m.stringWidth(s);
		int height = m.getHeight();
		g2d.setFont(font);
		g2d.setColor(col);
		g2d.drawString(s, x - width/2, y - height/2 + m.getAscent());
	}
	

}
