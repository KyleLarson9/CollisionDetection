package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class CollisionSystem {
	
	private Sim sim;
	
	private float intersectionX = -1;
	private float intersectionY = -1;
	
	public CollisionSystem(Sim sim) {
		this.sim = sim;
	}
	
	// public methods
	
	public void render(Graphics2D g2d) {
		renderIntersectionPoints(g2d);
	}
	
	// private methods
	
	private void renderIntersectionPoints(Graphics2D g2d) {
		float x1 = Sim.SIM_WIDTH/2;
		float y1 = Sim.SIM_HEIGHT/2;
		float x2 = sim.mouseInputs.getX();
		float y2 = sim.mouseInputs.getY();
		
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
			g2d.setColor(Color.orange);
			g2d.fillOval((int) intersectionX - 5,(int) intersectionY - 5,(int) 10,(int) 10);
			//System.out.println(intersectionX + ", " + intersectionY);
		}
	}
	
	private boolean lineLineCollision(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float denominator = (y4-y3)*(x2-x1)-(x4-x3)*(y2-y1);
		
		if(denominator == 0) { // parallel
			return false;
		}
		
		float t = ((x4-x3)*(y1-y3)-(y4-y3)*(x1-x3))/denominator;
		float u = ((x2-x1)*(y1-y3)-(y2-y1)*(x1-x3))/denominator;
		
		intersectionX = x1 + t*(x2-x1);
		intersectionY = y1 + t*(y2-y1);
		
		if(intersectionX >= x3 && intersectionY <= x4 && intersectionY >= y3 && intersectionY <= y4) { 
			return true;
		} else {
			return false;
		}
	}
	
	private Point2D.Float getIntersectionPoints(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		
		float denominator = (y4-y3)*(x2-x1)-(x4-x3)*(y2-y1);
		if(denominator == 0) { // parallel
			return null;
		}
		
		float t = ((x4-x3)*(y1-y3)-(y4-y3)*(x1-x3))/denominator;
		float u = ((x2-x1)*(y1-y3)-(y2-y1)*(x1-x3))/denominator;
		
		intersectionX = x1 + t*(x2-x1);
		intersectionY = y1 + t*(y2-y1);
		
		if(intersectionX >= x3 && intersectionY <= x4 && intersectionY >= y3 && intersectionY <= y4) { 
			return new Point2D.Float(intersectionX, intersectionY);
		} else {
			return null;
		}
	}
}
