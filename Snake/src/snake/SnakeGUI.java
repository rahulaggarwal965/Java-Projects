package snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import gameEngine.GameEngine;

public class SnakeGUI {

	public static Font titleFont = new Font("SansSerif", Font.BOLD, 100);
	public static Font labelFont = new Font("SansSerif", Font.ITALIC, 20);
	private int length;
	
	public int getLength() {
		return this.length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	public void init() throws Exception {
		
	}
	
	public void render(int gameState, Graphics2D g2d) {
		if(gameState == 0) {
			drawCenteredString(g2d, "Snake", titleFont, GameEngine.displayWidth/2, 80);
			drawCenteredString(g2d, "Rahul Aggarwal", labelFont, GameEngine.displayWidth/2, 150);
			drawCenteredString(g2d, "Press Enter or Click to Play", labelFont, GameEngine.displayWidth/2, 560);
			drawCenteredString(g2d, "Use Arrow Keys to Move The Snake", labelFont, GameEngine.displayWidth/2, 600);
		} else if(gameState == 1) { 
			drawCenteredString(g2d, "Length: " + String.valueOf(this.length), labelFont, 60, 20);
		} else if(gameState == 2) {
			drawCenteredString(g2d, "You Lose", titleFont, GameEngine.displayWidth/2, 80);
			drawCenteredString(g2d, "Your Score was " + String.valueOf(this.length), labelFont, GameEngine.displayWidth/2, 160);
			drawCenteredString(g2d, "Press Enter or Click to Play Again", labelFont, GameEngine.displayWidth/2, 680);
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
