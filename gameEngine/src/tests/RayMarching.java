package tests;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import gameEngine.GameEngine;
import gameEngine.IGameLogic;
import libraries.Maths;
import threeDimensions.Graphics3D;
import threeDimensions.Mat3;
import threeDimensions.PackedColor;
import threeDimensions.Vec2;
import threeDimensions.Vec3;

public class RayMarching implements IGameLogic{

	private Vec2 resolution;
	private final int MAX_STEPS = 100;
	private final float MAX_DIST = 100f;
	private final float SURFACE_DIST = 0.01f;
	
	private float sphereWidth = 0.05f;
	private Vec3 sphere = new Vec3(0f, 1f, 4);
	private Vec3 box = new Vec3(0.05f, 0.25f, 0.25f);
	private Vec3 box1Pos = new Vec3(-1.5f, 1f, 4);
	private Vec3 box2Pos = new Vec3(1.5f, 1f, 4f);
	private float box1Dy = 0, box1Dz = 0;
	private float box2Dy = 0, box2Dz = 0;
	private Vec3 rayOrigin = new Vec3(0, 1, 0);
	
	private Vec3 lightPosition = new Vec3(0, 1, 4);
	
	//Epsilon Vectors
	private Vec3 e1 = new Vec3(.01f, 0, 0);
	private Vec3 e2 = new Vec3(0, .01f, 0);
	private Vec3 e3 = new Vec3(0, 0, .01f);
	
	float ty = 0.01f;
	
	public float boxDistance(Vec3 p, Vec3 s) {
		
		return p._abs()._subtract(s)._max(0).len();
	}
	
	public float getDist(Vec3 p) {

		float box1Dist = boxDistance(p._subtract(box1Pos), this.box);
		float box2Dist = boxDistance(p._subtract(box2Pos), this.box);
		float sphereDist = (p._subtract(this.sphere)).len() - this.sphereWidth;
		
		return Math.min(Math.min(box1Dist, box2Dist), sphereDist);
	}
	
	public float rayMarch(Vec3 ro, Vec3 rd) {
		float dO = 0;
		for (int i = 0; i < MAX_STEPS; i++) {
			Vec3 p = ro._add(rd._multiply(dO));
			float dS = getDist(p);
			dO += dS;
			if(dO > MAX_DIST || dS < SURFACE_DIST) break;
		}
		
		return dO;
	}
	
	public Vec3 getNormal(Vec3 p) {
		float d = getDist(p);
		Vec3 n = new Vec3(
				getDist(p._subtract(e1)),
				getDist(p._subtract(e2)),
				getDist(p._subtract(e3))
				)._negate()._add(d);
		return n.getNormalized();
	}
	
	public float getLight(Vec3 p) {
		Vec3 l = this.lightPosition._subtract(p).getNormalized();
		Vec3 n = getNormal(p);
		
		float dif = Maths.clamp(n.dot(l), 0.0f, 1.0f);
		float d = rayMarch(p._add(n._multiply(SURFACE_DIST*2)), l);
		if(d < lightPosition._subtract(p).len()) dif *= 0.1f;
		return dif;
	}
	
	@Override
	public void init(Graphics g) throws Exception {
		// TODO Auto-generated method stub
		resolution = new Vec2(GameEngine.displayWidth, GameEngine.displayHeight);
	}

	@Override
	public void input() {
		box1Dy = 0; box1Dz = 0;
		box2Dy = 0; box2Dz = 0;
		// TODO Auto-generated method stub
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_W]) {
			box1Dy = .01f;
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_S]) {
			box1Dy = -.01f;
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_A]) {
			box1Dz = .01f;
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_D]) {
			box1Dz = -.01f;
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_UP]) {
			box2Dy = .01f;
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_DOWN]) {
			box2Dy = -.01f;
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_LEFT]) {
			box2Dz = .01f;
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_RIGHT]) {
			box2Dz = -.01f;
		}
	}

	@Override
	public void update(float interval) {
		// TODO Auto-generated method stub
		box1Pos.y += box1Dy;
		box1Pos.z += box1Dz;
		box2Pos.y += box2Dy;
		box2Pos.z += box2Dz;
		final Mat3 rotY = Mat3.rotationY(ty);
		rotY.multiply(lightPosition);
		//IntStre
	}

	@Override
	public void render(Graphics2D g2d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render3D(Graphics3D g3d) {
		// TODO Auto-generated method stub
		for (int j = 0; j < GameEngine.displayHeight; j++ ) {
			for (int i = 0; i < GameEngine.displayWidth; i++) {
				Vec2 fragCoord = new Vec2(i, GameEngine.displayHeight - 1 - j);
				Vec2 uv = fragCoord._subtract(this.resolution._multiply(0.5f))._divide(this.resolution);
				
				Vec3 rayDirection = new Vec3(uv.x, uv.y, 1).getNormalized();
				
				float d = rayMarch(rayOrigin, rayDirection);
				
				Vec3 p = rayOrigin._add(rayDirection._multiply(d));
				
				float dif = getLight(p);
				
				Vec3 col = new Vec3(dif, dif, dif);
				
				int color = PackedColor.makeRGB(col._multiply(255));
				g3d.drawPixel(i, j, color);
			}
		}
	}

}
