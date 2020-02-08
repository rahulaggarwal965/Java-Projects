package shooter;
import java.awt.Point;
import java.util.Random;

import math.Gradient;
import math.Noise;
import threeDimensions.Mesh;
import threeDimensions.Vec2;
import threeDimensions.Vec3;

public class TerrainGenerator {

	public final int size = 61; // 60 has factors of 1, 2, 3, 4, 5, 6, 10, 12, 15

	// Map Stuff
	private long seed = 2;
	private double persistence = 0.5;
	private double lacunarity = 2.0;
	private int octaves = 7;
	private Vec2 offset = new Vec2(0, 0);
	private int scaleY = 50;

	// Colors
	Gradient gr = new Gradient(
			new Vec3[] { new Vec3(40, 89, 3), new Vec3(97, 99, 65), new Vec3(164, 109, 56),
					new Vec3(121, 67, 9), new Vec3(61, 56, 50), new Vec3(255, 255, 255) },
			new float[] { 0f, 0.24f, 0.44f, 0.65f, 0.90f, 0.96f });

	public float[] generateMap(Point position) {
		float[] map = new float[this.size * this.size];
		Random rand = new Random(this.seed);
		Vec2[] offsets = new Vec2[this.octaves];
		for (int i = 0; i < octaves; i++) {
			float offsetX = rand.nextInt(20000) - 10000 + this.offset.x + position.x;
			float offsetY = rand.nextInt(20000) - 10000 + this.offset.y + position.y;
			offsets[i] = new Vec2(offsetX, offsetY);
		}

		for (int j = 0; j < this.size; j++) {
			for (int i = 0; i < this.size; i++) {
				float h = (float) Noise.noise((float) i / (this.size - 1), (float) j / (this.size - 1), 0, this.octaves,
						this.persistence, this.lacunarity, offsets) * 0.5f + 0.5f;
				map[i + j * this.size] = h * 2f - .25f;
			}
		}
		return map;
	}

	public Mesh generateMesh(float[] map) {

		float topLeft = (this.size - 1) / -2f;

		float[] positions = new float[this.size * this.size * 3];
		float[] colors = new float[this.size * this.size * 3];
		for (int j = 0; j < this.size; j++) {
			for (int i = 0; i < this.size; i++) {
				int mapIndex = (i + j * this.size);
				int index = mapIndex * 3;
				positions[index] = i + topLeft;
				positions[index + 1] = map[mapIndex] * scaleY;
				positions[index + 2] = j + topLeft;
				Vec3 color = this.gr.getColor(map[mapIndex]);
				colors[index] = color.x;
				colors[index + 1] = color.y;
				colors[index + 2] = color.z;
			}
		}
		int[] indices = new int[(this.size - 1) * (this.size - 1) * 6];
		int a = 0;
		for (int j = 0; j < this.size - 1; j++) {
			for (int i = 0; i < this.size - 1; i++) {
				int index = i + j * this.size;
				// First Triangle
				indices[a++] = index;// i
				indices[a++] = index + this.size; // i + cols + j*cols
				indices[a++] = index + 1; // i + 1 + j*cols

				// Second Triangle
				indices[a++] = index + 1;// i + 1 + j*cols
				indices[a++] = index + this.size;// i + cols + j*cols
				indices[a++] = index + 1 + this.size;// i + 1 + cols + j*cols
			}
		}

		return new Mesh(indices, new int[] { 3, 3 }, positions, colors);
	}

}
