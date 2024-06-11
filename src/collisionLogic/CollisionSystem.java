package collisionLogic;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import main.Sim;

public abstract class CollisionSystem {
	
	public IntersectionPointManager intersectionPointManager;
	
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
	
	// polygons
	public  int sides;
	public double radius;
	public Path2D.Float polygon;
	public ArrayList<Point2D.Double> vertices = new ArrayList<>();
		
	public CollisionSystem() {
		polygon = new Path2D.Float();
		vertices = new ArrayList<>();
		intersectionPointManager = new IntersectionPointManager();
		
		initializePolygon();
		
	}

	public void initializePolygon() {
		vertices.clear();
		polygon.reset();
		
		double centerX = 400;
		double centerY = 400;
		int rotate = 0;
		sides = 3;
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
	
	// finds the line collision point as well as determines the point if the lines aren't intersecting yet
	public boolean lineLineCollision(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		
		boolean intersected = false;
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
				intersectionPointManager.addIntersectionPoint(intersectionX, intersectionY);
				intersected = true;
			} else {
				intersected = false;
			}
		} else if(intersectionX >= x3 && intersectionX <= x4 && d1 < d2 || intersectionX <= x3 && intersectionX >= x4 && d1 < d2 ) { // non vertical lines
			intersectionPointManager.addIntersectionPoint(intersectionX, intersectionY);
			intersected = true;
		} else {
			intersected = false;
		}
				
		return intersected;
	}
	
	public boolean linePolygonCollision(float x1, float y1, float x2, float y2) {
						
		boolean collision = false;
		intersectionPointManager.clear(); // important
		
		// loop through each line segment
		for(int i = 0; i < sides - 1; i++) {
			float x3 = (float) vertices.get(i).x;
			float y3 = (float) vertices.get(i).y;
			float x4 = (float) vertices.get(i + 1).x;
			float y4 = (float) vertices.get(i + 1).y;
			
			if(lineLineCollision(x1, y1, x2, y2, x3, y3, x4, y4)) {
				collision = true;
			} else {
				collision = false;
			}

		}
		
		return collision;
	}
	
}

// idea for light refraction sim
// if there is a rectangle add special options to change the length of it then I don't need to have seperate methods for 
// rectangle and polygon
