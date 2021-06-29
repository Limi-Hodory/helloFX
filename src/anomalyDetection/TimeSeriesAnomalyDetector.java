package anomalyDetection;

import java.util.List;

public interface TimeSeriesAnomalyDetector 
{
	void learnNormal(TimeSeries ts);
	List<AnomalyReport> detect(TimeSeries ts);
	List<PaintData> paint(String col);
}
