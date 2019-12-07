package components;

import java.awt.Color;
import java.awt.Graphics2D;

public class PowerUp extends GameObject {
	
	public static final int SPEED = 200; 
	private static final Color[] COLORS = {Color.cyan, Color.yellow, Color.orange, Color.red};
	
	private int type;
	private float life;
	
	public PowerUp(float x, float y, float width, float height, float angle, float speed, int type, float life) {
		super(x, y, width, height, angle, speed, COLORS[type]);
		this.type = type;
		this.life = life;
	}

	@Override
	public void init() {
	}

	@Override
	public void input() {
	}
	
	public void update(double deltaTime) {
		if(this.life > 0) {
			super.update(deltaTime);
			this.life -= deltaTime;
		}
	}

	@Override
	public void render(Graphics2D g2d) {
		if(this.life > 0) {
			g2d.setColor(this.c);
			g2d.fillRect((int) this.position.x, (int) this.position.y, (int) this.size.x, (int) this.size.y);
		}
	}
	
	public int getType() {
		return this.type;
	}
	
	public boolean isAlive() {
		return this.life > 0;
	}
	
	public void setLife(float life) {
		this.life = life;
	}
	
}
