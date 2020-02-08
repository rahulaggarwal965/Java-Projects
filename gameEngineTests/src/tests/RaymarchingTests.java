package tests;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.stream.IntStream;

import gameEngine.GameEngine;
import gameEngine.GameLogic;
import math.Maths;
import threeDimensions.Graphics3D;
import threeDimensions.Matrix;
import threeDimensions.PackedColor;
import threeDimensions.Vec2;
import threeDimensions.Vec3;

//NEEDS FURTHER REFACTORING
//TODO: Encapsulate Camera, Encapsulate Shapes, Maybe: Encapsulate RayMarching
public class RaymarchingTests implements GameLogic {
	
	private final int MAX_STEPS = 100;
	private final float MAX_DIST = 100f;
	private final float SURFACE_DIST = 0.01f;
	
	private Vec2 resolution;
	private int[] pixels;
	
	//Epsilon Vectors
	private Vec3 e1 = new Vec3(.01f, 0, 0);
	private Vec3 e2 = new Vec3(0, .01f, 0);
	private Vec3 e3 = new Vec3(0, 0, .01f);
	
	//Camera Stuff
	private final float ZOOM = 1f;
	private Vec3 cameraPosition = new Vec3(0, 1, 0);
	private Vec3 lookAt = new Vec3(0, 1, 4);
	private Vec3 center;
	private Vec3 forward;
	private Vec3 right;
	private Vec3 up;
	
	//Shapes
	private Vec3 spherePosition = new Vec3(0, 1, 4);
	private Vec3 boxDimensions = new Vec3(0.5f, 0.5f, 0.5f);
	private Vec3 boxPosition = new Vec3(0, 1, 4);
	
	private Vec3 lightPosition = new Vec3(0, 3, 2);
	private float lightVelocityY = 0.01f;
	
	private Vec3 transformedLightPosition = new Vec3(0, 0, 0);
	
