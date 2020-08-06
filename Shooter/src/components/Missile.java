package components;

import math.Maths;
import math.Quaternion;
import shaders.Shader;
import threeDimensions.GameObject;
import threeDimensions.Graphics3D;
import threeDimensions.Matrix;
import threeDimensions.Mesh;
import threeDimensions.PackedColor;
import threeDimensions.ParticleSystem;
import threeDimensions.Pipeline;
import threeDimensions.Primitive;
import threeDimensions.Vec3;
import threeDimensions.Vec4;

public class Missile extends GameObject {

	protected final static Vec4 missileForward = new Vec4(0, 1, 0, 0);
	protected final static Vec4 missileRight = new Vec4(1, 0, 0, 0);
	protected final static Vec4 missileUp = new Vec4(0, 0, -1, 0);
	
	protected static final Mesh MISSILE_MESH = new Primitive().scale(0.01f, 0.4f, 0.01f).extrude(1, 4, (float theta, float h) -> {return 1;}).resolve();
	protected final float ACC_MAX = 5;
	protected final float VEL_LIMIT = 300.0f;
	
	protected Vec3 velocity = new Vec3(0, 0, 0);
	protected Vec3 acceleration = new Vec3(0, 0, 0);
	
	protected float life = 10.0f;

	protected final float pInterval = 0.1f;
	protected float pCooldown = 0f;
	protected Vec3 pDelta = new Vec3(0, 0, -5);
	protected ParticleSystem ps;
	protected Mesh pQuad = new Mesh(
			new int[] {0, 1, 3, 1, 2, 3}, 
			new int[] {3, 2}, 
			new float[] {
				-1.0f, -1.0f, 0f,
				-1.0f, 1.0f, 0f,
				1.0f, 1.0f, 0f,
				1.0f, -1.0f, 0f},
			new float[] {
				0, 1,
				0, 0,
				1, 0, 
				1, 1
			});
	
	protected Quaternion rotationQ;
	
	protected Vec4 forward = new Vec4(Missile.missileForward);
	protected Vec4 right = new Vec4(Missile.missileRight);
	protected Vec4 up = new Vec4(Missile.missileUp);
	
	protected Vec3 targetPosition = new Vec3(0, 0, 0);
	
	public Missile(ParticleSystem ps) {
		this.ps = ps;
	}
	
	public void init() {
		this.rotationQ = Quaternion.EulerAngles(this.rotation.x, this.rotation.y, this.rotation.z);
		this.setMesh(MISSILE_MESH);
		this.setScale(25);
	}
	
	public void setLife(float life) {
		this.life = life;
	}
	
	public float getLife() {
		return this.life;
	}
	
	public void setVelocity(float x, float y, float z) {
		this.velocity.set(x, y, z);
	}
	
	public void setTargetPosition(Vec3 targetPosition) {
		this.targetPosition.set(targetPosition);
	}
	
	public void update(float deltaTime) {
		
		this.life -= deltaTime;
			
		this.acceleration.set(0, 0, ACC_MAX  * this.life / 10.0f);
		this.velocity.add(this.acceleration);
		this.velocity.z = Math.min(this.velocity.z, VEL_LIMIT);

		Vec3 d = targetPosition._subtract(this.position);
		
		float yAxis = Maths.atan2(d.x, d.z);
		float xAxis = Maths.atan2(-d.y, Maths.sqrt(d.x*d.x + d.z*d.z));
		
		float yDelta = ((((yAxis - this.rotation.y) % Maths.PI2) + 3*Maths.PI) %  Maths.PI2) - Maths.PI;
		float xDelta = ((((xAxis - this.rotation.x + Maths.PI/2) % Maths.PI2) + 3*Maths.PI) %  Maths.PI2) - Maths.PI;
		
		this.rotation.y = Maths.wrapAngle(this.rotation.y + Maths.clamp(yDelta % Maths.PI, -0.1f, 0.1f));
		this.rotation.x = Maths.wrapAngle(this.rotation.x + Maths.clamp(xDelta % Maths.PI, -0.1f, 0.1f));
		
		
		//Set Quaternion
		this.rotationQ.set(Quaternion.EulerAngles(this.rotation));
		
		//Get Rotation Matrix and Direction Vectors
		Matrix rotation = this.rotationQ.getRotationMatrix(4);
		this.forward.set(rotation._multiply(Missile.missileForward));
		this.right.set(rotation._multiply(Missile.missileRight));
		this.up.set(rotation._multiply(Missile.missileUp));
		
		//Update Position
		this.position.add(this.right._multiply(this.velocity.x * deltaTime));
		this.position.add(this.up._multiply(this.velocity.y * deltaTime));
		this.position.add(this.forward._multiply(this.velocity.z * deltaTime));
		
		
		//Apply Translation
		
		this.pCooldown -= deltaTime;
		if(this.pCooldown <= 0) {
			this.ps.addParticle(pQuad, this.position._add(this.forward._multiply(this.pDelta.z)), Vec3.zero, 0.8f, this.forward._multiply(this.velocity.z* 0.8f), 1f);
			this.pCooldown = this.pInterval;
		}
		
	}
	
	public void render(Graphics3D g, Pipeline p) {
		Shader s = p.getShader();

		Matrix world = Matrix.Translation(this.position).multiply(this.rotationQ.getRotationMatrix(4));
		
		s.setWorld(world.multiply(Matrix.Scale(4, this.scale)));
		p.draw(g, this.mesh);
	}
	
}
