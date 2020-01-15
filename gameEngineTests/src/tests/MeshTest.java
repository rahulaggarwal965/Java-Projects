package tests;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;

import components.Cube;
import components.Mesh;
import gameEngine.GameEngine;
import gameEngine.GameLogic;
import math.Maths;
import math.Noise;
import threeDimensions.Camera;
import threeDimensions.Graphics3D;
import threeDimensions.IndexedTriangleList;
import threeDimensions.Matrix;
import threeDimensions.Pipeline;
import threeDimensions.Vec2;
import threeDimensions.Vec3;
import threeDimensions.Vertex;
import threeDimensions.VertexOut;

public class MeshTest implements GameLogic{
	
	private static final float ASPECT_RATIO = 1.777777f; //16:9
	private static final float FOV = 95.0f;
	private static final float CAMERA_SPEED = 1.0f;
	
	private boolean paused = false;
	
	private Pipeline pipeline;
	private Camera camera;
	private Matrix projection;
	
	private Vec3 modelPosition = new Vec3(0, 0, 2);
	private float tx = 0, ty = 0, tz = 0;
	
	//Map Stuff
	private long seed = 0;
	private double scale = 100;
	private double persistence = 0.5;
	private double lacunarity = 2.0;
	private int octaves = 6;
	
	private int rows = 100, cols = 100;
	
	private IndexedTriangleList<Vertex> terrain;
	
	public static void main(String[] args) {
		MeshTest mt = new MeshTest();
		GameEngine gameEngine = new GameEngine(60, 60, 3, mt, 1280, 720, "MeshTest");
	}

	@Override
	public void init(Graphics2D g) throws Exception {
		this.camera = new Camera(0, 0, 0, 0, 0, 0);
		this.projection = Matrix.ProjectionFOV(4, FOV, ASPECT_RATIO, 0.1f, 10f);
		
		this.pipeline = new Pipeline();
		pipeline.fragShader.setTexture(GameEngine.loadImage("/Users/infinity/Desktop/SauronEye.png"));
		
		float[] map = generateMap(rows, cols);
		this.terrain = Mesh.getTexturedTriangle(rows, cols, map);
		/*System.out.println(this.terrain.indices.length);
		for (int i = 0; i < this.terrain.indices.length / 3; i++) {
			System.out.printf("%d, %d, %d\n", this.terrain.indices[i*3], this.terrain.indices[i*3 + 1], this.terrain.indices[i*3 + 2]);
		}*/
		
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
			this.camera.rotationAngles.y = Maths.wrapAngle(this.camera.rotationAngles.y - 0.05f);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_RIGHT]) {
			this.camera.rotationAngles.y = Maths.wrapAngle(this.camera.rotationAngles.y + 0.05f);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_UP]) {
			this.camera.rotationAngles.x = Maths.wrapAngle(this.camera.rotationAngles.x - 0.05f);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_DOWN]) {
			this.camera.rotationAngles.x = Maths.wrapAngle(this.camera.rotationAngles.x + 0.05f);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_Q]) {
			this.camera.rotationAngles.z = Maths.wrapAngle(this.camera.rotationAngles.z + 0.05f);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_E]) {
			this.camera.rotationAngles.z = Maths.wrapAngle(this.camera.rotationAngles.z - 0.05f);
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
		
		Matrix view = this.camera.rotationInverse.multiply(Matrix.Translation(this.camera.position._negate()));
		
		Matrix world = Matrix.Translation(this.modelPosition)
				.multiply(Matrix.rotationZ(4, tz))
				.multiply(Matrix.rotationY(4, ty))
				.multiply(Matrix.rotationX(4, tx));

		pipeline.setWorld(world);
		pipeline.setView(view);
		pipeline.setProjection(this.projection);
		pipeline.draw(g, this.terrain);
	}
	
	public float[] generateMap(int rows, int cols) {
		float[] map = new float[rows * cols];
		
		Random rand = new Random(this.seed);
		Vec2[] offsets = new Vec2[this.octaves];
		for (int i = 0; i < octaves; i++) {
			float offsetX = rand.nextInt(20000) - 10000;
			float offsetY = rand.nextInt(20000) - 10000;
			offsets[i] = new Vec2(offsetX, offsetY);
		}
		
		for (int j = 0; j < rows; j++) {
			for (int i = 0; i < cols; i++) {
				map[i + j*cols] = (float) Noise.noise(i/scale, j/scale, 0, this.octaves, this.persistence, this.lacunarity, offsets) * 0.5f + 0.5f;
			}
		}
		return map;
	}
}
