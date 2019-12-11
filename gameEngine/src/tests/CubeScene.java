package tests;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import components.Cube;
import gameEngine.GameEngine;
import gameEngine.IGameLogic;
import libraries.Maths;
import threeDimensions.Graphics3D;
import threeDimensions.IndexedTriangleList;
import threeDimensions.Mat2;
import threeDimensions.Mat3;
import threeDimensions.Pipeline;
import threeDimensions.Vec3;
import threeDimensions.Vertex;

public class CubeScene implements IGameLogic {
	
	private Pipeline pipeline;
	private IndexedTriangleList<Vertex> iTriList;
	private float tx = 0.0f, ty = 0.0f, tz = 0.0f, offsetZ = 2.0f;
	
	public static void main(String[] args) {
		CubeScene cs = new CubeScene();
		GameEngine gameEngine = new GameEngine(60, 60, 3, cs, 800, 800, "CubeScene", true);
	}
	
	@Override
	public void init(Graphics g) throws Exception {
		// TODO Auto-generated method stub
		this.pipeline = new Pipeline();
		this.iTriList = Cube.getTexturedTriangles(1.0f, 1);
		pipeline.fragShader.setTexture(GameEngine.loadImage("/Users/infinity/Desktop/SauronEye.png"));
	}

	@Override
	public void input() {
		// TODO Auto-generated method stub
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_Q]) {
			tx = Maths.wrapAngle(tx+0.05f);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_W]) {
			ty = Maths.wrapAngle(ty+0.05f);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_E]) {
			tz = Maths.wrapAngle(tz+0.05f);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_A]) {
			tx = Maths.wrapAngle(tx-0.05f);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_S]) {
			ty = Maths.wrapAngle(ty-0.05f);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_D]) {
			tz = Maths.wrapAngle(tz-0.05f);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_R]) {
			offsetZ += 0.05f;
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_F]) {
			offsetZ -= 0.05f;
		}
		
	}

	@Override
	public void update(float interval) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(Graphics2D g2d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render3D(Graphics3D g3d) {
		pipeline.beginFrame();
		
		Mat3 rot = Mat3.rotationX(tx).multiply(Mat3.rotationY(ty)).multiply(Mat3.rotationZ(tz));
		//Mat3 rot = Mat3.rotationY(ty).multiply(Mat3.rotationX(tx));
		pipeline.setRotation(rot);
		
		pipeline.setTranslation(new Vec3(0.0f, 0.0f, offsetZ));
		pipeline.draw(g3d, this.iTriList);
		
	}
}
