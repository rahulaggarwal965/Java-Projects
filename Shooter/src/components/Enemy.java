package components;

import math.Maths;
import threeDimensions.Graphics3D;
import threeDimensions.Matrix;
import threeDimensions.Mesh;
import threeDimensions.PackedColor;
import threeDimensions.ParticleSystem;
import threeDimensions.Pipeline;
import threeDimensions.Ray;
import threeDimensions.Vec3;

public class Enemy extends Plane {

	protected final float MAX_HEALTH = 100.0f;
	protected Player player;
	
	private Mesh healthBar = new Mesh(
			new int[] {0, 1, 3, 1, 2, 3}, 
			new int[] {3},
			new float[] {
				-1.0f, -1.0f, 0f,
				-1.0f, 1.0f, 0f,
				1.0f, 1.0f, 0f,
				1.0f, -1.0f, 0f});
	
	public Enemy(ParticleSystem ps, Player player) {
		super(ps);
		this.player = player;
	}
	
	public void init() {
		this.health = this.MAX_HEALTH;
		this.setRotation(Maths.PI/2, 0, 0);
		//this.setPosition(0, 75, 40);
		super.init();
	}
	
	public void update(float deltaTime) {
		
		this.bullet = null;
		
		this.bulletCooldown -= deltaTime;
		Vec3 d = this.player.getPosition()._subtract(this.position);
		float dist = d.mag();
		
		if(dist < 100) {
			d = this.getPosition()._subtract(this.forward._multiply(20.0f))._add(Vec3.random(5.0f));
		} else if(dist < 150) {
			if(this.bulletCooldown <= 0) {
				this.bulletCooldown = bulletInterval;
				this.bullet = new Ray(this.position, this.forward);
				this.ps.addParticle(pQuad, this.position._add(this.forward._multiply(30)), Vec3.zero, 1.2f, this.forward._multiply(this.velocity.z + 100), 0.5f);
			}
		} else if (dist > 300) {
			this.acceleration.z = ACC_MAX;
		}
		float yAxis = Maths.atan2(d.x, d.z);
		float xAxis = Maths.atan2(-d.y, Maths.sqrt(d.x*d.x + d.z*d.z));
		
		float yDelta = ((((yAxis - this.rotation.y) % Maths.PI2) + 3*Maths.PI) %  Maths.PI2) - Maths.PI;
		float xDelta = ((((xAxis - this.rotation.x + Maths.PI/2) % Maths.PI2) + 3*Maths.PI) %  Maths.PI2) - Maths.PI;
		
		this.angularAcceleration.y = 4 * Maths.clamp(yDelta % Maths.PI, -1f, 1f);
		this.angularAcceleration.x = 4 * Maths.clamp(xDelta % Maths.PI, -1f, 1f);
		this.angularAcceleration.z = -3f * Maths.clamp(yDelta % Maths.PI + this.right.y*0.5f, -1f, 1f);
			
			
		super.update(deltaTime);
	}
	
	public void render(Graphics3D g, Pipeline p) {
		super.render(g, p);
		float scale = this.health / this.MAX_HEALTH * 5;
		Matrix world = Matrix.Translation(this.position._add(new Vec3(scale - 5, 8, 0)));
		float[] viewData = p.getShader().getView().getData();
		float[] worldData = world.getData();
		for (int j = 0; j < 3; j++) {
			for (int i = 0; i < 3; i++) {
				worldData[i + j * 4] = viewData[j + i * 4];
			}
		}
		world = world.multiply(Matrix.Scale(4, scale, 0.5f, 1f));
		p.getShader().setWorld(world);
	
		p.setDrawMode(Pipeline.TRIANGLE_MODE);
		p.getShader().setDefaultColor(PackedColor.Red);
		p.draw(g, this.healthBar);
	}
}
