package hybridAlg;

import java.util.List;
import anomalyDetection.Point;

public class Circle 
{
	public final Point center;
	public final float radius;
	
	public Circle(Point center, float radius)
	{
		this.center = new Point(center);
		this.radius = radius;
	}
	
	public Circle()
	{
		center = new Point(0, 0);
		radius = 0;
	}
	
	public Circle(Point center)
	{
		this.center = new Point(center);
		radius = 0;
	}
	
	public Circle(Point a, Point b)
	{
		center = new Point((a.x + b.x) / 2.0f, (a.y + b.y) / 2.0f);
		radius = a.distance(b) / 2;
	}
	
	public Circle(Point a, Point b, Point c)
	{
		//Rearanging the points in case we get nan in the x or y values to calculate the circle
		//in a different way. When all possible arrangments yield nan we know we cannot form a circle
		//from these points (they are on the same line).
		Circle circle = calcFrom3Points(a,b,c);
		if(Float.isNaN(circle.center.x) || Float.isNaN(circle.center.y))
			circle = calcFrom3Points(b, a, c);
		if(Float.isNaN(circle.center.x) || Float.isNaN(circle.center.y))
			circle = calcFrom3Points(c, b, a);
		if(Float.isNaN(circle.center.x) || Float.isNaN(circle.center.y))
			circle = calcFrom3Points(a, c, b);
		if(Float.isNaN(circle.center.x) || Float.isNaN(circle.center.y))
			circle = new Circle();
		
		center = circle.center;
		radius = circle.radius;
	}
	
	private static Circle calcFrom3Points(Point a, Point b, Point c)
	{
		float yba = b.y - a.y;
		float xba = b.x - a.x;
		
		float yac = a.y - c.y;
		float xac = a.x - c.x;
		
		float s_yba = yba*(b.y + a.y);
		float s_xba = xba*(b.x + a.x);
		
		float s_yac = yac*(a.y + c.y);
		float s_xac = xac*(a.x + c.x);
		
		float h2 = (yba*(s_xac + s_yac) - (yac)*(s_xba + s_yba)) / 
					(yba*xac - yac*xba);
		
		float k2 = (s_xac + s_yac - xac*h2) / yac;	
		float m = -(a.x*a.x) - (a.y*a.y) + a.x*h2 + a.y*k2;
		
		Point center = new Point(h2 / 2, k2 / 2);
		float radius = (float) Math.sqrt(center.x*center.x + center.y*center.y - m);
		return new Circle(center, radius);
	}
	
	public boolean isEnclosing(Point p)
	{
		return p.distance(center) <= radius;
	}
	
	public boolean isEnclosing(List<Point> points)
	{
		for(Point p : points)
			if(!isEnclosing(p))
				return false;
		return true;
	}
	
	@Override
	public String toString()
	{
		return "Center at: (" + center.x + ", " + center.y + ") radius = " + radius;
	}
}
