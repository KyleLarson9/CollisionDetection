package collisionLogic;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

public class IntersectionPointManager {

	private ArrayList<Point2D.Double> intersectionPoints;
	
	public IntersectionPointManager() {
		intersectionPoints = new ArrayList<>();
	}
	
	public void addIntersectionPoint(double x, double y) {
		intersectionPoints.add(new Point2D.Double(x, y));
	}
	
	public void clear() {
		intersectionPoints.clear();
	}
	
	public ArrayList<Point2D.Double> getIntersectionPoints() {
		return intersectionPoints;
	}
	
}
