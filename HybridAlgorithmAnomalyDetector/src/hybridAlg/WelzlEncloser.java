package hybridAlg;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import anomalyDetection.Point;

public class WelzlEncloser 
{
	public final List<Point> points;
	private Random rand;
	
	public WelzlEncloser(List<Point> points)
	{
		this.points = points;
	}
	
	public Circle minimumEnclosingCircle()
	{
		List<Point> P = points;
		List<Point> R = new ArrayList<Point>();
		rand = new Random();
		return welzl(P, R);
	}
	
	private Circle welzl(List<Point> points, List<Point> boundary) 
	{	
		if (boundary.size() == 3) 
			return new Circle(boundary.get(0), boundary.get(1), boundary.get(2));
		if (points.isEmpty() && boundary.size() == 2)
			return new Circle(boundary.get(0), boundary.get(1));
		if (points.size() == 1 && boundary.isEmpty())
			return new Circle(points.get(0), 0.0f);
		if (points.size() == 1 && boundary.size() == 1)
			return new Circle(points.get(0), boundary.get(0));
		
		Point p = points.remove(rand.nextInt(points.size()));
		Circle minimumCircle = welzl(points, boundary);
			
		if (!minimumCircle.isEnclosing(p)) 
		{
			boundary.add(p);
			minimumCircle = welzl(points, boundary);
			boundary.remove(p);
			points.add(p);
		}
				
		return minimumCircle;
    }
	
}
