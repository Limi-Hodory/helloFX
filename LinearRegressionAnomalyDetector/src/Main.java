
import java.util.List;

import anomalyDetection.*;
import linreg.LinearRegressionAnomalyDetector;

public class Main {

	public static void main(String[] args) {
		
		LinearRegressionAnomalyDetector detector = new LinearRegressionAnomalyDetector(0.9f);
		
		TimeSeries ts = new TimeSeries("reg_flight.csv");
		detector.learnNormal(ts);
		
		/*
		for(float f : ts.valuesOf("heading-deg"))
			System.out.println(f);
		
		System.out.println("Mean: " + StatLib.avg(ts.valuesOf("heading-deg")));
		System.out.println("var: " + StatLib.var(ts.valuesOf("heading-deg")));
		*/
		
		/*
		for(String s : detector.t.keySet())
			System.out.println(s + " = " + detector.t.get(s));
		*/
		
		List<AnomalyReport> reports = detector.detect(new TimeSeries("anomaly_flight.csv"));
		
		System.out.println("Number of reports: " + reports.size());
		for(AnomalyReport ar : reports)
			System.out.println(ar);
		
		
		System.out.println("done!");
	}

}
