package components;

import threeDimensions.Graphics3D;
import threeDimensions.ParticleSystem;
import threeDimensions.Pipeline;
import threeDimensions.Vec3;

public class EnemyFactory {
	
	private Player player;
	private ParticleSystem ps;
	private Enemy[] enemyPool;

	private int enemyCount;

	public EnemyFactory(Player player, ParticleSystem ps, int num) {
		this.ps = ps;
		this.player = player;
		this.enemyPool = new Enemy[num];
		for (int i = 0; i < num; i++) {
			this.enemyPool[i] = new Enemy(ps, player);
			this.enemyPool[i].init();
		}
		this.enemyCount = 0;
	}
	
	public void clear() {
		for (int i = 0; i < this.enemyCount; i++) {
			this.enemyPool[i].setHealth(0.0f);
		}
		this.enemyCount = 0;
	}
	
	public void addEnemy(Vec3 position, Vec3 rotation) {
		if(this.enemyCount != this.enemyPool.length) {
			Enemy e = this.enemyPool[enemyCount];
			e.setHealth(100.0f);
			e.setPosition(position.x, position.y, position.z);
			e.setRotation(rotation.x, rotation.y, rotation.z);
			this.enemyCount++;
		}
	}
	
	public void update(float deltaTime) {
		for (int i = 0; i < this.enemyCount; i++) {
			if(this.enemyPool[i].getHealth() <= 0) {
				Enemy temp = this.enemyPool[i];
				this.enemyPool[i] = this.enemyPool[this.enemyCount - 1];
				this.enemyPool[this.enemyCount - 1] = temp;
				this.enemyCount--;
			} else {
				this.enemyPool[i].update(deltaTime);
			}
		}
	}
	
	public void render(Graphics3D g, Pipeline p){
		for (int i = 0; i < this.enemyCount; i++) {
			this.enemyPool[i].render(g, p);
		}
	}

	public Enemy[] getEnemyPool() {
		return enemyPool;
	}

	public int getEnemyCount() {
		return enemyCount;
	}

}
