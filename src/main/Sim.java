package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;

import inputs.MouseInputs;

public class Sim implements Runnable {

	private SimFrame frame;
	public SimPanel panel;
	public MouseInputs mouseInputs;
	
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
	
	private float intersectionX = -1;
	private float intersectionY = -1;
	
	public Sim() {
		
		initializeClasses();
		
		panel.setFocusable(true);
		panel.requestFocus();
		panel.addMouseMotionListener(mouseInputs);
		
		startSimLoop();
	}

	// public methods
	
	public void render(Graphics2D g2d) {
		
		lines(g2d);
	}
	
	// private methods
	
	private void lines(Graphics2D g2d) { 
		float x1 = SIM_WIDTH/2;
		float y1 = SIM_HEIGHT/2;
		float x2 = mouseInputs.getX();
		float y2 = mouseInputs.getY();
		
		g2d.setColor(Color.red);
		g2d.draw(new Line2D.Double(x1, y1, x2, y2));
		
		float x3 = 100;
		float y3 = 100;
		float x4 = 300;
		float y4 = 200;
		
		g2d.setColor(Color.cyan);
		g2d.draw(new Line2D.Double(x3, y3, x4, y4));
		
		boolean hit = lineLineCollision(x1, y1, x2, y2, x3, y3, x4, y4);
		
		if(hit == true) {
			g2d.drawOval((int) intersectionX - 5,(int) intersectionY - 5,(int) 10,(int) 10);
			//System.out.println(intersectionX + ", " + intersectionY);
		}
		
	}
	
	private boolean lineLineCollision(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		
		float t = ((x4-x3)*(y1-y3)-(y4-y3)*(x1-x3))/((y4-y3)*(x2-x1)-(x4-x3)*(y2-y1));
		float u = ((x2-x1)*(y1-y3)-(y2-y1)*(x1-x3))/((y4-y3)*(x2-x1)-(x4-x3)*(y2-y1));
		
		intersectionX = x1 + t*(x2-x1);
		intersectionY = y1 + t*(y2-y1);
		
		return t <= 1 && t >= 0 && u <= 1 && u >= 0;
	}
	
	private void startSimLoop() {
		thread = new Thread(this);
		thread.start();
	}
	
	private void initializeClasses() {
		panel = new SimPanel(this);
		mouseInputs = new MouseInputs();
		frame = new SimFrame(panel);
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
