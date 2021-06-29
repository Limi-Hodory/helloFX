package hybridAlg;

import java.lang.Math;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import anomalyDetection.*;

public class LinearRegressionAnomalyDetector implements TimeSeriesAnomalyDetector 
{
	
	private float threshold;
	private List<CorrelatedFeatures> correlatedFeatures;
	private TimeSeries learnTimeSeries;
	private List<AnomalyReport> detectionRes;
	private int timeSteps = 0;

	public LinearRegressionAnomalyDetector()
	{
		this(0.9f);
	}
	
	public LinearRegressionAnomalyDetector(float threshold) {
		this.threshold = threshold;
		correlatedFeatures = new ArrayList<CorrelatedFeatures>();
	}
	
	public float getThreshold() {
		return threshold;
	}
	
	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}
	
	@Override
	public void learnNormal(TimeSeries ts)
	{
		learnTimeSeries = ts;
		List<CorrelatedFeatures> correlatedFeatures = findCorrelatedFeatures(ts);
		for(CorrelatedFeatures cf : correlatedFeatures) {
			Point[] dataPoints = convertToPoints(ts.valuesOf(cf.feature1), ts.valuesOf(cf.feature2));
			Line regLine = StatLib.linear_reg(dataPoints);
			
			float maxThreshold = 0;
			for(Point p : dataPoints) {
				float dev = StatLib.dev(p, regLine);
				if(dev > maxThreshold)
					maxThreshold = dev;
			}
			
			this.correlatedFeatures.add(new CorrelatedFeatures(cf.feature1, cf.feature2, cf.correlation, regLine, maxThreshold * 1.1f));
		}
	}

	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		List<AnomalyReport> report = new ArrayList<AnomalyReport>();
		
		for(int i = 0; i < ts.getTimeSteps(); i++) {
			Map<String, Float> m = ts.row(i);
			for(CorrelatedFeatures cf : correlatedFeatures) {
				Point p = new Point(m.get(cf.feature1), m.get(cf.feature2));
				if(StatLib.dev(p, cf.lin_reg) > cf.threshold)
					report.add(new AnomalyReport(String.format("%s=%f,%s=%f", cf.feature1, m.get(cf.feature1), cf.feature2, m.get(cf.feature2)), i + 1));
			}
		}

		timeSteps = (int)ts.getTimeSteps();
		detectionRes = report;
		return report;
	}

	@Override
	public List<PaintData> paint(String col)
	{
		List<PaintData> paints = new ArrayList<PaintData>();
		paints.add(paintRegLine(col));
		paints.add(paintNormal(col));
		paints.add(paintResults(col));
		return  paints;
	}

	private PaintData paintRegLine(String col)
	{
		List<Point> points = new ArrayList<Point>();
		for(CorrelatedFeatures cf : correlatedFeatures) {
			if (cf.feature1.equals(col))
			{
				float min = minVal(col);
				points.add(new Point(min, cf.lin_reg.f(min)));
				float max = maxVal(col);
				points.add(new Point(max, cf.lin_reg.f(max)));
				return new PaintData(points, "blue", true);
			}
		}
		points.add(new Point(0,0));
		return new PaintData(points, "blue", false);
	}

	private PaintData paintNormal(String col)
	{
		List<Point> points = new ArrayList<Point>();
		for(CorrelatedFeatures cf : correlatedFeatures) {
			if (cf.feature1.equals(col))
			{
				float[] x = learnTimeSeries.valuesOf(cf.feature1);
				float[] y = learnTimeSeries.valuesOf(cf.feature2);
				for(int i = 0; i < x.length; i++)
					points.add(new Point(x[i], y[i]));
				return new PaintData(points, "green", false);
			}
		}
		points.add(new Point(0,0));
		return new PaintData(points, "green", false);
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

	private float minVal(String f)
	{
		float[] vals = learnTimeSeries.valuesOf(f);
		float min = Float.POSITIVE_INFINITY;
		for(int i = 0; i < vals.length; i++)
			if(vals[i] < min)
				min = vals[i];
		return min;
	}

	private float maxVal(String f)
	{
		float[] vals = learnTimeSeries.valuesOf(f);
		float max = Float.NEGATIVE_INFINITY;
		for(int i = 0; i < vals.length; i++)
			if(vals[i] > max)
				max = vals[i];
		return max;
	}

	public List<CorrelatedFeatures> getNormalModel(){
		return correlatedFeatures;
	}
	
	private List<CorrelatedFeatures> findCorrelatedFeatures(TimeSeries ts) {
		List<CorrelatedFeatures> cf = new ArrayList<CorrelatedFeatures>();

		String[] f = ts.getFeatures();
		for(int i = 0; i < f.length; i++) {
			float maxCorrelation = 0;
			String maxFeature = f[i];
			for(int j = i + 1; j < f.length; j++) {
				float correlation = StatLib.pearson(ts.valuesOf(f[i]), ts.valuesOf(f[j]));
				if(Math.abs(correlation) > Math.abs(maxCorrelation)) {
					maxCorrelation = correlation;
					maxFeature = f[j];
				}
			}
			
			if(Math.abs(maxCorrelation) > threshold)
				cf.add(new CorrelatedFeatures(f[i], maxFeature, maxCorrelation,
						null, 0));
		}

		return cf;
	}
	
	private Point[] convertToPoints(float[] vals1, float[] vals2) {
		Point[] points = new Point[vals1.length];
		for(int i = 0; i < points.length; i++)
			points[i] = new Point(vals1[i], vals2[i]);
		return points;
	}
}