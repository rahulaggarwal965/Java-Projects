package components;

import java.awt.Graphics2D;
import java.util.Random;

import gameEngine.GameEngine;

public class PowerUpSystem {
	
	private final double COOLDOWN = 5.0;
	private double time = COOLDOWN;
	
	private PowerUp activePowerUp;
	
	public void init() {
		
	}
	
	public void input() {
		
	}
	
	public void spawnPowerUp() {
		Random rand = new Random();
		this.activePowerUp = new PowerUp(
				rand.nextInt(GameEngine.displayWidth), 
				rand.nextInt(3 * GameEngine.displayHeight/4), 
				20, 20,	
				(float) (Math.random() * -Math.PI/2 - Math.PI/4),
				PowerUp.SPEED,
				rand.nextInt(4),
				(float) Math.random() * 5 + 3);
	}
	
	public void update(double deltaTime) {
		if(this.activePowerUp != null) {
			this.activePowerUp.update(deltaTime);
			if(!this.activePowerUp.isAlive()) {
				this.activePowerUp = null;
			}
		}
		else {
			time -= deltaTime;
			if(time <= 0) {
				time = COOLDOWN;
				this.spawnPowerUp();
			}
		}
	}
	
	public void render(Graphics2D g2d) {
		if(this.activePowerUp != null) this.activePowerUp.render(g2d);
	}

	public PowerUp getActivePowerUp() {
		return activePowerUp;
	}

	public void setActivePowerUp(PowerUp activePowerUp) {
		this.activePowerUp = activePowerUp;
	}
}
