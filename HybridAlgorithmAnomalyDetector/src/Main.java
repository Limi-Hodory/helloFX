import java.util.ArrayList;
import java.util.List;

import anomalyDetection.*;
import hybridAlg.*;


public class Main {

	public static void main(String[] args) {
		
		HybridAlgorithmAnomalyDetector detector = new HybridAlgorithmAnomalyDetector();
		detector.learnNormal(new TimeSeries("reg_flight.csv"));
		List<AnomalyReport> l = detector.detect(new TimeSeries("anomaly_flight.csv"));
		
		for(AnomalyReport a : l)
			System.out.println(a);
		
		System.out.println("done!");
	}

}
