package viewModel;

import anomalyDetection.PaintData;
import anomalyDetection.Point;
import anomalyDetection.StatLib;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.FlightConnector;
import model.Model;
import settings.UserSettings;

import java.util.*;

public class ViewModel extends Observable implements Observer {
    //timeStep: tell us in witch line of the timeSeries we are in.
    //We want the model to change the timeStep, the timer in the model will change the timeStep
    //When the timeStep change we want to Extract the data for the display variables (הנתונים בצג)
    //In the model we"ll have reference to timeStep but its ok because is an integerProperty, *not* kind of view model

    private IntegerProperty timestep;
    public final Runnable openD,openA, play, pause, stop,multiply1, multiply2; //My methods
    public Model m;

    private UserSettings settings;
    private ObservableList<String> colsNames;

    private StringProperty selectedFeature;
    private FloatProperty selectedFeatureValue;
    private StringProperty selectedCorrelatedFeature;
    private FloatProperty selectedCorrelatedFeatureValue;

    //private   File chosen;

    private ObservableList<String> cols = new SimpleListProperty<>();
    public HashMap<String, DoubleProperty> displayVariables = new HashMap<>(); //String = name of attribute (altitude,speed...)

    private FlightConnector mFlightConnector = new FlightConnector();

    public ViewModel() 
    {
    	try 
    	{
    		settings = UserSettings.decodeXML();
    	} catch(Exception e)
    	{
    		Alert a = new Alert(AlertType.ERROR);
    		a.setHeaderText("Fatal Error: Missing Or Invalid Configuration File.");
    		a.setContentText("Could not load configuration file.\n"
    				+ "Please provide valid 'conf.xml' file in the same directory and restart the program.\n"
    				+ "Program will not run correctly and may crash!");
    		a.show();
    	}
        timestep = new SimpleIntegerProperty(0);
        m = new Model(timestep, settings); //Equally the model should also know the timeSeries.
        m.addObserver(this);
        colsNames = FXCollections.observableArrayList();

        selectedFeature = new SimpleStringProperty("");
        selectedFeatureValue = new SimpleFloatProperty(0);
        selectedCorrelatedFeature = new SimpleStringProperty("");
        selectedCorrelatedFeatureValue = new SimpleFloatProperty(0);

        //We"ll add to the map only the variable that we want to show in the view
        displayVariables.put(settings.getAltitudeField().getFeatureName(), new SimpleDoubleProperty());
        displayVariables.put(settings.getSpeedField().getFeatureName(), new SimpleDoubleProperty());
        displayVariables.put(settings.getDirectionField().getFeatureName(), new SimpleDoubleProperty());
        displayVariables.put(settings.getYawField().getFeatureName(), new SimpleDoubleProperty());
        displayVariables.put(settings.getPitchField().getFeatureName(), new SimpleDoubleProperty());
        displayVariables.put(settings.getThrottleField().getFeatureName(), new SimpleDoubleProperty());
        displayVariables.put(settings.getRudderField().getFeatureName(), new SimpleDoubleProperty());
        displayVariables.put(settings.getJoystickXField().getFeatureName(), new SimpleDoubleProperty());
        displayVariables.put(settings.getJoystickYField().getFeatureName(), new SimpleDoubleProperty());
        displayVariables.put(settings.getRollField().getFeatureName(), new SimpleDoubleProperty());


        timestep.addListener((observer, oldV, newV) ->
        {    //This func will run every time that timeStep will change.
            // here we need to take the values from time Series and update.

            Map<String, Float> vals = m.getTimeSeries().row(newV.intValue());
            if (vals != null) {
                for (String s : displayVariables.keySet()) {
                    Float val = vals.get(s);
                    if (val != null) {
                        Platform.runLater(() -> {
                            displayVariables.get(s).set(vals.get(s));
                        });
                    }
                }
                selectedFeatureValue.set(vals.getOrDefault(selectedFeature.get(), 0f));
                selectedCorrelatedFeatureValue.set(vals.getOrDefault(selectedCorrelatedFeature.get(), 0f));

                selectedFeatureValue.set(vals.getOrDefault(selectedFeature.get(), 0f));
            }
            mFlightConnector.sendDataToFlight(mapToString(vals));
        });

        openD = () -> m.openDetect();
        openA = ()-> m.openAlgo();
        play = () -> m.play(settings.getSamplesPerSecond());
        pause = () -> m.pause();
        stop = () -> m.stop();
        multiply1 =() -> m.multiply1(settings.getSamplesPerSecond());
        multiply2 =() -> m.multiply2(settings.getSamplesPerSecond());

    }
    
    private String mapToString(Map<String, Float> map)
    {
    	String toReturn = "";
    	for(String s : map.keySet())
    		toReturn += map.get(s).toString();
    	return toReturn;
    }
    
    public DoubleProperty getProperty(String name) {
        return displayVariables.get(name);
    }

    public UserSettings getSettings() {
        return settings;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == m) {
            colsNames = m.getColsNames();
            setChanged();
            notifyObservers();
        }
    }

    public ObservableList<String> getColsNames() {
        return colsNames;
    }

    public void selectFeature(String featureName)
    {
        selectedFeature.set(featureName);
        selectedCorrelatedFeature.set(m.mostCorFeature(featureName));
    }

    public List<PaintData> paintAlgo(String f1)
    {
        return m.paintAlgo(f1);
    }
    /*
    public List<Point> paintResults(String f1, String f2)
    {
        return m.paintResults(f1, f2);
    }

    public List<Point> paintLearn(String f1, String f2)
    {
        return m.paintLearn(f1,f2);
    }


     */
    public StringProperty getSelectedFeatureProperty() {
        return selectedFeature;
    }

    public FloatProperty getSelectedFeatureValueProperty() {
        return selectedFeatureValue;
    }

    public IntegerProperty getTimestepProperty() {
        return timestep;
    }

    public StringProperty getSelectedCorrelatedFeatureProperty() {
        return selectedCorrelatedFeature;
    }

    public FloatProperty getSelectedCorrelatedFeatureValueProperty() {
        return selectedCorrelatedFeatureValue;
    }

    public float[] getTimeSeriesColUntil(String col, int timestep)
    {
        return m.getDetectTimeSeriesColUntil(col, timestep);
    }
}