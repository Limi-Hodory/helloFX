package settings;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import anomalyDetection.AnomalyReport;
import anomalyDetection.TimeSeries;
import anomalyDetection.TimeSeriesAnomalyDetector;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

public class jarTest extends Application 
{
	
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("Sample.fxml"));
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception 
	{
		/*
		String path = "E:\\ASD\\lab2\\HybridAlgorithmAnomalyDetector\\bin\\";
		String className = "hybridAlg.HybridAlgorithmAnomalyDetector";
		URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] {new URL("file://"+path)});
		Class<?> c = classLoader.loadClass(className);
		TimeSeriesAnomalyDetector detector = (TimeSeriesAnomalyDetector) c.newInstance();
		
		TimeSeries ts = new TimeSeries("reg_flight.csv");
		detector.learnNormal(ts);
		
		List<AnomalyReport> reports = detector.detect(new TimeSeries("anomaly_flight.csv"));
		
		System.out.println("Number of reports: " + reports.size());
		for(AnomalyReport ar : reports)
			System.out.println(ar);
		
		System.out.println("done!");
		 */
	}

}