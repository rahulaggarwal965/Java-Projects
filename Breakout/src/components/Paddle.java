package components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import breakout.BreakoutGUI;
import gameEngine.GameEngine;

public class Paddle extends GameObject {
	
	//PowerUp Variables
	public static final float initialWidth = 150;
	public static final float fastSpeed = 1200;
	private static final float initialSpeed = 600;
	
	private float powerUpTimer = 0;
	private float activeSpeed = initialSpeed;
	private int lives = 4;
	
	public Paddle(float x, float y, float width, float height, Color col) {
		super(x, y, width, height, col);
		this.setVelocity(0, this.activeSpeed);
		this.id = 0;
	}
	
	public void init() {
		this.lives = 4;
	}
	
	public void input() {
		this.setVelocity(0, 0);
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_A]) {
			this.setVelocity((float) Math.PI, this.activeSpeed);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_D]) {
			this.setVelocity(0, this.activeSpeed);
		}
	}
	
	public void update(double deltaTime) {
		super.update(deltaTime);
		
		if(this.powerUpTimer > 0) {
			this.powerUpTimer -= deltaTime;
			if(this.powerUpTimer <= 0) {
				this.setActiveSpeed(initialSpeed);
				this.setWidth(initialWidth);
			}
		}
		if(this.position.x < 0) {
			this.setX(0);
		}
		if(this.position.x + this.size.x > GameEngine.displayWidth) {
			this.setX(GameEngine.displayWidth - this.size.x);
		}
	}
	
	public void render(Graphics2D g2d) {
		g2d.setColor(this.c);
		g2d.fillRect((int) this.position.x, (int) this.position.y, (int) this.size.x, (int) this.size.y);
		BreakoutGUI.drawCenteredString(g2d, "Lives: " + String.valueOf(this.lives), BreakoutGUI.labelFont, GameEngine.displayWidth - 50, 780);
	}

	public void loseLife() {
		this.lives--;
	}
	
	public boolean isDead() {
		return this.lives == 0;
	}

	public float getActiveSpeed() {
		return activeSpeed;
	}

	public void setActiveSpeed(float activeSpeed) {
		this.activeSpeed = activeSpeed;
	}
	
	public void setPowerUpTimer(float time) {
		if(this.powerUpTimer <= 0) this.powerUpTimer = time;
	}
	
}
