package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class CollisionSystem {
	
	private Sim sim;
		
	private float intersectionX = -1;
	private float intersectionY = -1;
	
	public CollisionSystem(Sim sim) {
		this.sim = sim;
	}
	
	// public methods
	
	public void render(Graphics2D g2d) {
		renderLineIntersectionPoints(g2d);
		renderLineRectIntersectionPoints(g2d);
	}
	
	// private methods
	
	private void renderLineIntersectionPoints(Graphics2D g2d) {
		float x1 = Sim.SIM_WIDTH/2;
		float y1 = Sim.SIM_HEIGHT/2;
		float x2 = sim.mouseInputs.getX();
		float y2 = sim.mouseInputs.getY();
		
		g2d.setColor(Color.red);
		g2d.draw(new Line2D.Double(x1, y1, x2, y2));
		
		float x3 = 100;
		float y3 = 100;
		float x4 = 500;
		float y4 = 300;
		
		g2d.setColor(Color.cyan);
		g2d.draw(new Line2D.Double(x3, y3, x4, y4));
		
		boolean hit = predictedLineLineCollision(x1, y1, x2, y2, x3, y3, x4, y4);
		
		if(hit == true) {
			g2d.setColor(Color.red);
			g2d.fillOval((int) intersectionX - 5,(int) intersectionY - 5,(int) 10,(int) 10);
		}
		
	}
	
	private void renderLineRectIntersectionPoints(Graphics2D g2d) {
		
	}
	
	// just displays collision point
	private boolean lineLineCollision(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		
		boolean intersected = true;
		float denominator = (y4-y3)*(x2-x1)-(x4-x3)*(y2-y1);
		
		if(denominator == 0) { // check if parallel
			intersected = false;
		}
		
	    // Calculate the parameters t and u for the intersection formulas
		float t = ((x4-x3)*(y1-y3)-(y4-y3)*(x1-x3))/denominator;
		float u = ((x2-x1)*(y1-y3)-(y2-y1)*(x1-x3))/denominator;
		
	    // Check if the intersection point lies on the second line segment
		if(t >= 0 && t <= 1 && u >= 0 && u <= 1) {
			intersectionX = x1 + t*(x2-x1);
			intersectionY = y1 + t*(y2-y1);
			intersected = true;
		} else {
			intersected = false;
		}
		
		return intersected;
	}
	
	// displays collision point and predicted collision point
	private boolean predictedLineLineCollision(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		
		boolean intersected = true;
		float denominator = (y4-y3)*(x2-x1)-(x4-x3)*(y2-y1);
		
		if(denominator == 0) { // check if parallel
			intersected = false;
		}
		
	    // Calculate the parameters t and u for the intersection formulas
		float t = ((x4-x3)*(y1-y3)-(y4-y3)*(x1-x3))/denominator;
		float u = ((x2-x1)*(y1-y3)-(y2-y1)*(x1-x3))/denominator;
		
		// calculate intersection point
		intersectionX = x1 + t*(x2-x1);
		intersectionY = y1 + t*(y2-y1);
		
	    // Calculate distances from the intersection point to the endpoints of the first line segment
		float d1 = (float) Math.sqrt(Math.pow((intersectionX - x2), 2) + Math.sqrt(Math.pow((intersectionY - y2), 2)));
		float d2 = (float) Math.sqrt(Math.pow((intersectionX - x1), 2) + Math.sqrt(Math.pow((intersectionY - y1), 2)));
		
	    // Check if the intersection point lies on the second line segment
		if(x3 == x4) { // vertical lines
			if(intersectionY >= y3 && intersectionY <= y4 && d1 < d2) {
				intersected = true;
			} else {
				intersected = false;
			}
		} else if(intersectionX >= x3 && intersectionX <= x4 && d1 < d2) { // non vertical lines
			intersected = true;
		} else {
			intersected = false;
		}
				
		return intersected;
	}
	
	private boolean lineRectangleCollision(float x1, float y1, float x2, float y2) {
		
		
		return true;
	}
	
}
