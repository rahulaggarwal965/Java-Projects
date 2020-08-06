package components;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import math.Maths;
import shaders.Shader;
import threeDimensions.Graphics3D;
import threeDimensions.Matrix;
import threeDimensions.Mesh;
import threeDimensions.Pipeline;
import threeDimensions.Vec3;

public class EndlessTerrain {

	private HashMap<Point, TerrainChunk> terrainChunkMap = new HashMap<Point, TerrainChunk>();
	private ArrayList<TerrainChunk> visibleChunks = new ArrayList<TerrainChunk>();

	private final float maxViewDistance = 120;
	private final float squaredMaxViewDistance = maxViewDistance * maxViewDistance;
	public Vec3 playerPosition;
	public static TerrainGenerator terrainGenerator;
	private int chunkSize, chunkMinMax;

	public Pipeline p;
	public Shader s;

	public EndlessTerrain(Vec3 playerPosition, TerrainGenerator terrainGenerator, Pipeline p, Shader s) {
		this.playerPosition = playerPosition;
		EndlessTerrain.terrainGenerator = terrainGenerator;
		this.chunkSize = terrainGenerator.size - 1;
		this.chunkMinMax = Math.round(this.maxViewDistance / this.chunkSize);

		this.p = p;
		this.s = s;

	}

	public void update(float deltaTime) {
		// System.out.println("hello");
		for (TerrainChunk c : visibleChunks) {
			c.setVisible(false);
		}
		visibleChunks.clear();
//		
		int cx = Math.round(playerPosition.x / chunkSize);
		int cz = Math.round(playerPosition.z / chunkSize);

		for (int j = -chunkMinMax; j <= chunkMinMax; j++) {
			for (int i = -chunkMinMax; i <= chunkMinMax; i++) {
				Point chunkC = new Point(cx + i, cz + j);

				if (terrainChunkMap.containsKey(chunkC)) {
					TerrainChunk c = terrainChunkMap.get(chunkC);
					c.update();
					if (c.getVisible()) {
						visibleChunks.add(c);
					}
				} else {
					terrainChunkMap.put(chunkC, new TerrainChunk(chunkC, chunkSize));
				}
			}
		}
	}

	public void render(Graphics3D g) {
		for (TerrainChunk c : visibleChunks) {
			c.render(g);
		}
	}

	public class TerrainChunk {
		private Mesh mesh;
		private Vec3 position;
		private int size;
		private boolean visible = false;

		public TerrainChunk(Point position, int size) {
			this.size = size;
			this.position = new Vec3(position.x * size, 0, position.y * size);
			this.mesh = terrainGenerator.generateMesh(terrainGenerator.generateMap(position));
		}

		public void update() {
			float dist = Maths.squaredDistanceToRect(playerPosition.x, playerPosition.z,
					this.position.x, this.position.z, this.size, this.size);
			this.visible = dist <= squaredMaxViewDistance;
		}

		public void setVisible(boolean visible) {
			this.visible = visible;
		}

		public boolean getVisible() {
			return this.visible;
		}

		public void render(Graphics3D g) {
			s.setWorld(Matrix.Translation(this.position));
			p.draw(g, this.mesh);
		}

	}

}
