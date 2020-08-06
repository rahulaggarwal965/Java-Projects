package utils;

public abstract class Pool<T> {
	
	private T[] pool;
	private int count;
	
	public abstract void add();
	
	public abstract void clear();
}
