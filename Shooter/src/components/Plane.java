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
import threeDimensions.Ray;
import threeDimensions.Vec3;
import threeDimensions.Vec4;

public class Plane extends GameObject{
	
	protected final static Vec4 planeForward = new Vec4(0, 1, 0, 0);
	protected final static Vec4 planeRight = new Vec4(1, 0, 0, 0);
	protected final static Vec4 planeUp = new Vec4(0, 0, -1, 0);
	
	protected static final Mesh PLANE_MESH = new Primitive().scale(0.1f, 1.5f, 0.1f).extrude(10, 20, (float theta, float h) -> {
		return 1f;
	}).translate(0, 0.95f, 0).scale(0.1f, 0.4f, 0.1f).extrude(20, 20, (float theta, float h) -> {
		return Maths.pow(1-(h + 0.5f), 0.61f);
	}).translate(0, -0.775f, 0).scale(0.1f, 0.05f, 0.1f).extrude(10, 20, (float theta, float h) -> {
		return 0.15f*(h - 0.5f) + 0.95f;
	})
	.translate(0.6f, -0.2f, 0).scale(0.01f, 0.4f, 0.01f).extrude(1, 4, (float theta, float h) -> {return 1;})
	.translate(-0.6f, -0.2f, 0).scale(0.01f, 0.4f, 0.01f).extrude(1, 4, (float theta, float h) -> {return 1;})
	.translate(0.1f, 0, 0).rotate(0, 0, 0).scale(0.5f, 1, 1).quadCorners(0, -0.3f, 0, 0.45f, 1, -0.2f, 1, -0.4f, 0.01f)
	.translate(-0.1f, 0, 0).quadCorners(-1, -0.4f, -1, -0.2f, 0, 0.45f, 0, -0.3f, 0.01f)
	.translate(0.1f, 0, 0).quadCorners(0, -0.72f, 0, -0.39f, 0.5f, -0.6f, 0.5f, -0.77f, 0.01f)
	.translate(-0.1f, 0, 0).quadCorners(-0.5f, -0.77f, -0.5f, -0.6f, 0, -0.39f, 0, -0.72f, 0.01f)
	.translate(0, 0, -0.1f).rotate(0, Maths.PI/2, 0).quadCorners(0, -0.72f, 0, -0.39f, 0.5f, -0.65f, 0.5f, -0.85f, 0.01f)
			.resolve();
	
	protected static final Mesh planeBoundingBox = new Primitive().quadCorners(-0.1f, -0.8f, -0.1f, 1.15f, 0.1f, 1.15f, 0.1f, -0.8f, 0.2f).resolve();
	
	protected Vec3 bbMin = new Vec3(-0.1f, -0.8f, -0.1f)._multiply(25);
	protected Vec3 bbMax = new Vec3(0.1f,1.15f, 0.1f)._multiply(25);
	
	protected final float ACC_MAX = 20; //??
	protected final float VEL_LIMIT = 200;
	protected final float VEL_MIN = 50;
	protected Vec3 velocity = new Vec3(0, 0, 0);
	protected Vec3 acceleration = new Vec3(0, 0, 0);
	
	protected Vec3 angularAcceleration = new Vec3(0, 0, 0);
	protected Vec3 angularVelocity = new Vec3(0, 0, 0);
	protected float angularDrag = 0.07f;
	
	protected int bbColor = PackedColor.Red;
	protected Matrix world = Matrix.Identity(4);
	
	protected final float pInterval = 0.1f;
	protected float pCooldown = 0f;
	protected Vec3 pDelta = new Vec3(0, 0, -20);
	protected ParticleSystem ps;
	public static Mesh pQuad = new Mesh(
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
	
	protected Vec4 forward = new Vec4(Plane.planeForward);
	protected Vec4 right = new Vec4(Plane.planeRight);
	protected Vec4 up = new Vec4(Plane.planeUp);
	
	protected Ray bullet = null;
	protected final float bulletInterval = 0.05f;
	protected float bulletCooldown = 0f;
	
	protected float health = 100.0f;
	
	public Plane(ParticleSystem ps) {
		this.ps = ps;
	}
	
	public void init() {
		this.setMesh(PLANE_MESH);
		this.rotationQ = Quaternion.EulerAngles(this.rotation.x, this.rotation.y, this.rotation.z);
		this.setScale(25);
	}
	
	public Matrix getWorld() {
		return this.world;
	}
	
	public void setBBColor(int color) {
		this.bbColor = color;
	}
	
	public float getHealth() {
		return this.health;
	}
	
	public Vec3 getVelocity() {
		return this.velocity;
	}
	
	public void setHealth(float health) {
		this.health = health;
	}
	
	public Ray getRay() {
		return this.bullet;
	}
	
	public void setRay(Ray r) {
		this.bullet = r;
	}
	
	public void update(float deltaTime) {
		
		//Register Acceleration Change
		this.velocity.add(this.acceleration._multiply(deltaTime));
		this.velocity.z = Maths.clamp(this.velocity.z, VEL_MIN, VEL_LIMIT);
		
		//Register Angular Acceleration Change
		this.angularVelocity.add(this.angularAcceleration._multiply(deltaTime));
		this.angularVelocity.subtract(this.angularVelocity._multiply(angularDrag));
		this.rotation.add(this.angularVelocity._multiply(deltaTime));
		this.rotation.x = Maths.wrapAngle(this.rotation.x);
		this.rotation.y = Maths.wrapAngle(this.rotation.y);
		this.rotation.z = Maths.wrapAngle(this.rotation.z);
		
		//Set Quaternion
		this.rotationQ.set(Quaternion.EulerAngles(this.rotation));
		
		//Get Rotation Matrix and Direction Vectors
		Matrix rotation = this.rotationQ.getRotationMatrix(4);
		this.forward.set(rotation._multiply(Plane.planeForward));
		this.right.set(rotation._multiply(Plane.planeRight));
		this.up.set(rotation._multiply(Plane.planeUp));
		
		//Update Position
		this.position.add(this.right._multiply(this.velocity.x * deltaTime));
		this.position.add(this.up._multiply(this.velocity.y * deltaTime));
		this.position.add(this.forward._multiply(this.velocity.z * deltaTime));
		
		
		//Apply Translation
		this.world = Matrix.Translation(this.position).multiply(rotation);
		
		this.pCooldown -= deltaTime;
		if(this.pCooldown <= 0) {
			this.ps.addParticle(pQuad, this.position._add(this.forward._multiply(this.pDelta.z)), Vec3.zero, 3.3f, this.forward._multiply(this.velocity.z* 0.8f), 1f);
			this.pCooldown = this.pInterval;
		}
		
	}
	
	public void render(Graphics3D g, Pipeline p) {
		Shader s = p.getShader();
		p.setDrawMode(Pipeline.LINE_MODE);
		p.setColorMode(Pipeline.RGB);
		
		System.out.println(this.world == null);
		s.setWorld(world.multiply(Matrix.Scale(4, this.scale)));
		s.setDefaultColor(PackedColor.White);
		p.draw(g, this.mesh);
		//s.setDefaultColor(this.bbColor);
		//p.draw(g, planeBoundingBox);
	}

}
