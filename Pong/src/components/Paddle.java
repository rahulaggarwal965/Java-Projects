package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import gameEngine.GameEngine;

public class Paddle extends PongMovable {
	
	protected int width, height;
	protected boolean side;

	public Paddle(int dx, int dy, int x, int y, Color col, int width, int height, boolean side) {
		super(dx, dy, x, y, col);
		// TODO Auto-generated constructor stub
		this.width = width;
		this.height = height;
		this.side = side;
	}
	
	public Paddle() {
		super();
		this.width = 0;
		this.height = 0;
		this.side = false;
	}
	
	public Paddle(Color col, int width, int height, boolean side) {
		super(col);
		this.width = width;
		this.height = height;
		this.side = side;
	}
	
	@Override
	public void init() {
		this.x = (side) ? 50 : GameEngine.displayWidth - 50;
		this.y = GameEngine.displayHeight/2 - this.height/2;
	}
	
	@Override
	public void input() {
		this.dy = 0;
		if(!this.side) {
			if(GameEngine.keyboard.keysPressed[KeyEvent.VK_UP]) {
				this.dy = -10;
			}
			if(GameEngine.keyboard.keysPressed[KeyEvent.VK_DOWN]) {
				this.dy = 10;
			}
		} else {
			if(GameEngine.keyboard.keysPressed[KeyEvent.VK_W]) {
				this.dy = -10;
			}
			if(GameEngine.keyboard.keysPressed[KeyEvent.VK_S]) {
				this.dy = 10;
			}
		}
		
	}
	
	@Override
	public void update() {
		this.translateY(this.dy);
		if(this.y < 0 || this.y + this.height > GameEngine.displayHeight) {
			this.translateY(-this.dy);
		}
	}
	
	@Override
	public void render(Graphics g) {
		g.setColor(this.col);
		g.fillRect(this.x, this.y, this.width, this.height);
	}
	
	public void think(int ballY, int ballDx, int ballDy) {
		this.dy = 0;
		if (ballDx > 0) {
			int diff = ballY - this.y;
			if(diff < 0 && ballDy < 0) this.dy = Math.max(-5, diff/3);
			else if (diff > 0 && ballDy > 0) this.dy = Math.min(5, diff/3);
		}
	}
	
	public void think(Ball ball) {
		this.think(ball.getY(), ball.getDx(), ball.getDy());
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	

	
	
	
}
