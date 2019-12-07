package compSciPackage;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class CarDisplay extends Canvas implements Runnable{
	
	public static final int UPDATES_PER_SEC = 30;
	public static final int FRAMES_PER_SEC = 30;
	
	private final double[][] RESET_ARR = {{1, 0, 400},{0, 1, 280},{0, 0, 1}};
	private final double[][] ORIGIN_ARR = {{1, 0, -400},{0, 1, -280},{0, 0, 1}};
	
	private final Matrix RESET_MAT = new Matrix(RESET_ARR);
	private final Matrix ORIGIN_MAT = new Matrix(ORIGIN_ARR);

	private Matrix lastTransform = null;
	
	private Matrix[] partMatrix;
	private final Timer timer;
	
	private int numBuffers;
	private BufferStrategy strategy;
	private double angle = 1;
	private double x = 0;
	private double y = 0;
	private double scale = 1;
	
	
	public CarDisplay(int numBuffers) {
		//Create Timer Instance
		this.timer = new Timer();
		this.numBuffers = numBuffers;
		
		//Create the Car Matrix
		this.partMatrix = new Matrix[CarXData.xData.length];
		for (int i = 0; i < this.partMatrix.length; i++) {
			double[][] part = new double[3][CarXData.xData[i].length];
			part[0] = CarXData.xData[i];
			part[1] = CarYData.yData[i];
			for (int j = 0; j < part[2].length; j++) {
				part[2][j] = 1;
			}
			this.partMatrix[i] = new Matrix(part);
		}
	}
	
	//Box Car
	private void drawSimpleCar(Graphics win, int x, int y, int scale) {
		win.setColor(Color.RED);
		win.fillRect(x+24*scale, y-12*scale, 48*scale, 24*scale);
		win.fillArc(x+12*scale, y-2*scale, 24*scale, 14*2*scale, 180, -90);
		win.fillArc(x+60*scale, y-2*scale, 36*scale, 14*2*scale, 90, -90);
		win.setColor(Color.BLACK);
		win.fillRect(x + 27*scale, y - 9*scale, 20*scale, 8*scale);
		win.fillRect(x + 49*scale, y - 9*scale, 20*scale, 8*scale);
		win.fillOval(x+18*scale, y+6*scale, 12*scale, 12*scale);
		win.fillOval(x+66*scale, y+6*scale, 12*scale, 12*scale);
		win.setColor(Color.gray);
		win.fillOval(x+22*scale, y+10*scale, 4*scale, 4*scale);
		win.fillOval(x+70*scale, y+10*scale, 4*scale, 4*scale);
	}
	
	//Complex Car
	private void drawComplexCar(Graphics win) {
		win.setColor(Color.DARK_GRAY);
		for (int i = 0; i < partMatrix.length; i++) {
			int[] xPoints = partMatrix[i].getRow(0);
			int[] yPoints = partMatrix[i].getRow(1);
			win.drawPolygon(xPoints, yPoints, xPoints.length);
		}
	}
	
	//If we already have a transform matrix then use that, otherwise create a new one
	private Matrix transform(Matrix m, double angle, double x, double y, double scale) {
		if(this.angle == angle && this.x == x && this.y == y && this.scale == scale && lastTransform != null) {
			return this.RESET_MAT.multiply(lastTransform.multiply(this.ORIGIN_MAT.multiply(m)));
		} else {
			double rad = Math.toRadians(angle);
			double[][] transformArr = {
					{scale*Math.cos(rad), scale*Math.sin(rad), x},
					{-scale*Math.sin(rad), scale*Math.cos(rad), y},
					{0, 0, 1}
					};
			this.lastTransform = new Matrix(transformArr);
			return this.RESET_MAT.multiply(lastTransform.multiply(this.ORIGIN_MAT.multiply(m)));
		}
	}
	
	private void updateCar() {
		for (int i = 0; i < this.partMatrix.length; i++) {
			this.partMatrix[i] = transform(this.partMatrix[i], this.angle, this.x, this.y, this.scale);
		}
	}
	
	//If we spend less time then wait to sync up to frame rate
	private void sync() {
		float loopSlot = 1f / CarDisplay.FRAMES_PER_SEC;
		double endTime = timer.getLastLoopTime() + loopSlot;
		while(timer.getTime() < endTime) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException ie) {}
		}
	}
	
	private void render() {
		Graphics g = this.strategy.getDrawGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, 1000, 800);
		drawComplexCar(g);
		g.dispose();
		this.strategy.show();
	}
	
	//Main game loop where updates happen every interval
	private void loop() {
		float elapsedTime;
		float accumulator = 0f;
		float interval = 1f / CarDisplay.UPDATES_PER_SEC;
		
		boolean running = true;
		while(running) {
			elapsedTime = this.timer.getElapsedTime();
			accumulator += elapsedTime;
			
			while(accumulator >= interval) {
				this.updateCar();
				accumulator -= interval;
			}
			
			this.render();
			this.sync();
		}
	}
	
	@Override
	public void run() {
		try {
			this.createBufferStrategy(this.numBuffers);
			this.strategy = this.getBufferStrategy();
			this.timer.init();
			this.loop();
		} catch (Exception e) {e.printStackTrace();}
	}
	
}
