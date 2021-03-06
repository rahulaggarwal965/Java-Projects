package tests;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.stream.IntStream;

import gameEngine.GameEngine;
import gameEngine.GameLogic;
import math.Gradient;
import math.Maths;
import math.Noise;
import threeDimensions.Graphics3D;
import threeDimensions.PackedColor;
import threeDimensions.Vec2;
import threeDimensions.Vec3;

public class PerlinNoiseTests implements GameLogic {

	public static void main(String[] args) {
		PerlinNoiseTests pnt = new PerlinNoiseTests();
		GameEngine gameEngine = new GameEngine(60, 60, 3, pnt, 800, 800, "Perlin Noise Tests");
	}
	
	private long seed = 0;
	
	private boolean update = false;
	private int[] pixels;
	private double scale = 100;
	private double persistence = 0.5;
	private double lacunarity = 2.0;
	private float x = 0, y = 0;
	private int octaves = 6;
	
	private Gradient gr;
	
	@Override
	public void init(Graphics2D g) throws Exception {
		this.pixels = new int[GameEngine.displayHeight * GameEngine.displayWidth];
				
		this.gr = new Gradient(
				new Vec3[] {
						new Vec3(0, 0, 0),
						new Vec3(102, 0, 0),
						new Vec3(247, 247, 91),
						new Vec3(255, 255, 255)},
				new float[] {
						0.4f,
						0.5f,
						0.65f,
						0.7f});
		this.updatePixels();
	}

	@Override
	public void input() {
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_LEFT]) {this.update = true; x -= 10/scale;}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_RIGHT]) {this.update = true; x += 10/scale;}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_UP]) {this.update = true; y -= 10/scale;}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_DOWN]) {this.update = true; y += 10/scale;}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_Q]) {this.update = true; persistence += 0.01;}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_A]) {this.update = true; persistence -= 0.01;}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_W]) {this.update = true; lacunarity += 0.01;}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_S]) {this.update = true; lacunarity -= 0.01;}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_Z]) {this.update = true; this.scale *= 1.1;}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_X]) {this.update = true; this.scale *= 0.9;}
		if(GameEngine.keyboard.keysTyped[KeyEvent.VK_E]) {GameEngine.keyboard.keysTyped[KeyEvent.VK_E] = false; this.update = true; this.octaves++;}
		if(GameEngine.keyboard.keysTyped[KeyEvent.VK_D]) {GameEngine.keyboard.keysTyped[KeyEvent.VK_D] = false; this.update = true; this.octaves--;}
		if(GameEngine.keyboard.keysTyped[KeyEvent.VK_R]) {GameEngine.keyboard.keysTyped[KeyEvent.VK_R] = false; this.update = true; this.seed--;}
		if(GameEngine.keyboard.keysTyped[KeyEvent.VK_F]) {GameEngine.keyboard.keysTyped[KeyEvent.VK_F] = false; this.update = true; this.seed++;}
		
		
	}

	@Override
	public void update(float deltaTime) {
		
		//CLAMP VALUES
		this.persistence = Maths.clamp(this.persistence, 0.0, 1.0);
		if(this.lacunarity < 1) this.lacunarity = 1;
		if(this.octaves < 1) this.octaves = 1;
		
		//GENERATE NOISE
		if(update) {
			System.out.printf("Octaves: %d, Persistence: %1.9f, Lacunarity: %5.5f, Scale: %9.4f\n", this.octaves, this.persistence, this.lacunarity, this.scale);
			this.update = false;
			this.updatePixels();
		}
	}

	@Override
	public void render(Graphics2D g) {

	}

	@Override
	public void render(Graphics3D g) {
		g.drawPixels(this.pixels);
	}
	
	public int updatePixel(int index, Vec2[] offsets) {
		float i = index % GameEngine.displayWidth - GameEngine.displayWidth/2;
		float j = index / GameEngine.displayHeight - GameEngine.displayHeight/2;
		double n =  Noise.noise(i/scale, j/scale, 0, this.octaves, this.persistence, this.lacunarity, offsets) * 0.5 + 0.5;
		//return this.gr.getPackedColor((float) n);
		return PackedColor.makeRGB(n, n, n);
	}
	
	public void updatePixels() {
		Random rand = new Random(this.seed);
		Vec2[] offsets = new Vec2[this.octaves];
		for (int i = 0; i < octaves; i++) {
			float offsetX = rand.nextInt(20000) - 10000 + x;
			float offsetY = rand.nextInt(20000) - 10000 + y;
			offsets[i] = new Vec2(offsetX, offsetY);
		}
		
		
		this.pixels = IntStream.range(0, this.pixels.length).parallel().map(x -> updatePixel(x, offsets)).toArray();
	}

}