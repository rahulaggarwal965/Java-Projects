package threeDimensions;

public class Camera {

	public final static Vec4 worldRight = new Vec4(1, 0, 0, 0);
	public final static Vec4 worldUp = new Vec4(0, 1, 0, 0);
	public final static Vec4 worldForward = new Vec4(0, 0, 1, 0);
	
	public Vec3 position;
	public Vec3 velocity;
	public Vec3 rotationAngles;
	public Matrix rotationInverse = Matrix.Identity(4);
	public Vec4 right;
	public Vec4 up;
	public Vec4 forward;

	public Camera(float x, float y, float z, float vx, float vy, float vz) {
		//position
		this.position = new Vec3(x, y, z);
		this.velocity = new Vec3(vx, vy, vz);
		
		//rotation
		this.rotationAngles = new Vec3(0, 0, 0);
		this.right = new Vec4(worldRight);
		this.up = new Vec4(worldUp);
		this.forward = new Vec4(worldForward);
	}
	
	public void rotate() {
		this.rotationInverse = Matrix.rotationX(4, -this.rotationAngles.x)
				.multiply(Matrix.rotationY(4, -this.rotationAngles.y))
				.multiply(Matrix.rotationZ(4, -this.rotationAngles.z));
		Matrix rotation = this.rotationInverse.transpose(); //orthographic inverse
		this.right = rotation._multiply(worldRight);
		this.up = rotation._multiply(worldUp);
		this.forward = rotation._multiply(worldForward);
	}
	
	public void translate(Vec3 delta) {
		this.position.add(this.right._multiply(delta.x));
		this.position.add(this.up._multiply(delta.y));
		this.position.add(this.forward._multiply(delta.z));
	}

}
