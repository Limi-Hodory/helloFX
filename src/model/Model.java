package model;

import anomalyDetection.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import settings.UserSettings;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class Model extends Observable
{
    private Timer timer = null;
    private IntegerProperty timestep;
    private TimeSeries detectTimeSeries;
    private TimeSeries learnTimeSeries;
    private TimeSeriesAnomalyDetector detector;
    private List<AnomalyReport> detectionRes;
    private List<CorrelatedFeatures> correlatedFeatures;

    private ObservableList<String> colsNames;

    private UserSettings settings;

    public Model(IntegerProperty timestep, UserSettings settings)
    {
        this.settings = settings;
    	this.timestep = new SimpleIntegerProperty();
        timestep.bind(this.timestep);
        detectTimeSeries = new TimeSeries();
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
                    if (timestep.get() < detectTimeSeries.getTimeSteps())
                        timestep.set(timestep.get() + 1);
                }
            }, 0, 1000 / samplesPerSecond);
        }
    }

    public TimeSeries getTimeSeries() 
    {
        return detectTimeSeries;
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
            detectTimeSeries = new TimeSeries(chosen.getName());
            detectionRes = detector.detect(detectTimeSeries);
            // ViewList
            colsNames = FXCollections.observableArrayList(detectTimeSeries.getFeatures());
            setChanged();
            notifyObservers();
            for(AnomalyReport ar : detectionRes)
                System.out.println(ar.description);
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
                learnTimeSeries = new TimeSeries(settings.getValidFlightFilePath());
                correlatedFeatures = StatLib.getMostCorrelatedFeatures(learnTimeSeries);
            	detector.learnNormal(learnTimeSeries);
            	System.out.println("trained!");
            }
        }
    }
    
    private String constructClassName(String path)
    {
    	String className = path.substring(settings.getAnomalyDetectionAlgoFilePath().length(), path.length());
    	return className.replace(".class", "").replace("\\", ".");
    }
    
    public float[] getDetectTimeSeriesColUntil(String col, int timestep)
    {
        if(detectTimeSeries != null)
            return Arrays.copyOf(detectTimeSeries.valuesOf(col), timestep);
        return new float[0];
    }
/*
    public List<Point> paintLearn(String f1, String f2)
    {
        List<Point> toReturn = new ArrayList<Point>();
        if(detector.isSingleColumn())
        {
            float[] arr = learnTimeSeries.valuesOf(f1);
            for(int i = 0; i < arr.length; i++)
                toReturn.add(new Point(i, arr[i]));
        }
        else
        {
            float[] arr1 = learnTimeSeries.valuesOf(f1);
            float[] arr2 = learnTimeSeries.valuesOf(f2);
            for(int i = 0; i< arr1.length; i++)
                toReturn.add(new Point(arr1[i], arr2[i]));
        }

        return toReturn;
    }
*/
    public ObservableList<String> getColsNames()
    {
        return colsNames;
    }

    public List<PaintData> paintAlgo(String f1)
    {
        if(detector != null)
            return detector.paint(f1);
        return new ArrayList<PaintData>();
    }
/*
    public List<Point> paintResults(String f1, String f2)
    {
        List<Point> toReturn = new ArrayList<Point>();
        String description = f1;
        if(!detector.isSingleColumn())
            description += "," + f2;
        System.out.println(description);
        for(AnomalyReport ar : detectionRes)
        {
            if(ar.description.equals(description))
            {
                Map<String, Float> row = detectTimeSeries.row((int)ar.timeStep);
                if(detector.isSingleColumn())
                    toReturn.add(new Point((int)ar.timeStep, row.get(f1)));
                else
                    toReturn.add(new Point(row.get(f1), row.get(f2)));
            }
        }
        return toReturn;
    }
*/
    public String mostCorFeature(String f)
    {
        for(CorrelatedFeatures cf : correlatedFeatures)
            if(cf.feature1.equals(f))
                return cf.feature2;
        return "";
    }
}