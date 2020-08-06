package threeDimensions;

import math.Matrix;
import math.Vec3;
import math.Vec4;

public class Camera {

	public final static Vec4 worldRight = new Vec4(1, 0, 0, 0);
	public final static Vec4 worldUp = new Vec4(0, 1, 0, 0);
	public final static Vec4 worldForward = new Vec4(0, 0, 1, 0);
	
	public Vec3 position;
	public Vec3 velocity;
	public Vec3 rotation;
	private Matrix rotationInverse = Matrix.Identity(4);
	private Vec4 right;
	private Vec4 up;
	private Vec4 forward;

	public Camera(float x, float y, float z, float vx, float vy, float vz) {
		//position
		this.position = new Vec3(x, y, z);
		this.velocity = new Vec3(vx, vy, vz);
		
		//rotation
		this.rotation = new Vec3(0, 0, 0);
		this.right = new Vec4(worldRight);
		this.up = new Vec4(worldUp);
		this.forward = new Vec4(worldForward);
	}
	
	public Vec4 getForward() {
		return this.forward;
	}
	
	
	//Factor Out
	public void rotate() {
		this.rotationInverse = Matrix.rotationX(4, -this.rotation.x)
				.multiply(Matrix.rotationY(4, -this.rotation.y))
				.multiply(Matrix.rotationZ(4, -this.rotation.z));
		Matrix rotation = this.rotationInverse.transpose(); //orthographic inverse
		this.right = rotation._multiply(worldRight);
		this.up = rotation._multiply(worldUp);
		this.forward = rotation._multiply(worldForward);
	}
	
	//Factor Out
	public void translate(Vec3 delta) {
		this.position.add(this.right._multiply(delta.x));
		this.position.add(this.up._multiply(delta.y));
		this.position.add(this.forward._multiply(delta.z));
	}
	
	public void setPosition(Vec3 position) {
		this.position.set(position);
	}
	
	public void setPosition(float x, float y, float z) {
		this.position.set(x, y, z);
	}
	
	public void setRotation(Vec3 rotation) {
		this.setRotation(rotation.x, rotation.y, rotation.z);
	}
	
	public void setRotation(float x, float y, float z) {
		this.rotation.set(x, y, z);
		this.rotationInverse = Matrix.rotationX(4, -this.rotation.x)
				.multiply(Matrix.rotationY(4, -this.rotation.y))
				.multiply(Matrix.rotationZ(4, -this.rotation.z));
		Matrix rotationMatrix = this.rotationInverse.transpose(); //orthographic inverse
		this.right = rotationMatrix._multiply(worldRight);
		this.up = rotationMatrix._multiply(worldUp);
		this.forward = rotationMatrix._multiply(worldForward);
	}
		
	public Matrix getViewMatrix() {
		return this.rotationInverse.multiply(Matrix.Translation(this.position._negate()));
	}

}
