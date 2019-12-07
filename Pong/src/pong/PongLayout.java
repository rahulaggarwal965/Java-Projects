package pong;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import components.CenteredString;
import gameEngine.GameEngine;

public class PongLayout {
	
	private CenteredString[] centeredStrings = new CenteredString[10];
	private Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
	
	void init(Graphics g) {
		centeredStrings[0] = new CenteredString(g, "PONG", 0, 0, GameEngine.displayWidth, GameEngine.displayHeight/3, new Font("SansSerif", Font.BOLD, 100));
		centeredStrings[1] = new CenteredString(g, "Rahul Aggarwal", 0, 200, GameEngine.displayWidth, 30, new Font("SansSerif", Font.PLAIN, 20));
		centeredStrings[2] = new CenteredString(g, "Single Player", 0, GameEngine.displayHeight/2, GameEngine.displayWidth/2, GameEngine.displayHeight*5/8, new Font("SansSerif", Font.ITALIC, 30));
		centeredStrings[3] = new CenteredString(g, "Play against an AI using WASD", 0, GameEngine.displayHeight/2 + 30, GameEngine.displayWidth/2, GameEngine.displayHeight*5/8, new Font("SansSerif", Font.ITALIC, 15));
		centeredStrings[4] = new CenteredString(g, "Two Player", GameEngine.displayWidth/2, GameEngine.displayHeight/2, GameEngine.displayWidth/2, GameEngine.displayHeight*5/8, new Font("SansSerif", Font.ITALIC, 30));
		centeredStrings[5] = new CenteredString(g, "Play against each other using", GameEngine.displayWidth/2, GameEngine.displayHeight/2 + 30, GameEngine.displayWidth/2, GameEngine.displayHeight*5/8, new Font("SansSerif", Font.ITALIC, 15));
		centeredStrings[6] = new CenteredString(g, "WASD and the arrow keys", GameEngine.displayWidth/2, GameEngine.displayHeight/2 + 50, GameEngine.displayWidth/2, GameEngine.displayHeight*5/8, new Font("SansSerif", Font.ITALIC, 15));
		
		centeredStrings[7] = new CenteredString(g, "Player 1 Wins", 0, 0, GameEngine.displayWidth, GameEngine.displayHeight/3, new Font("SansSerif", Font.BOLD, 100));
		centeredStrings[8] = new CenteredString(g, "Player 2 Wins", 0, 0, GameEngine.displayWidth, GameEngine.displayHeight/3, new Font("SansSerif", Font.BOLD, 100));
		centeredStrings[9] = new CenteredString(g, "Press Enter or Click to Play Again", 0, GameEngine.displayHeight/2, GameEngine.displayWidth, GameEngine.displayHeight*5/8, new Font("SansSerif", Font.ITALIC, 30));
	}
	
	void render(Graphics2D g2d, int gameState, int score1, int score2) {
		if(gameState == 0) {
			for (int i = 0; i < 7; i++) {
				centeredStrings[i].render(g2d, Color.white);
			}
		} else if (gameState == 1 || gameState == 2) {
			g2d.setColor(Color.white);
			g2d.setStroke(dashed);
			g2d.drawLine(GameEngine.displayWidth/2, 0, GameEngine.displayWidth/2, GameEngine.displayHeight);
			CenteredString cscore1 = new CenteredString(g2d, String.valueOf(score1), 0, 0, GameEngine.displayWidth/2, GameEngine.displayHeight/3, new Font("SansSerif", Font.PLAIN, 80));
			CenteredString cscore2 = new CenteredString(g2d, String.valueOf(score2), GameEngine.displayWidth/2, 0, GameEngine.displayWidth/2, GameEngine.displayHeight/3, new Font("SansSerif", Font.PLAIN, 80));
			cscore1.render(g2d, Color.white);
			cscore2.render(g2d, Color.white);
		} else if (gameState == 3) {
			if(score1 > score2) {
				centeredStrings[7].render(g2d, Color.white);
			} else {
				centeredStrings[8].render(g2d, Color.white);
			}
			centeredStrings[9].render(g2d, Color.white);
		}
	}
}
