package pong;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import components.Paddle;
import components.PongMovable;
import components.Ball;
import gameEngine.GameEngine;
import gameEngine.GameLogic;
import threeDimensions.Graphics3D;

public class Pong implements GameLogic {
	
	private int gameState = 0;
	private int selector = 0;
	private int score1 = 0, score2 = 0;
	private PongMovable[] pongObjects = new PongMovable[3];
	private PongLayout pongLayout = new PongLayout();
	
	@Override
	public void init(Graphics2D g) throws Exception {
		// TODO Auto-generated method stub
		pongLayout.init(g);
		pongObjects[0] = new Ball(Color.white, 5, 3);
		pongObjects[1] = new Paddle(Color.white, 10, 80, true);
		pongObjects[2] = new Paddle(Color.white, 10, 80, false);
		for (PongMovable pm : pongObjects) {
			pm.init();
		}
		
	}

	@Override
	public void input() {
		// TODO Auto-generated method stub
		if(this.gameState == 0) {
			if(GameEngine.mouse.position.x > GameEngine.displayWidth/2) selector = 1;
			else selector = 0;
			if(GameEngine.keyboard.keysTyped[KeyEvent.VK_ENTER] || GameEngine.mouse.mouseClicked ) {
				GameEngine.keyboard.keysTyped[KeyEvent.VK_ENTER] = false;
				GameEngine.mouse.mouseClicked = false;
				score1 = 0; score2 = 0;
				this.gameState = -selector + 2;
			}
		} else if(this.gameState == 1 || this.gameState == 2) {
			for (PongMovable pm : pongObjects) {
				pm.input();
			}
		} else if(this.gameState == 3) {
			if(GameEngine.keyboard.keysTyped[KeyEvent.VK_ENTER] || GameEngine.mouse.mouseClicked ) {
				GameEngine.keyboard.keysTyped[KeyEvent.VK_ENTER] = false;
				GameEngine.mouse.mouseClicked = false;
				this.gameState = 0;
			}
		}
	}

	@Override
	public void update(float interval) {
		// TODO Auto-generated method stub
		if (gameState == 2) {
			((Paddle) pongObjects[2]).think((Ball) pongObjects[0]);
		}
		if(gameState == 1 || gameState == 2) {
			for (PongMovable pm : pongObjects) {
				pm.update();
			}
			for (int i = 1; i < 3; i++) {
				((Ball) pongObjects[0]).collision((Paddle) pongObjects[i]); 
			}
			score2 += (((Ball) pongObjects[0]).touchingEdge(false)) ? 1 : 0;
			score1 += (((Ball) pongObjects[0]).touchingEdge(true)) ? 1 : 0;
			if(score1 >= 3 || score2 >= 3) {
				this.gameState = 3;
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.darkGray);
		if(gameState == 0) g.fillRect(GameEngine.displayWidth/2 * selector, 0, GameEngine.displayWidth/2, GameEngine.displayHeight);
		pongLayout.render(g, gameState, score1, score2);
		if(gameState == 1 || gameState == 2) {
			for (PongMovable pm : pongObjects) {
				pm.render(g);
			}
		}
	}

	@Override
	public void render(Graphics3D g) {
		// TODO Auto-generated method stub
	}

}
