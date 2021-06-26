package views;

import java.util.Observable;
import java.util.Observer;

import anomalyDetection.CorrelatedFeatures;
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
	LineChart<Number, Number> featureGraph;
	@FXML
	LineChart<Number, Number> correlatedFeatureGraph;

	private ViewModel vm;

	private Series<Number, Number> featureGraphSeries;
	private Series<Number, Number> correlatedFeatureSeries;

	private StringProperty selectedFeature;
	private StringProperty correlatedFeature;
	private FloatProperty selectedFeatureValue;
	private FloatProperty correlatedFeatureValue;
	private IntegerProperty timestep;

	public void setViewModel(ViewModel vm)
	{
		this.vm = vm;
		vm.addObserver(this);
		
		selectedFeature = new SimpleStringProperty("");
		correlatedFeature = new SimpleStringProperty("");
		selectedFeatureValue = new SimpleFloatProperty();
		correlatedFeatureValue = new SimpleFloatProperty();
		timestep = new SimpleIntegerProperty();
		
		selectedFeature.bind(vm.getSelectedFeatureProperty());
		correlatedFeature.bind(vm.getSelectedCorrelatedFeatureProperty());
		selectedFeatureValue.bind(vm.getSelectedFeatureValueProperty());
		correlatedFeatureValue.bind(vm.getSelectedCorrelatedFeatureValueProperty());
		timestep.bind(vm.getTimestepProperty());

		featureGraphSeries = new Series<Number, Number>();
		featureGraph.getData().add(featureGraphSeries);

		correlatedFeatureSeries = new Series<Number, Number>();
		correlatedFeatureGraph.getData().add(correlatedFeatureSeries);

		selectedFeature.addListener((observableValue, oldV, newV) ->
			{
				Platform.runLater(() -> changeFeatureGraph(featureGraph, featureGraphSeries, newV));
			});

		correlatedFeature.addListener((ObservableValue, oldV, newV) ->
			{
				Platform.runLater(() -> changeFeatureGraph(correlatedFeatureGraph, correlatedFeatureSeries, newV));
			});

		timestep.addListener((observable, oldV, newV) -> 
			{
				if(newV.intValue() == 0)
				{
					Platform.runLater(() -> featureGraphSeries.getData().clear());
					Platform.runLater(() -> correlatedFeatureSeries.getData().clear());
				}
				else if(!selectedFeature.get().equals(""))
				{
					XYChart.Data<Number, Number> dataF = new XYChart.Data<Number, Number>(newV.intValue(), selectedFeatureValue.get());
					Platform.runLater(() -> featureGraphSeries.getData().add(dataF));
					XYChart.Data<Number, Number> dataCor = new XYChart.Data<Number, Number>(newV.intValue(), correlatedFeatureValue.get());
					Platform.runLater(() -> correlatedFeatureSeries.getData().add(dataCor));
				}
			});
	}

	private void changeFeatureGraph(LineChart<Number, Number> graph, Series<Number, Number> series, String changeTo)
	{
		series.getData().clear();
		float[] f = vm.getTimeSeriesColUntil(changeTo, timestep.get());
		for(int i = 0; i < f.length; i++)
		{
			XYChart.Data<Number, Number> data = new XYChart.Data<Number, Number>(i + 1, f[i]);
			series.getData().add(data);
		}
		graph.getYAxis().setLabel(changeTo);
	}

	@Override
	public void update(Observable o, Object arg)
	{
	}
	
}
