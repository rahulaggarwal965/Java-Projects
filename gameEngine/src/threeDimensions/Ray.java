package threeDimensions;

import math.Vec3;

public class Ray {
	
	private Vec3 rayOrigin;
	private Vec3 rayDirection;

	public Ray(Vec3 rayOrigin, Vec3 rayDirection) {
		this.rayOrigin = rayOrigin;
		this.rayDirection = rayDirection;
	}

	public Vec3 getRayOrigin() {
		return rayOrigin;
	}

	public Vec3 getRayDirection() {
		return rayDirection;
	}

}
