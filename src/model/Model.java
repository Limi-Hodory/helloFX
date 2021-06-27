package model;

import anomalyDetection.TimeSeriesAnomalyDetector;
import javafx.beans.property.IntegerProperty;
import anomalyDetection.TimeSeries;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import settings.UserSettings;
import viewModel.ViewModel;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class Model extends Observable
{
    private Timer timer = null;
    private IntegerProperty timestep;
    private TimeSeries timeSeries;
    private TimeSeriesAnomalyDetector detector;
    
    private ObservableList<String> colsNames;

    private UserSettings settings;

    public Model(IntegerProperty timestep, UserSettings settings)
    {
        this.settings = settings;
    	this.timestep = new SimpleIntegerProperty();
        timestep.bind(this.timestep);
        timeSeries = new TimeSeries();
        colsNames = new SimpleListProperty<String>();
    }

    public void play(int samplesPerSecond) 
    {
        if (samplesPerSecond == 0)
            return;

        if (timer == null) 
        {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() 
            {
                @Override
                public void run() 
                {
                    if (timestep.get() < timeSeries.getTimeSteps())
                        timestep.set(timestep.get() + 1);
                }
            }, 0, 1000 / samplesPerSecond);
        }
    }

    public TimeSeries getTimeSeries() 
    {
        return timeSeries;
    }

    public void pause() 
    {
        timer.cancel();
        timer = null;
    }

    public void stop() 
    {
        if (timer == null)
            return;

        timer.cancel();
        timer = null;
        timestep.set(0);
    }

    public void multiply1(int samplesPerSecond){

        timer.cancel();
        timer=null;
        play(samplesPerSecond*2);
    }

    public void multiply2(int samplesPerSecond){

        timer.cancel();
        timer=null;
        play(samplesPerSecond*3);
    }
    
    public void openDetect()
    {
    	if(detector == null)
    	{
    		Alert a = new Alert(AlertType.WARNING);
    		a.setContentText("There is no loaded algorithm.");
    		a.show();
    		return;
    	}
        FileChooser fc = new FileChooser();
        fc.setTitle("open file");
        fc.setInitialDirectory(new File(".")); //Path to the file
        fc.setSelectedExtensionFilter((new FileChooser.ExtensionFilter("csv file", "csv")));
        File chosen = fc.showOpenDialog(null); //Give back a file: if its null-not found
        if (chosen != null) {
            // here we choose what to do with the file
            timeSeries = new TimeSeries(chosen.getName());
            
            // ViewList
            colsNames = FXCollections.observableArrayList(timeSeries.getFeatures());
            setChanged();
            notifyObservers();
        }
    }

    public void openAlgo()
    {
        FileChooser fc = new FileChooser();
        fc.setTitle("open file");
        fc.setInitialDirectory(new File(settings.getAnomalyDetectionAlgoFilePath())); //Path to the f
        fc.setSelectedExtensionFilter((new FileChooser.ExtensionFilter("class file", "class")));
        File chosen = fc.showOpenDialog(null); //Give back a file: if its null-not found
        if (chosen != null)
        {
        	String className = constructClassName(chosen.getPath());
            try {
                URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new URL("file://" + settings.getAnomalyDetectionAlgoFilePath())});
                Class<?> c = classLoader.loadClass(className);
                detector = (TimeSeriesAnomalyDetector) c.newInstance();
            } catch (Exception e) {
            	e.printStackTrace();
            	System.out.println("Error occured trying to open the algorithm. \n"
            			+ "Try checking the conf.xml file for the right plugins path. \n"
            			+ "Class Name: " + className);
            }
            if(detector != null)
            {
            	detector.learnNormal(new TimeSeries(settings.getValidFlightFilePath()));
            	System.out.println("trained!");
            }
        }
    }
    
    private String constructClassName(String path)
    {
    	String className = path.substring(settings.getAnomalyDetectionAlgoFilePath().length(), path.length());
    	return className.replace(".class", "").replace("\\", ".");
    }
    
    public float[] getTimeSeriesColUntil(String col, int timestep)
    {
        return Arrays.copyOf(timeSeries.valuesOf(col), timestep);
    }

    public ObservableList<String> getColsNames()
    {
        return colsNames;
    }
}