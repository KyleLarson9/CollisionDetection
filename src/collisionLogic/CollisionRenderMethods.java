package collisionLogic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import main.Sim;

public class CollisionRenderMethods extends CollisionSystem {

	private Sim sim;
	
	public CollisionRenderMethods(Sim sim) {
		this.sim = sim;
	}

	public void render(Graphics2D g2d) {
		renderLineIntersectionPoints(g2d);
		renderLineRectIntersectionPoints(g2d);
		renderPolygon(g2d);
		renderLinePolyIntersectionPoints(g2d);
	}

	private void renderPolygon(Graphics2D g2d) {
		g2d.setColor(Color.magenta);
		g2d.draw(polygon);
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
}