package zscore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import anomalyDetection.*;

import javax.print.attribute.standard.PrinterIsAcceptingJobs;

public class ZScoreAnomalyDetector implements TimeSeriesAnomalyDetector
{
	private HashMap<String, Float> t;

	private TimeSeries learnTimeSeries;
	private List<AnomalyReport> detectionResults;

	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		List<AnomalyReport> reports = new ArrayList<AnomalyReport>();
		for(String featureName : ts.getFeatures()) {
			if(t.containsKey(featureName)) {
				float[] X = ts.valuesOf(featureName);
				for(int i = 0; i < X.length; i++) {
					float zScore = z(X[i], Arrays.copyOfRange(X, 0, i));
					if(zScore > t.get(featureName))
						reports.add(new AnomalyReport(featureName + "=" + zScore, i));
				}
			}
		}
		detectionResults = reports;
		return reports;
	}

	@Override
	public List<PaintData> paint(String col)
	{
		List<PaintData> paints = new ArrayList<PaintData>();
		paints.add(paintZScore(col));
		paints.add(paintResults(col));
		return paints;
	}

	private PaintData paintZScore(String col)
	{
		List<Point> points = new ArrayList<Point>();
		float[] X = learnTimeSeries.valuesOf(col);
		for(int i = 0; i < X.length; i++)
			points.add(new Point(i, z(X[i], Arrays.copyOfRange(X, 0, i))));
		return new PaintData(points, "blue", true);
	}

	private PaintData paintResults(String col)
	{
		List<Point> points = new ArrayList<Point>();
		for(AnomalyReport ar : detectionResults)
			if(ar.description.contains(col))
				points.add(new Point(ar.timeStep, Float.parseFloat(ar.description.split("=")[1])));
		return new PaintData(points, "red", false);
	}

	@Override
	public void learnNormal(TimeSeries ts) {
		learnTimeSeries = ts;
		t = new HashMap<String, Float>();
		for(String featureName : ts.getFeatures()) {
			t.put(featureName, 0f);
			float[] X = ts.valuesOf(featureName);
			for(int i = 0; i < X.length; i++)
			{
				float zScore = z(X[i], Arrays.copyOfRange(X, 0, i));
				if(zScore > t.get(featureName))
					t.put(featureName, zScore);
			}
		}
		
	}
	
	private float z(float x, float[] X) {
		float mean = StatLib.avg(X);
		float var = StatLib.var(X);
		return mean == 0 || var == 0 ? 0f : (float)(Math.abs(x - mean) / Math.sqrt(StatLib.var(X)));
	}
}
