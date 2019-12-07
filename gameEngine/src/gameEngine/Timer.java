package gameEngine;

public class Timer {
	private double lastLoopTime;
	
	public void init() {
		lastLoopTime = getTime();
	}
	
	public double getTime() {
		return System.nanoTime() / 1000_000_000.0;
	}
	
	public float getElapsedTime() {
		double now = getTime();
		float elapsedTime = (float) (now - this.lastLoopTime);
		this.lastLoopTime = now;
		return elapsedTime;
	}
	
	public double getLastLoopTime() {
		return this.lastLoopTime;
	}
}
