package components;

import java.awt.event.KeyEvent;
import java.util.Random;

import gameEngine.GameEngine;
import math.Maths;
import shooter.ShooterSound;
import threeDimensions.Camera;
import threeDimensions.Graphics3D;
import threeDimensions.PackedColor;
import threeDimensions.ParticleSystem;
import threeDimensions.Pipeline;
import threeDimensions.Ray;
import threeDimensions.Vec3;

public class Player extends Plane {
	
	public static final float maxHealth = 200.0f;
	
	private float cameraDamping = 0.1f;
	private Camera camera;
	private Vec3 cameraDelta = new Vec3(0, 10, -50);
	
	private int cameraState = 0;
	
	private final float missileInterval = 5.0f;
	private float missileCooldown = 0.0f;
	
	private Missile[] missiles;
	private int missileCount = 0;
	
	public Player(ParticleSystem ps) {
		super(ps);
		this.camera = new Camera(0, 80, -20, 0, 0, 0);
		this.health = Player.maxHealth;
		this.missiles = new Missile[5];
		for (int i = 0; i < 5; i++) {
			this.missiles[i] = new Missile(ps);
			this.missiles[i].init();
		}
		this.missileCount = 0;
	}
	
	public Ray getBullet() {
		return this.bullet;
	}
	
	@Override
	public void init() {
		this.setRotation(Maths.PI/2, 0, 0);
		this.setPosition(0, 75, 5);
		this.clearMissiles();
		super.init();
	}
	
	public Camera getCamera() {
		return this.camera;
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
	
	public void input() {
		this.acceleration.set(0 ,0 ,0);
		this.angularAcceleration.set(0, 0, 0);
		this.bullet = null;
		
		// Movement
		if (GameEngine.keyboard.keysPressed[KeyEvent.VK_W]) {
			this.acceleration.z = ACC_MAX;
		}
		if (GameEngine.keyboard.keysPressed[KeyEvent.VK_S]) {
			this.acceleration.z = -ACC_MAX;
		}
		
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_SPACE] && this.bulletCooldown <= 0) {
			this.bulletCooldown = bulletInterval;
			this.bullet = new Ray(this.position, this.forward);
			ShooterSound.BULLET.play(false);
			this.ps.addParticle(pQuad, this.position._add(this.forward._multiply(30)), Vec3.zero, 1.2f, this.forward._multiply(this.velocity.z + 100), 0.5f);
		}
		
		
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_V] && this.missileCooldown <= 0) {
			this.missileCooldown = missileInterval;
			this.addMissile();
		}
		
		if(GameEngine.keyboard.keysTyped[KeyEvent.VK_1]) {
			GameEngine.keyboard.keysTyped[KeyEvent.VK_1] = false;
			this.cameraState = 0;
		}
		
		if(GameEngine.keyboard.keysTyped[KeyEvent.VK_2]) {
			GameEngine.keyboard.keysTyped[KeyEvent.VK_2] = false;
			this.cameraState = 1;
		}
		if(GameEngine.keyboard.keysTyped[KeyEvent.VK_3]) {
			GameEngine.keyboard.keysTyped[KeyEvent.VK_3] = false;
			this.cameraState = 2;
		}
		
		if (GameEngine.keyboard.keysPressed[KeyEvent.VK_LEFT]) {
			this.angularAcceleration.y -= 6f;
		}
		if (GameEngine.keyboard.keysPressed[KeyEvent.VK_RIGHT]) {
			this.angularAcceleration.y += 6f;
		}
		if (GameEngine.keyboard.keysPressed[KeyEvent.VK_UP]) {
			this.angularAcceleration.x -= 4f;
		}
		if (GameEngine.keyboard.keysPressed[KeyEvent.VK_DOWN]) {
			this.angularAcceleration.x += 4f;
		}
		if (GameEngine.keyboard.keysPressed[KeyEvent.VK_Q]) {
			this.angularAcceleration.z += 4f;
		}
		if (GameEngine.keyboard.keysPressed[KeyEvent.VK_E]) {
			this.angularAcceleration.z -= 4f;
		}
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		this.bulletCooldown -= deltaTime;
		this.missileCooldown -= deltaTime;
		
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
		
		if(this.cameraState == 0) {
			this.cameraDelta.y = 10;
			this.cameraDelta.z = -0.000625f * (this.velocity.z * this.velocity.z) - 50; //z because only forward 
			
			float xTo = 0.0f;
			if(Math.abs(this.forward.y) > 0.9f) xTo = this.rotation.x - Maths.PI/2;
			
			float camRotXDelta = ((((xTo - this.camera.rotation.x) % Maths.PI2) + 3*Maths.PI) %  Maths.PI2) - Maths.PI;
			float xDelta = this.camera.rotation.x + (camRotXDelta * 0.05f) % Maths.PI;
			
			float camRotYDelta = ((((this.rotation.y - this.camera.rotation.y) % Maths.PI2) + 3*Maths.PI) %  Maths.PI2) - Maths.PI;
			float yDelta = this.camera.rotation.y + (camRotYDelta * cameraDamping) % Maths.PI;
			this.camera.setRotation(xDelta, yDelta, 0f);
		} else if(this.cameraState == 1) {
			this.cameraDelta.y = 0;
			this.cameraDelta.z = 20;
			this.camera.setRotation(this.rotation.x - Maths.PI/2, this.rotation.y, this.rotation.z);
		} else if(this.cameraState == 2) {
			this.cameraDelta.y = 0;
			this.cameraDelta.z = -400;
			this.camera.setRotation(Maths.PI/2, 0f, 0f);
		}
		this.camera.setPosition(this.position);
		this.camera.translate(cameraDelta);
	}
	
	public void render(Graphics3D g, Pipeline p) {
		super.render(g, p);
		p.getShader().setDefaultColor(PackedColor.Green);
		for (int i = 0; i < this.missileCount; i++) {
			this.missiles[i].render(g, p);
		}
		
	}

}
