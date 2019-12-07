package gameEngine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import threeDimensions.Graphics3D;
import threeDimensions.PackedColor;
import threeDimensions.Texture;

public class GameEngine extends Canvas implements Runnable {
	
	public static int displayWidth, displayHeight;
	
	private int fps, ups;
	private boolean threeDimensional;
	
	public static Keyboard keyboard;
	public static Mouse mouse;
	
	private JFrame window;
	
	private int numBuffers;
	private BufferStrategy strategy;
	private Timer timer;
	private IGameLogic gameLogic;
	
	public GameEngine(int fps, int ups, int numBuffers, IGameLogic gameLogic, int width, int height, String title, boolean threeDimensional) {
		
		//Important Statics
		displayWidth = width;
		displayHeight = height;
		
		//User Set Variables
		this.fps = fps;
		this.ups = ups;
		this.numBuffers = numBuffers;
		this.threeDimensional = threeDimensional;
		
		//Input Handling
		keyboard = new Keyboard();
		mouse = new Mouse();
		this.addMouseListener(mouse);
		this.addMouseMotionListener(mouse);
		this.addKeyListener(keyboard);
		
		//First Frame
		this.setBackground(Color.black);
	
		//Initialize Timer
		this.timer = new Timer();
		
		//Most Important - ALL GAME LOGIC
		this.gameLogic = gameLogic;
		
		//Creating the Frame/Canvas
		this.setSize(width, height);
		this.window = new JFrame();
		window.add(this);
		window.pack();
		window.setTitle(title);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setVisible(true);
		
		this.startThread();
	}
	
	public GameEngine(IGameLogic gameLogic) {
		this(60, 60, 3, gameLogic, 800, 800, "Default", false);
	}

	private synchronized void startThread() {
		Thread t = new Thread(this);
		t.start();
		this.setFocusable(true);
	}
	
	protected void init() throws Exception {
		this.createBufferStrategy(this.numBuffers);
		this.strategy = this.getBufferStrategy();
		this.timer.init();
		this.gameLogic.init(this.window.getGraphics());
	}
	
	private void sync() {
		float loopSlot = 1f / this.fps;
		double endTime = timer.getLastLoopTime() + loopSlot;
		while(timer.getTime() < endTime) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException ie) {}
		}
	}
	
	private void loop() {
		float elapsedTime;
		float accumulator = 0f;
		float interval = 1f / this.ups;
		
		boolean running = true;
		while(running) {
			elapsedTime = this.timer.getElapsedTime();
			accumulator += elapsedTime;
			
			this.input();
			while(accumulator >= interval) {
				this.update(interval);
				accumulator -= interval;
			}
			
			this.render();
			this.sync();
		}
	}
	
	public static Texture loadImage(String filepath) {
		BufferedImage img = null;
		try {

			img = ImageIO.read(new File(filepath));
			System.out.println(BufferedImage.TYPE_3BYTE_BGR);

		} catch (IOException e) {
			System.out.println(e);
		}

		int[] pixels = new int[img.getWidth() * img.getHeight()];
		if (img.getType() == BufferedImage.TYPE_3BYTE_BGR) {
			byte[] src = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
			for (int i = 0, j = 0; i < pixels.length; i++ ) {
				byte b = src[j++];
				byte g = src[j++];
				byte r = src[j++];
				pixels[i] = ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
			}
		} else if(img.getType() == BufferedImage.TYPE_4BYTE_ABGR) {
			byte[] src = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
			for (int i = 0, j = 0; i < pixels.length; i++ ) {
				byte a = src[j++];
				byte b = src[j++];
				byte g = src[j++];
				byte r = src[j++];
				pixels[i] = a << 24 | r << 16 | g << 8 | b;
			}
		}
		return new Texture(pixels, img.getWidth(), img.getHeight());
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.init();
			this.loop();
		} catch (Exception e) {e.printStackTrace();}
	}
	
	protected void input() {
		this.gameLogic.input();
	}
	
	protected void update(float interval) {
		this.gameLogic.update(interval);
	}
	
	protected void render() {
		Graphics g = this.strategy.getDrawGraphics();
		
		if(this.threeDimensional) {
			Graphics3D g3d = new Graphics3D(); //Might make this persistent
			g3d.clear(PackedColor.Black);
			this.gameLogic.render3D(g3d);
			g.drawImage(g3d.getImage(), 0, 0, null);
		} else {
			g.setColor(Color.black);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		
		this.gameLogic.render((Graphics2D) g);
		
		g.dispose();
		this.strategy.show();
	}

	
	
}