package pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import components.Paddle;
import components.PongMovable;
import components.RefactoredBall;
import gameEngine.GameEngine;
import gameEngine.IGameLogic;
import threeDimensions.Graphics3D;

public class RefactoredPong implements IGameLogic {
	
	private int gameState = 0;
	private int selector = 0;
	private int score1 = 0, score2 = 0;
	private PongMovable[] pongObjects = new PongMovable[3];
	private PongLayout pongLayout = new PongLayout();
	private RayMarching rm = new RayMarching();
	
	@Override
	public void init(Graphics g) throws Exception {
		// TODO Auto-generated method stub
		rm.init(g);
		pongLayout.init(g);
		pongObjects[0] = new RefactoredBall(Color.white, 5, 3);
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
			if(GameEngine.mouse.mouseX > GameEngine.displayWidth/2) selector = 1;
			else selector = 0;
			if(GameEngine.keyboard.keysTyped[KeyEvent.VK_ENTER] || GameEngine.mouse.mouseClicked ) {
				GameEngine.keyboard.keysTyped[KeyEvent.VK_ENTER] = false;
				GameEngine.mouse.mouseClicked = false;
				score1 = 0; score2 = 0;
				this.gameState = -selector + 2;
			} else if(GameEngine.keyboard.keysTyped[KeyEvent.VK_R]) {
				this.gameState = 99;
				rm.reset();
				GameEngine.keyboard.keysTyped[KeyEvent.VK_R] = false;
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
		} else if (gameState == 99) {
			if (GameEngine.keyboard.keysTyped[KeyEvent.VK_ESCAPE]) {
				GameEngine.keyboard.keysTyped[KeyEvent.VK_ESCAPE] = false;
				this.gameState = 0;
			} else {
				rm.input();
			}
		}
		
	}

	@Override
	public void update(float interval) {
		// TODO Auto-generated method stub
		if (gameState == 2) {
			((Paddle) pongObjects[2]).think((RefactoredBall) pongObjects[0]);
		}
		if(gameState == 1 || gameState == 2) {
			for (PongMovable pm : pongObjects) {
				pm.update();
			}
			for (int i = 1; i < 3; i++) {
				((RefactoredBall) pongObjects[0]).collision((Paddle) pongObjects[i]); 
			}
			score2 += (((RefactoredBall) pongObjects[0]).touchingEdge(false)) ? 1 : 0;
			score1 += (((RefactoredBall) pongObjects[0]).touchingEdge(true)) ? 1 : 0;
			if(score1 >= 3 || score2 >= 3) {
				this.gameState = 3;
			}
		} else if (gameState == 99) {
			rm.update(interval);
			score1 = rm.score1; score2 = rm.score2;
			if(score1 >= 3 || score2 >= 3) {
				this.gameState = 3;
				rm.score1 = 0;
				rm.score2 = 0;
			}
		}
	}

	@Override
	public void render(Graphics2D g2d) {
		// TODO Auto-generated method stub
		g2d.setColor(Color.darkGray);
		if(gameState == 0) g2d.fillRect(GameEngine.displayWidth/2 * selector, 0, GameEngine.displayWidth/2, GameEngine.displayHeight);
		pongLayout.render(g2d, gameState, score1, score2);
		if(gameState == 1 || gameState == 2) {
			for (PongMovable pm : pongObjects) {
				pm.render(g2d);
			}
		}
		if(gameState == 99) {
			rm.render(g2d);
		}
	}

	@Override
	public void render3D(Graphics3D g3d) {
		// TODO Auto-generated method stub
		if(gameState == 99) {
			rm.render3D(g3d);
		}
	}

}
