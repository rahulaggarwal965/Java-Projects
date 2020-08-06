package tests;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Vector;

import components.Terrain;
import gameEngine.GameEngine;
import gameEngine.GameLogic;
import math.Gradient;
import math.Maths;
import math.Noise;
import shaders.GeometryLightShader;
import threeDimensions.Camera;
import threeDimensions.Graphics3D;
import threeDimensions.Matrix;
import threeDimensions.Mesh;
import threeDimensions.Pipeline;
import threeDimensions.Vec2;
import threeDimensions.Vec3;

public class MeshTest implements GameLogic{
	
	private static final float ASPECT_RATIO = 1.777777f; //16:9
	private static final float FOV = 95.0f;
	private static final float CAMERA_SPEED = 3.0f;
	
	private boolean paused = false;
	
	private Pipeline pipeline;
	private GeometryLightShader s;
	private Camera camera;
	private Matrix projection;
	
	private Vec3 modelPosition0 = new Vec3(0, 0, 0);
	
	//Map Stuff
	private long seed = 2;
	private double persistence = 0.5;
	private double lacunarity = 2.0;
	private int octaves = 7;
	
	private Gradient gr;
	
	private int rows = 5, cols = 5;
	
	private Mesh terrain0;
	
	public static void main(String[] args) {
		MeshTest mt = new MeshTest();
		GameEngine gameEngine = new GameEngine(60, 60, 3, mt, 1280, 720, "MeshTest");
	}

	@Override
	public void init(Graphics2D g) throws Exception {
		this.camera = new Camera(0, 100, 0, 0, 0, 0);
		this.camera.rotation.x = (float) Math.PI/2;
		this.projection = Matrix.ProjectionFOV(4, FOV, ASPECT_RATIO, 0.05f, 1000f);
		
		this.s = new GeometryLightShader();
		this.pipeline = new Pipeline(this.s);
		
		this.gr = new Gradient(
				new Vec3[] {
						new Vec3(40, 89, 3),
						new Vec3(97, 99, 65),
						new Vec3(164, 109, 56),
						new Vec3(121, 67, 9),
						new Vec3(61, 56, 50),
						new Vec3(255, 255, 255)},
				new float[] {
						0f,
						0.24f,
						0.44f,
						0.65f,
						0.90f,
						0.96f});
		float[] map0 = generateMap(rows, cols, 0, 0);
		this.terrain0 = Terrain.getTriangles(20, 10, 20, map0, rows, cols, gr);
		
	}

	@Override
	public void input() {
		this.camera.velocity.x = 0;
		this.camera.velocity.y = 0;
		this.camera.velocity.z = 0; //TODO: make this a function
		
		//Movement
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_W]) {
			this.camera.velocity.z = CAMERA_SPEED;
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_S]) {
			this.camera.velocity.z = -CAMERA_SPEED;
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_A]) {
			this.camera.velocity.x = -CAMERA_SPEED;
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_D]) {
			this.camera.velocity.x = CAMERA_SPEED;
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_SPACE]) {
			this.camera.velocity.y = CAMERA_SPEED;
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_SHIFT]) {
			this.camera.velocity.y = -CAMERA_SPEED;
		}
		
		//Rotation
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_LEFT]) {
			this.camera.rotation.y = Maths.wrapAngle(this.camera.rotation.y - 0.05f);
			//this.modelPosition1.x -= 2f;
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_RIGHT]) {
			this.camera.rotation.y = Maths.wrapAngle(this.camera.rotation.y + 0.05f);
			//this.modelPosition1.x += 2f;
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_UP]) {
			this.camera.rotation.x = Maths.wrapAngle(this.camera.rotation.x - 0.05f);
			//this.modelPosition1.z += 2f;
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_DOWN]) {
			this.camera.rotation.x = Maths.wrapAngle(this.camera.rotation.x + 0.05f);
			//this.modelPosition1.z -= 2f;
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_Q]) {
			this.camera.rotation.z = Maths.wrapAngle(this.camera.rotation.z + 0.05f);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_E]) {
			this.camera.rotation.z = Maths.wrapAngle(this.camera.rotation.z - 0.05f);
		}
			
		if(GameEngine.keyboard.keysTyped[KeyEvent.VK_ESCAPE]) {
			this.paused = !this.paused;
			GameEngine.keyboard.keysTyped[KeyEvent.VK_ESCAPE] = false;
		}
		
	}

	@Override
	public void update(float deltaTime) {
		if(!paused) {
			this.camera.rotate();
			this.camera.translate(this.camera.velocity._multiply(deltaTime));
		}
	}

	@Override
	public void render(Graphics2D g) {
	}

	@Override
	public void render(Graphics3D g) {
		pipeline.beginFrame();
		
		Matrix view = this.camera.getViewMatrix();
		
		Matrix world = Matrix.Translation(this.modelPosition0);
		
		s.setWorld(world);
		s.setView(view);
		s.setProjection(this.projection);
		pipeline.draw(g, this.terrain0);
	}
	
	public float[] generateMap(int rows, int cols, float x, float y) {
		float[] map = new float[rows * cols];
		
//		float min = Float.MAX_VALUE;
//		float max = Float.MIN_VALUE;
		
		Random rand = new Random(this.seed);
		Vec2[] offsets = new Vec2[this.octaves];
		for (int i = 0; i < octaves; i++) {
			float offsetX = rand.nextInt(20000) - 10000 + x;
			float offsetY = rand.nextInt(20000) - 10000 + y;
			offsets[i] = new Vec2(offsetX, offsetY);
		}
		
		for (int j = 0; j < rows; j++) {
			for (int i = 0; i < cols; i++) {
				float h = (float) Noise.noise((float) i/cols, (float) j/rows, 0, this.octaves, this.persistence, this.lacunarity, offsets) * 0.5f + 0.5f;
				map[i + j*cols] = h * 2f;
//				if(h < min) min = h;
//				if(h > max) max = h;
			}
		}
//		for (int i = 0; i < map.length; i++) {
//			map[i] = Maths.map(map[i], min, max, 0, 1);
//		}
		return map;
	}
}
