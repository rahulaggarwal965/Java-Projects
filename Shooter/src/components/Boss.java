package components;

import java.util.Random;

import shooter.ShooterSound;
import threeDimensions.Graphics3D;
import threeDimensions.PackedColor;
import threeDimensions.ParticleSystem;
import threeDimensions.Pipeline;
import threeDimensions.Vec3;

public class Boss extends Enemy {
	
	protected final float MAX_HEALTH = 200.0f;
	
	private final float missileInterval = 10.0f;
	private float missileCooldown = 0.0f;
	
	private Missile[] missiles;
	private int missileCount = 0;

	public Boss(ParticleSystem ps, Player player) {
		super(ps, player);
		this.missiles = new Missile[3];
		for (int i = 0; i < 3; i++) {
			this.missiles[i] = new Missile(ps);
			this.missiles[i].init();
		}
		this.missileCount = 0;
	}
	
	public void clearMissiles() {
		for (int i = 0; i < this.missileCount; i++) {
			this.missiles[i].setLife(0.0f);
		}
		this.missileCount = 0;
	}
	
	public void addMissile() {
		if(this.missileCount != this.missiles.length) {
			Missile m = this.missiles[missileCount];
			m.setLife(10.0f);
			Random rand = new Random();
			float sign = rand.nextInt(2) * 2 - 1;
			Vec3 position = this.position._add(this.right._multiply(15.0f * sign));
			m.setPosition(position.x, position.y, position.z);
			m.setTargetPosition(this.player.getPosition());
			m.setRotation(this.rotation.x, this.rotation.y, this.rotation.z);
			m.setVelocity(0, 0, this.velocity.z + 50);
			ShooterSound.MISSILE.play(false);
			this.missileCount++;
		}
	}
	
	public Missile[] getMissiles() {
		return this.missiles;
	}
	
	public int getMissileCount() {
		return this.missileCount;
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		this.missileCooldown -= deltaTime;
		
		if(missileCooldown <= 0) {
			this.missileCooldown = this.missileInterval;
			this.addMissile();
		}
		
		for (int i = 0; i < this.missileCount; i++) {
			if(this.missiles[i].getLife() <= 0) {
				Missile temp = this.missiles[i];
				this.missiles[i] = this.missiles[this.missileCount - 1];
				this.missiles[this.missileCount - 1] = temp;
				this.missileCount--;
			} else {
				this.missiles[i].update(deltaTime);
			}
		}
	}
	
	public void render(Graphics3D g, Pipeline p) {
		super.render(g, p);
		p.getShader().setDefaultColor(PackedColor.Red);
		for (int i = 0; i < this.missileCount; i++) {
			this.missiles[i].render(g, p);
		}
		
	}

}
