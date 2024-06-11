package collisionLogic;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import main.Sim;

public abstract class CollisionSystem {
	
	public float intersectionX = -1;
	public float intersectionY = -1;
	
	// mouse controlled  line
	public float x1 = Sim.SIM_WIDTH/2;
	public float y1 = Sim.SIM_HEIGHT/2;
	public float x2;
	public float y2;
	
	// static line
	public float x3 = 100;
	public float y3 = 100;
	public float x4 = 500;
	public float y4 = 300;
	
	// rectangle
	public float rectX = 100;
	public float rectY = 400;
	public float rectWidth = 100;
	public float rectHeight = 100;
	
	// polygons
	public  int sides;
	public double radius;
	public Path2D.Float polygon;
	public  ArrayList<Point2D.Double> vertices = new ArrayList<>();
	
	public CollisionSystem() {
		polygon = new Path2D.Float();
		vertices = new ArrayList<>();
		
		initializePolygon();
		
	}

	public void initializePolygon() {
		vertices.clear();
		polygon.reset();
		
		double centerX = 400;
		double centerY = 400;
		int rotate = 40;
		sides = 7;
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
	
	public boolean predictedLineLineCollision(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		
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

	public boolean lineRectangleCollision(float x1, float y1, float x2, float y2, float rectX, float rectY, float rectWidth, float rectHeight) {
		
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
	
	public boolean linePolygonCollision(float x1, float y1, float x2, float y2) {
				
		boolean collision = false;
		
		// loop through each line segment
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