	public static void main(String[] args) {
		try {
			RaymarchingTests rt = new RaymarchingTests();
			GameEngine gameEngine = new GameEngine(60, 60, 3, rt, 800, 600, "Raymarching");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public float boxDistance(Vec3 p, Vec3 s) {
		return p._abs()._subtract(s)._max(0).mag();
	}
	
	public float distanceToScene(Vec3 point) {
		//Get Distance to Each object
		float sphereDistance = (point._subtract(spherePosition)).mag() - 1f;
		float boxDistance = boxDistance(point._subtract(boxPosition), boxDimensions);
		float planeDistance = point.y;
		
		return Math.min(planeDistance, boxDistance);
	}
	
	public float rayMarch(Vec3 rayOrigin, Vec3 rayDirection) {
		float rayDistance = 0;
		for (int i = 0; i < MAX_STEPS; i++) {
			float sceneDistance = distanceToScene(rayOrigin._add(rayDirection._multiply(rayDistance)));
			rayDistance += sceneDistance;
			if(rayDistance > MAX_DIST || sceneDistance < SURFACE_DIST) break;
		}
		return rayDistance;
	}
	
	public Vec3 getNormal(Vec3 point) {
		float distance = distanceToScene(point);
		Vec3 surfaceNormal = new Vec3(
				distanceToScene(point._subtract(e1)),
				distanceToScene(point._subtract(e2)),
				distanceToScene(point._subtract(e3))
				)._negate()._add(distance).getNormalized();
		return surfaceNormal;
	}
	
	public float getLight(Vec3 point) {
		Vec3 lightNormal = this.transformedLightPosition._subtract(point).getNormalized();
		Vec3 surfaceNormal = getNormal(point);
		
		float diffuse = Maths.clamp(surfaceNormal.dot(lightNormal), 0.0f, 1.0f); //Ambient
		
		//Shadow
		float distanceToLight = rayMarch(point._add(surfaceNormal._multiply(SURFACE_DIST*2)), lightNormal);
		if (distanceToLight < this.transformedLightPosition._subtract(point).mag()) diffuse *= 0.1;
		
		return diffuse;
	}
	
	public int shade(int x) {
		int i = x % GameEngine.displayWidth;
		int j = x / GameEngine.displayWidth;
		Vec2 fragCoord = new Vec2(i, GameEngine.displayHeight - 1 - j);
		Vec2 uv = fragCoord._subtract(this.resolution._multiply(0.5f))._divide(this.resolution);
		uv.x *= (float) GameEngine.displayWidth/GameEngine.displayHeight;
		
		//Vec3 rayDirection = new Vec3(uv.x, uv.y, 1).getNormalized();
		Vec3 rayDirection = this.center._add(this.right._multiply(uv.x))._add(this.up._multiply(uv.y))._subtract(cameraPosition).getNormalized();
		
		float distance = rayMarch(cameraPosition, rayDirection);
	
		Vec3 point = cameraPosition._add(rayDirection._multiply(distance));
		
		float diffuse = getLight(point);
		
		Vec3 col = new Vec3(diffuse, diffuse, diffuse);
		
		return PackedColor.makeRGB(col._multiply(255));
	}
	

	@Override
	public void init(Graphics2D g) throws Exception {
		// TODO Auto-generated method stub
		
		this.forward = new Vec3(0, 0, 1);
		this.right = new Vec3(1, 0, 0);
		this.up = new Vec3(0, 1, 0);
		
		this.center = this.cameraPosition._add(this.forward._multiply(ZOOM));
		
		this.resolution = new Vec2(GameEngine.displayWidth, GameEngine.displayHeight);
		this.pixels = new int[GameEngine.displayWidth * GameEngine.displayHeight];
		for (int i = 0; i < pixels.length; i++) pixels[i] = PackedColor.Black;
	}

	@Override
	public void input() {
		// TODO Auto-generated method stub
		Vec2 m = new Vec2(GameEngine.mouse.position.x, GameEngine.mouse.position.y);
		float mX = Maths.map(m.x, 0, GameEngine.displayWidth - 1, -1, 1);
		float mY = Maths.map(m.y, 0, GameEngine.displayHeight - 1, 1, -1);
		
		float l = Maths.clamp(m._subtract(this.resolution._divide(2)).mag(), 0f, 300f);
		
		
		float mZ = Maths.map(l, 0, GameEngine.displayWidth/2, 4, 1f);
		System.out.printf("mY: %2.9f, mZ: %2.9f\n", mY, mZ);
		
		
		this.lookAt = new Vec3(0, mY, mZ);
		
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_W]) {
			this.cameraPosition.add(this.forward._multiply(0.1f));
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_A]) {
			this.cameraPosition.subtract(this.right._multiply(0.1f));
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_S]) {
			this.cameraPosition.subtract(this.forward._multiply(0.1f));
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_D]) {
			this.cameraPosition.add(this.right._multiply(0.1f));
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_SPACE]) {
			this.cameraPosition.add(this.up._multiply(0.1f));
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_SHIFT]) {
			this.cameraPosition.subtract(this.up._multiply(0.1f));
		}
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		
		//Camera stuff
		
		this.forward = this.lookAt._subtract(cameraPosition).getNormalized();
		this.right = new Vec3(0, 1, 0).cross(forward);
		this.up = forward.cross(right);
		this.center = this.cameraPosition._add(this.forward._multiply(ZOOM));
		
		this.lightVelocityY = Maths.wrapAngle(lightVelocityY + 0.01f);
		this.transformedLightPosition = Matrix.rotationY(3, lightVelocityY)._multiply(this.lightPosition)._add(new Vec3(0, 0, 2));
		
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics3D g) {
		// TODO Auto-generated method stub
		this.pixels = IntStream.range(0, this.pixels.length).parallel().map(x -> shade(x)).toArray();
		g.drawPixels(this.pixels);
	}

}
