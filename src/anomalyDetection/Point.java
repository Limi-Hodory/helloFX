package anomalyDetection;

public class Point 
{	
	public final float x;
	public final float y;
	
	public Point(float x, float y) 
	{
		this.x=x;
		this.y=y;
	}
	
	public Point(Point p)
	{
		this(p.x, p.y);
	}
	
	public float distance(Point p)
	{
		return (float)Math.sqrt((x-p.x)*(x-p.x) + (y-p.y)*(y-p.y));
	}
}
