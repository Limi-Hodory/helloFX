package anomalyDetection;

import java.lang.Math;

public class StatLib {
	
	// simple average
	public static float avg(float[] x) {
		if(x.length == 0)
			return 0;
		
		float sum = 0;
		for(int i = 0; i < x.length; i++)
			sum += x[i];
		return sum / x.length;
	}

	// returns the variance of X and Y
	public static float var(float[] x) {
		if(x.length == 0)
			return 0;
		
		float mu = avg(x);
		float varSum = 0;
		for(int i = 0; i < x.length; i++)
			varSum += (x[i] - mu)*(x[i] - mu);
		return varSum / x.length;
	}

	// returns the covariance of X and Y
	public static float cov(float[] x, float[] y) {
		if(x.length != y.length)
			return 0;
		
		float[] xy = new float[x.length];
		for(int i = 0; i < x.length; i++)
			xy[i] = x[i]*y[i];
		
		return avg(xy) - avg(x)*avg(y);
	}

	// returns the Pearson correlation coefficient of X and Y
	public static float pearson(float[] x, float[] y) {
		return (float)(cov(x, y) / (Math.sqrt(var(x))*Math.sqrt(var(y))));
	}

	// performs a linear regression and returns the line equation
	public static Line linear_reg(Point[] points) {
		float[] x = new float[points.length];
		float[] y = new float[points.length];
		for(int i = 0; i < points.length; i++) {
			x[i] = points[i].x;
			y[i] = points[i].y;
		}
		
		float a = cov(x, y) / var(x);
		float b = avg(y) - a*avg(x);
		return new Line(a, b);
	}

	// returns the deviation between point p and the line equation of the points
	public static float dev(Point p, Point[] points){
		return dev(p, linear_reg(points));
	}

	// returns the deviation between point p and the line
	public static float dev(Point p, Line l) {
		return Math.abs(l.f(p.x) - p.y);
	}
	
}