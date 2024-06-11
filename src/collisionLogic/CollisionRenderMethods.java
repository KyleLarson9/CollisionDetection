package collisionLogic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D.Double;

import main.Sim;

public class CollisionRenderMethods extends CollisionSystem {

	private Sim sim;
	
	public CollisionRenderMethods(Sim sim) {
		this.sim = sim;
	}

	public void render(Graphics2D g2d) {
		renderLineIntersectionPoints(g2d);
		renderPolygon(g2d);
		renderLinePolyIntersectionPoints(g2d);
		
	}

	private void renderPolygon(Graphics2D g2d) {
		g2d.setColor(Color.magenta);
		g2d.draw(newPolyShape);
		
		g2d.setColor(Color.red);
		
		
	    for(Point2D.Double point : intersectionPointManager.getIntersectionPoints()) {
	        g2d.fill(new Ellipse2D.Double(point.x - 5, point.y - 5, 10, 10));
	        
	    }
	}
	
	private void renderLineIntersectionPoints(Graphics2D g2d) {
		
		x2 = sim.mouseInputs.getX();
		y2 = sim.mouseInputs.getY();
		
		g2d.setColor(Color.red);
		g2d.draw(new Line2D.Double(x1, y1, x2, y2));
		
		g2d.setColor(Color.cyan);
		g2d.draw(new Line2D.Double(x3, y3, x4, y4));
		
		boolean hit = lineLineCollision(x1, y1, x2, y2, x3, y3, x4, y4);
		
		if(hit) {
			g2d.setColor(Color.red);
			g2d.fill(new Ellipse2D.Double(intersectionX - 5, intersectionY - 5, 10, 10));
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
}
