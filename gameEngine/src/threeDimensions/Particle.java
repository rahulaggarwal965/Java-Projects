package threeDimensions;

import math.Matrix;
import math.Vec3;

public class Particle extends GameObject {
	
	private Vec3 velocity;
	private float life;
	private float dScale = 0;
	
	public Particle() {
		super();
		this.life = 0;
		this.velocity = new Vec3(0, 0, 0);
	}

	public Particle(Mesh mesh, Vec3 speed, float life) {
		super(mesh);
		this.velocity = new Vec3(speed);
		this.life = life;
	}
	
	public Particle(Particle p) {
		super(p.getMesh());
		this.position.set(p.getPosition());
		this.rotation.set(p.getRotation());
		this.setScale(p.getScale());
		this.velocity = new Vec3(p.getVelocity());
		this.life = p.getLife();
	}
	
	public void set(Particle p) {
		this.set(p.getMesh(), p.getPosition(), p.getRotation(), life, p.getVelocity(), p.life);
	}
	
	public void set(Mesh m, Vec3 position, Vec3 rotation, float scale, Vec3 velocity, float life) {
		this.setMesh(m);
		this.position.set(position);
		this.rotation.set(rotation);
		this.velocity.set(velocity);
		this.scale = scale;
		this.life = life;
		this.dScale = scale/life;
	}

	public Vec3 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vec3 velocity) {
		this.velocity = velocity;
	}

	public float getLife() {
		return life;
	}

	public void setLife(float life) {
		this.life = life;
	}
	
	public void update(float deltaTime) {
		this.position.add(this.velocity._multiply(deltaTime));
		this.life -= deltaTime;
		this.scale -= dScale * deltaTime;
	}
	
	public void render(Graphics3D g, Pipeline p) {
		Matrix world = Matrix.Translation(this.position);
		float[] viewData = p.getShader().getView().getData();
		float[] worldData = world.getData();
		for (int j = 0; j < 3; j++) {
			for (int i = 0; i < 3; i++) {
				worldData[i + j * 4] = viewData[j + i * 4];
			}
		}
		world = world.multiply(Matrix.Scale(4, this.scale));
		p.getShader().setWorld(world);
		p.draw(g, this.mesh);
	}

}
