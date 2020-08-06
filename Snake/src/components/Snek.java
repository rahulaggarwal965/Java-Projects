package components;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import gameEngine.GameEngine;
import threeDimensions.Graphics3D;
import threeDimensions.PackedColor;

public class Snek implements GameObject {
	
	public static final int SIZE = 20;
	public static final int HALF_SIZE = Snek.SIZE/2;
	
	private int id;
	
	private Point moveDirection = new Point(0, 0);
	
	//private ArrayList<Block> blocks = new ArrayList<Block>();
	private Block[] blockPool;
	private int blockCount = 0;
	
	private Block head;
	private Block tail;
	
	private int growth = 0;
	
	public Point getHeadPosition() {
		return this.head.getPosition();
	}
	
	public void setGrowth(int n) {
		this.growth = n;
	}
	
	public int getGrowth() {
		return this.growth;
	}
	
	public int getID() {
		return this.id;
	}
	
	public boolean pointInSnake(Point p) {
		for (int i = 0; i < blockCount; i++) {
			if(this.blockPool[i].getPosition().equals(p)) return true;
		}
		return false;
	}
	
	public void clear() {
		for (int i = 0; i < blockCount; i++) {
			this.blockPool[i].set(null, null);
		}
		this.blockCount = 0;
	}
	
	public void add(Block prev, int x, int y) {
		if(this.blockCount != this.blockPool.length) {
			this.blockPool[this.blockCount].set(prev, x, y);
			blockCount++;
		}
	}
	
	public Snek(int id) {
		this.blockPool = new Block[GameEngine.displayHeight/SIZE * GameEngine.displayWidth/SIZE];
		for (int i = 0; i < blockPool.length; i++) {
			this.blockPool[i] = new Block(null, null);
		}
		this.id = id;
	}

	@Override
	public void init() {
		this.clear();
		this.add(null, GameEngine.displayWidth/2 + HALF_SIZE, GameEngine.displayHeight/2 + HALF_SIZE);
		this.head = this.blockPool[0];
		this.tail = this.blockPool[0];
		this.moveDirection.setLocation(0, 0);
	}

	@Override
	public void input() {
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_LEFT]) {
			if(this.moveDirection.x == 0 || this.blockCount == 1)
				this.moveDirection.setLocation(-SIZE, 0);
		} else if(GameEngine.keyboard.keysPressed[KeyEvent.VK_RIGHT]) {
			if(this.moveDirection.x == 0 || this.blockCount == 1)
				this.moveDirection.setLocation(SIZE, 0);
		} else if(GameEngine.keyboard.keysPressed[KeyEvent.VK_DOWN]) {
			if(this.moveDirection.y == 0 || this.blockCount == 1)
			this.moveDirection.setLocation(0, SIZE);
		} else if(GameEngine.keyboard.keysPressed[KeyEvent.VK_UP]) {
			if(this.moveDirection.y == 0 || this.blockCount == 1)
				this.moveDirection.setLocation(0, -SIZE);
		}
	}

	@Override
	public void update(float deltaTime) {
		Point ph = this.head.getPosition();
		this.tail.setPosition(ph.x + moveDirection.x, ph.y + moveDirection.y);
		this.head.setPrev(this.tail);
		this.tail = this.tail.getPrev();
		this.head = this.head.getPrev();
		if(this.growth-- > 0) this.addPart(1);
	}
	
	public void addPart(int num) {
		int dx = 0;
		int dy = 0;
		
		Point p1 = this.tail.getPosition();
		
		if(this.blockCount == 1) {
			dx = -this.moveDirection.x;
			dy = -this.moveDirection.y;
		} else {
			Point p0 = this.tail.getPrev().getPosition();
			dx = p1.x - p0.x;
			dy = p1.y - p0.y;
		}
		
		for (int i = 1; i <= num; i++) {
			this.add(this.tail, p1.x + dx * i, p1.y + dy * i);
			this.tail = this.blockPool[blockCount - 1];
		}
		
	}
	
	public boolean checkDeathCollisions() {
		Point headPosition = this.head.getPosition();
		if(headPosition.x <= 0 || headPosition.x >= GameEngine.displayWidth|| headPosition.y <= 0 || headPosition.y >= GameEngine.displayHeight) return true;
				
		for (int i = 0; i < blockCount; i++) {
			if(this.blockPool[i] == this.head) continue;
			if(headPosition.equals(this.blockPool[i].getPosition())) return true;
		}
		return false;
	}

	@Override
	public void render(Graphics2D g) {
		
	}

	@Override
	public void render(Graphics3D g) {
		for (int i = 0; i < blockCount; i++) {
			Point p = this.blockPool[i].getPosition();
			g.fillRectangle(p.x - HALF_SIZE + 1, p.y-HALF_SIZE + 1, SIZE - 2, SIZE - 2, PackedColor.Red);
		}
	}

}
