package views;

import java.util.Observable;
import java.util.Observer;

import javafx.application.Platform;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import viewModel.ViewModel;

@SuppressWarnings("deprecation")
public class GraphController implements Observer
{
	@FXML
	LineChart<String, Float> featureLine;
	
	private ViewModel vm;
	
	private Series<String, Float> featureLineData;
	
	private StringProperty currentFeature;
	private String curF = "";
	private FloatProperty currentFeatureValue;
	private IntegerProperty timestep;
	
	public void setViewModel(ViewModel vm)
	{
		this.vm = vm;
		vm.addObserver(this);
		
		currentFeature = new SimpleStringProperty("");
		currentFeatureValue = new SimpleFloatProperty();
		timestep = new SimpleIntegerProperty();
		
		currentFeature.bind(vm.getSelectedFeatureProperty());
		currentFeatureValue.bind(vm.getSelectedFeatureValueProperty());
		timestep.bind(vm.getTimestepProperty());
		
		featureLineData = new Series<String, Float>();
		featureLine.getData().add(featureLineData);
		featureLine.setAnimated(false);
		featureLine.getYAxis().setAutoRanging(false);


		/*
		currentFeature.addListener((observer, oldV, newV) -> 
			{
				featureLine.setTitle(newV);
				
				//featureLine.getData().remove(featureLineData);
				//featureLineData.getData().clear();

				featureLine.getData().removeAll();

				featureLineData = new Series<String, Float>();
				float test = (float)1.1;
				featureLineData.getData().add(0, new XYChart.Data<String, Float>("aa", test));
				featureLine.getData().add(featureLineData);
				//featureLine.getData().add(featureLineData);
				
			});
		*/
		timestep.addListener((observable, oldV, newV) -> 
			{

				if(!currentFeature.get().equals(""))
				{
					if(!curF.equals(currentFeature.toString()))//change
					{
						Platform.runLater(() -> featureLineData.getData().clear());
						curF = currentFeature.toString();
					}
					Platform.runLater(() -> featureLineData.getData().add(new XYChart.Data<String, Float>(newV.toString(), currentFeatureValue.get())));
					System.out.println("new data created:" + currentFeatureValue.get());
				}
				});
		
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		
	}
	
}
