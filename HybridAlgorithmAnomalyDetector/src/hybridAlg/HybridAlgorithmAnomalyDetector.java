package hybridAlg;
import java.util.ArrayList;
import java.util.List;
import anomalyDetection.*;

public class HybridAlgorithmAnomalyDetector implements TimeSeriesAnomalyDetector 
{
	private class CircleFeatures 
	{
		public final String feature1;
		public final String feature2;
		public Circle minimumEnclosingCircle;
		
		public CircleFeatures(String feature1, String feature2)
		{
			this.feature1 = feature1;
			this.feature2 = feature2;
		}
	}
	
	private float highThreshold = 0.95f;
	private float lowThreshold = 0.5f;
	
	private LinearRegressionAnomalyDetector linRegDetector;
	private ZScoreAnomalyDetector zScoreDetector;
	
	private List<String> linRegCols;
	private List<String> zScoreCols;
	private List<CircleFeatures> circleCols;
	private TimeSeries learnTimeSeries;
	private List<AnomalyReport> detectionRes;

	public HybridAlgorithmAnomalyDetector() 
	{
		linRegDetector = new LinearRegressionAnomalyDetector(highThreshold);
		zScoreDetector = new ZScoreAnomalyDetector();
	}
	
	@Override
	public List<AnomalyReport> detect(TimeSeries ts) 
	{
		List<AnomalyReport> reports = new ArrayList<AnomalyReport>();
		
		reports.addAll(linRegDetector.detect(ts.subSeries(linRegCols)));
		reports.addAll(zScoreDetector.detect(ts.subSeries(zScoreCols)));
		
		for(CircleFeatures features : circleCols)
		{
			List<Point> points = makePoints(ts.valuesOf(features.feature1), ts.valuesOf(features.feature2));
			for(int i = 0; i < ts.getTimeSteps(); i++)
				if(!features.minimumEnclosingCircle.isEnclosing(points.get(i)))
					reports.add(new AnomalyReport(String.format("%s=%f,%s=%f", features.feature1, points.get(i).x, features.feature2, points.get(i).y), i + 1));
		}
		detectionRes = reports;
		return reports;
	}

	@Override
	public List<PaintData> paint(String col)
	{
		List<CorrelatedFeatures> mostCor = StatLib.getMostCorrelatedFeatures(learnTimeSeries);

		CorrelatedFeatures sCorF = null;
		for(CorrelatedFeatures cf : mostCor)
			if(cf.feature1.equals(col))
				sCorF = cf;
		if(sCorF == null)
			return new ArrayList<PaintData>();

		if (Math.abs(sCorF.correlation) >= highThreshold)
			return linRegDetector.paint(col);
		else if(Math.abs(sCorF.correlation) < lowThreshold)
			return zScoreDetector.paint(col);
		List<PaintData> paints = new ArrayList<PaintData>();
		paints.add(paintCircle(col));
		paints.add(paintResults(col));
		return paints;
	}

	private PaintData paintCircle(String col)
	{
		List<Point> points = new ArrayList<Point>();
		for(CircleFeatures cf: circleCols)
		{
			if(cf.feature1.equals(col))
			{
				Circle c = cf.minimumEnclosingCircle;
				for(float angle = 0; angle < 360; angle += 0.5f)
				{
					float x = (float)(c.radius*Math.cos(angle) + c.center.x);
					float y = (float)(c.radius*Math.sin(angle) + c.center.y);
					points.add(new Point(x,y));
				}
				return new PaintData(points, "blue", false);
			}
		}
		points.add(new Point(0,0));
		return new PaintData(points, "blue", true);
	}

	private PaintData paintResults(String col)
	{
		List<Point> points = new ArrayList<Point>();
		for(AnomalyReport ar : detectionRes)
		{
			String[] s = ar.description.split(",");
			if(s[0].contains(col))
			{
				float valx = Float.parseFloat(s[0].split("=")[1]);
				float valy = Float.parseFloat(s[1].split("=")[1]);
				points.add(new Point(valx,valy));
			}
		}
		return new PaintData(points, "red", false);
	}

	@Override
	public void learnNormal(TimeSeries ts) 
	{
		learnTimeSeries = ts;
		fillColsLists(ts);
		linRegDetector.learnNormal(ts.subSeries(linRegCols));
		zScoreDetector.learnNormal(ts.subSeries(zScoreCols));
		
		for(CircleFeatures features : circleCols)
		{
			WelzlEncloser encloser = new WelzlEncloser(makePoints(ts.valuesOf(features.feature1),
					ts.valuesOf(features.feature2)));
			features.minimumEnclosingCircle = encloser.minimumEnclosingCircle();
		}
	}
	
	private void fillColsLists(TimeSeries ts) 
	{
		linRegCols = new ArrayList<String>();
		zScoreCols = new ArrayList<String>();
		circleCols = new ArrayList<CircleFeatures>();
		
		String[] f = ts.getFeatures();
		for(int i = 0; i < f.length; i++) 
		{
			float maxCorrelation = 0;
			int maxFeatureIndex = i;
			for(int j = 0; j < f.length; j++) 
			{
				if(j != i)
				{
					float correlation = StatLib.pearson(ts.valuesOf(f[i]), ts.valuesOf(f[j]));
					if(Math.abs(correlation) > Math.abs(maxCorrelation)) 
					{
						maxCorrelation = correlation;
						maxFeatureIndex = j;
					}
				}
			}
			
			if(Math.abs(maxCorrelation) >= highThreshold) 
			{
				linRegCols.add(f[i]);
				linRegCols.add(f[maxFeatureIndex]);
			}
			else if(Math.abs(maxCorrelation) >= lowThreshold)
				circleCols.add(new CircleFeatures(f[i], f[maxFeatureIndex]));
			else 
				zScoreCols.add(f[i]);
		}
	}
	
	private List<Point> makePoints(float[] x, float[] y)
	{	
		List<Point> l = new ArrayList<Point>();
		
		if(x.length != y.length)
			return l;
		
		for(int i = 0; i < x.length; i++)
			l.add(new Point(x[i], y[i]));
		
		return l;
	}

}
