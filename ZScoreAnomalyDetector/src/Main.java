import java.util.List;
import anomalyDetection.AnomalyReport;
import anomalyDetection.TimeSeries;
import zscore.ZScoreAnomalyDetector;

public class Main {

	public static void main(String[] args) {
		ZScoreAnomalyDetector detector = new ZScoreAnomalyDetector();
		
		TimeSeries ts = new TimeSeries("anomalyTrain.csv");
		detector.learnNormal(ts);

		/*
		for(String s : detector.t.keySet())
			System.out.println(s + " = " + detector.t.get(s));
		*/
		List<AnomalyReport> reports = detector.detect(new TimeSeries("anomalyTest.csv"));
		
		System.out.println("Number of reports: " + reports.size());
		for(AnomalyReport ar : reports)
			System.out.println("Anomaly at " + ar.timeStep + ": " + ar.description);
		
		System.out.println("done!");
	}

}
