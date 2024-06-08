package main;

import java.awt.Graphics2D;
import inputs.MouseInputs;

public class Sim implements Runnable {

	private SimFrame frame;
	public SimPanel panel;
	public MouseInputs mouseInputs;
	private CollisionSystem collisionSystem;
	
	private Thread thread;
	private boolean running = true;
	
	private final int FPS = 120;
	private final int UPS = 200;
	
	private final static int TILES_DEFAULT_SIZE = 20;
	private final static float SCALE = 1.0f;
	private final static int TILES_IN_WIDTH = 30;
	private final static int TILES_IN_HEIGHT = 30;
	private final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	public final static int SIM_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int SIM_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
	
	public Sim() {
		
		initializeClasses();
		
		panel.setFocusable(true);
		panel.requestFocus();
		panel.addMouseMotionListener(mouseInputs);
		
		startSimLoop();
	}

	// public methods
	
	public void render(Graphics2D g2d) {
		collisionSystem.render(g2d);
	}
	
	// private methods

	private void startSimLoop() {
		thread = new Thread(this);
		thread.start();
	}
	
	private void initializeClasses() {
		panel = new SimPanel(this);
		mouseInputs = new MouseInputs();
		frame = new SimFrame(panel);
		collisionSystem = new CollisionSystem(this);
	}
	
	@Override
	public void run() {
		double timePerFrame = 1_000_000_000.0 / FPS; // how long each from will last, 1 second
		double timePerUpdate = 1_000_000_000.0 / UPS;
		
		long previousTime = System.nanoTime();
			
		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();
		
		double deltaU = 0;
		double deltaF = 0;
		
		while(running) {
			long currentTime = System.nanoTime();
			
			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;
			
			if(deltaU >= 1) {
				updates++;
				deltaU--;
			}
			
			if(deltaF >= 1) {
				panel.repaint();
				frames++;
				deltaF--;
			}
			
			if(System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				frames = 0;
				updates = 0;
			}

		}
	}
	
}
