package viewModel;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FlightConnector;
import model.Model;
import settings.UserSettings;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer {
    //timeStep: tell us in witch line of the timeSeries we are in.
    //We want the model to change the timeStep, the timer in the model will change the timeStep
    //When the timeStep change we want to Extract the data for the display variables (הנתונים בצג)
    //In the model we"ll have reference to timeStep but its ok because is an integerProperty, *not* kind of view model

    private IntegerProperty timestep;
    public final Runnable openL, openD,openA, play, pause, stop; //My methods
    public Model m;

    private UserSettings settings;
    private ObservableList<String> colsNames;

    private StringProperty selectedFeature;
    private FloatProperty selectedFeatureValue;

    //private   File chosen;

    private ObservableList<String> cols = new SimpleListProperty<>();
    public HashMap<String, DoubleProperty> displayVariables = new HashMap<>(); //String = name of attribute (altitude,speed...)

    private FlightConnector mFlightConnector = new FlightConnector();

    public ViewModel(UserSettings settings) {
        timestep = new SimpleIntegerProperty(0);
        m = new Model(timestep); //Equally the model should also know the timeSeries.
        m.addObserver(this);
        colsNames = FXCollections.observableArrayList();
        selectedFeature = new SimpleStringProperty("");
        selectedFeatureValue = new SimpleFloatProperty(0);
        this.settings = settings;
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


        timestep.addListener((observer, oldV, newV) ->
        {    //This func will run every time that timeStep will change.
            // here we need to take the values from time Series and update.

            Map<String, Float> vals = m.getTrainTimeSeries().row(newV.intValue());
            if (vals != null) {
                for (String s : displayVariables.keySet()) {
                    Float val = vals.get(s);
                    if (val != null) {
                        Platform.runLater(() -> {
                            displayVariables.get(s).set(vals.get(s));


                        });
                    }
                }
                // System.out.println("selected feature: " + selectedFeature);

                selectedFeatureValue.set(vals.getOrDefault(selectedFeature.get(), 0f));
            }
            String row = m.getTrainTimeSeries().getRow(newV.intValue());
            System.out.println(newV.intValue() + row);
            mFlightConnector.sendDataToFlight(row);

        });

        openL = () -> m.openLearn();
        openD = () -> m.openDetect();
        openA=()->m.openAlgo();
        play = () -> m.play(settings.getSamplesPerSecond());
        pause = () -> m.pause();
        stop = () -> m.stop();

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

    public void selectFeature(String featureName) {
        selectedFeature.setValue(featureName);
    }

    public StringProperty getSelectedFeatureProperty() {
        return selectedFeature;
    }

    public FloatProperty getSelectedFeatureValueProperty() {
        return selectedFeatureValue;
    }

    public IntegerProperty getTimestepProperty() {
        return timestep;
    }
}