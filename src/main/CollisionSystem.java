package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class CollisionSystem {
		
	private Sim sim;
		
	private float intersectionX = -1;
	private float intersectionY = -1;
	
	// mouse controlled  line
	private float x1 = Sim.SIM_WIDTH/2;
	private float y1 = Sim.SIM_HEIGHT/2;
	private float x2;
	private float y2;
	
	// static line
	private float x3 = 100;
	private float y3 = 100;
	private float x4 = 500;
	private float y4 = 300;
	
	// rectangle
	private float rectX = 100;
	private float rectY = 400;
	private float rectWidth = 100;
	private float rectHeight = 100;
	
	// polygons
	private  int sides;
	private double radius;
	private Path2D.Float polygon;
	private  ArrayList<Point2D.Double> vertices = new ArrayList<>();

	public CollisionSystem(Sim sim) {
		this.sim = sim;	
		polygon = new Path2D.Float();
		vertices = new ArrayList<>();
		
		initializePolygon();
	}
	
	// public methods
	
	public void render(Graphics2D g2d) {
		renderLineIntersectionPoints(g2d);
		renderLineRectIntersectionPoints(g2d);
		renderPolygon(g2d);
		drawPolygonVerticies(g2d);
		renderLinePolyIntersectionPoints(g2d);
	}
	
	// private methods
	
	private void initializePolygon() {
		vertices.clear();
		polygon.reset();
		
		double centerX = 400;
		double centerY = 400;
		int rotate = 40;
		sides = 8;
		radius = 100;
		
		for(int i = 0; i < sides; i++) {
			double angle = Math.toRadians((i*360)/sides - rotate);
			double x = centerX + radius * Math.cos(angle);
			double y = centerY + radius * Math.sin(angle);
			
			vertices.add(new Point2D.Double(x, y));
			
			if(i == 0) {
				polygon.moveTo(x, y);
			} else {
				polygon.lineTo(x, y);
			}
		}
		
		polygon.closePath();
	}

	private void renderPolygon(Graphics2D g2d) {
		g2d.setColor(Color.magenta);
		g2d.draw(polygon);
	}
	
	private void drawPolygonVerticies(Graphics2D g2d) {
		
		for(Point2D.Double vertex : vertices) {
			g2d.setColor(Color.black);
			g2d.fillOval((int) vertex.getX() - 5, (int) vertex.getY() - 5, 10, 10);
		}
		
	}
	
	private void renderLineIntersectionPoints(Graphics2D g2d) {
		
		x2 = sim.mouseInputs.getX();
		y2 = sim.mouseInputs.getY();
		
		g2d.setColor(Color.red);
		g2d.draw(new Line2D.Double(x1, y1, x2, y2));
		
		g2d.setColor(Color.cyan);
		g2d.draw(new Line2D.Double(x3, y3, x4, y4));
		 
		boolean hit = predictedLineLineCollision(x1, y1, x2, y2, x3, y3, x4, y4);
		
		if(hit == true) {
			g2d.setColor(Color.red);
			g2d.fillOval((int) intersectionX - 5,(int) intersectionY - 5,(int) 10,(int) 10);
		}
		
	}
	
	private void renderLineRectIntersectionPoints(Graphics2D g2d) {
		
		x2 = sim.mouseInputs.getX();
		y2 = sim.mouseInputs.getY();
		
		g2d.setColor(Color.green);
		g2d.fill(new Rectangle2D.Float(rectX, rectY, rectWidth, rectHeight)); // x, y, width, height
		
		boolean hit = lineRectangleCollision(x1, y1, x2, y2, rectX, rectY, rectWidth, rectHeight);
		
		if(hit == true) {
			g2d.setColor(Color.red);
			g2d.fillOval((int) intersectionX - 5,(int) intersectionY - 5,(int) 10,(int) 10);
		}
	}
		
	private void renderLinePolyIntersectionPoints(Graphics2D g2d) {
		
		x2 = sim.mouseInputs.getX();
		y2 = sim.mouseInputs.getY();
		
		boolean hit = linePolygonCollision(x1, y1, x2, y2);
		
		if(hit == true)	{
			g2d.setColor(Color.red);
			g2d.fillOval((int) intersectionX - 5,(int) intersectionY - 5,(int) 10,(int) 10);
		}
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
			if(intersectionY >= y3 && intersectionY <= y4 && d1 < d2 || intersectionY <= y3 && intersectionY >= y4 && d1 < d2) {
				intersected = true;
			} else {
				intersected = false;
			}
		} else if(intersectionX >= x3 && intersectionX <= x4 && d1 < d2 || intersectionX <= x3 && intersectionX >= x4 && d1 < d2 ) { // non vertical lines
			intersected = true;
		} else {
			intersected = false;
		}
				
		return intersected;
	}
	
	// just display collision points
	private boolean lineLineCollision(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		
		boolean intersected = true;
		float denominator = (y4-y3)*(x2-x1)-(x4-x3)*(y2-y1);
		
		if(denominator == 0) { // check if parallel
			intersected = false;
		}
		
	    // Calculate the parameters t and u for the intersection formulas
		float t = ((x4-x3)*(y1-y3)-(y4-y3)*(x1-x3))/denominator;
		float u = ((x2-x1)*(y1-y3)-(y2-y1)*(x1-x3))/denominator;
		
	    // Check if the intersection point lies on the second line segments
		if(t >= 0 && t <= 1 && u >= 0 && u <= 1) {
			intersectionX = x1 + t*(x2-x1);
			intersectionY = y1 + t*(y2-y1);
			intersected = true;
		} else {
			intersected = false;
		}
		
		return intersected;
	}

	private boolean lineRectangleCollision(float x1, float y1, float x2, float y2, float rectX, float rectY, float rectWidth, float rectHeight) {
		
		// check each line segment
		boolean top  = predictedLineLineCollision(x1, y1, x2, y2, rectX, rectY, rectX + rectWidth, rectY);
		if(top) return true;
		boolean right = predictedLineLineCollision(x1, y1, x2, y2, rectX + rectWidth, rectY, rectX + rectWidth, rectY + rectHeight);
		if(right) return true;
		boolean bottom = predictedLineLineCollision(x1, y1, x2, y2, rectX, rectY + rectHeight, rectX + rectWidth, rectY + rectHeight);
		if(bottom) return true;
		boolean left = predictedLineLineCollision(x1, y1, x2, y2, rectX, rectY, rectX, rectY + rectHeight);
		if(left) return true;
		

		return false;
	}
	
	private boolean linePolygonCollision(float x1, float y1, float x2, float y2) {
		
		// loop through each line segment
		
		boolean collision = false;
		
		for(int i = 0; i < sides - 1; i++) {
			float x3 = (float) vertices.get(i).x;
			float y3 = (float) vertices.get(i).y;
			float x4 = (float) vertices.get(i + 1).x;
			float y4 = (float) vertices.get(i + 1).y;
			
			collision = predictedLineLineCollision(x1, y1, x2, y2, x3, y3, x4, y4);
			
			if(collision) {
				collision = true;
			} else {
				collision = false;
			}
		}
		
		return collision;
	}
}
