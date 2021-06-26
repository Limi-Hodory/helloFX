package model;

import anomalyDetection.TimeSeriesAnomalyDetector;
import javafx.beans.property.IntegerProperty;
import anomalyDetection.TimeSeries;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private TimeSeries trainTimeSeries;
    private TimeSeriesAnomalyDetector detector;

    private ObservableList<String> colsNames;
    //private ListProperty<String> colsNames;
    private File chosen;

    private UserSettings settings;

    public Model(IntegerProperty timestep, UserSettings settings)
    {
        this.settings = settings;

    	this.timestep = new SimpleIntegerProperty();
        timestep.bind(this.timestep);
       // ts = new TimeSeries("reg_flight.csv");
        trainTimeSeries = new TimeSeries();
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
                    if (timestep.get() < trainTimeSeries.getTimeSteps())
                        timestep.set(timestep.get() + 1);
                }
            }, 0, 1000 / samplesPerSecond);
        }
    }

    public TimeSeries getTrainTimeSeries() 
    {
        return trainTimeSeries;
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

     public void openLearn()
     {
        FileChooser fc = new FileChooser();
        fc.setTitle("open file");
        fc.setInitialDirectory(new File(".")); //Path to the file
        fc.setSelectedExtensionFilter((new FileChooser.ExtensionFilter("csv file", "csv")));
        chosen = fc.showOpenDialog(null); //Give back a file: if its null-not found
        if (chosen != null) {
            // here we choose what to do with the file
            trainTimeSeries = new TimeSeries(chosen.getName());

            // ViewList
            colsNames = FXCollections.observableArrayList(trainTimeSeries.getFeatures());
            setChanged();
            notifyObservers();
        }
    }
    public void openDetect()
    {
        FileChooser fc = new FileChooser();
        fc.setTitle("open file");
        fc.setInitialDirectory(new File(".")); //Path to the file
        fc.setSelectedExtensionFilter((new FileChooser.ExtensionFilter("csv file", "csv")));
        chosen = fc.showOpenDialog(null); //Give back a file: if its null-not found
        if (chosen != null) {
            // here we choose what to do with the file
            trainTimeSeries = new TimeSeries(chosen.getName());

            // ViewList
            colsNames = FXCollections.observableArrayList(trainTimeSeries.getFeatures());
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
        chosen = fc.showOpenDialog(null); //Give back a file: if its null-not found
        if (chosen != null)
        {
            try {

                URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new URL("file://" + settings.getAnomalyDetectionAlgoFilePath())});
                String className = chosen.getPath().substring(settings.getAnomalyDetectionAlgoFilePath().length(), chosen.getPath().length()); //to get the specific path (for ex: \linreg\LinearRegressionAnomalyDetector.class)
                System.out.println(className);
            /*
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


             */
            } catch (Exception e) {}
        }
    }

    public float[] getTimeSeriesColUntil(String col, int timestep)
    {
        return Arrays.copyOf(trainTimeSeries.valuesOf(col), timestep);
    }

    public ObservableList<String> getColsNames()
    {
        return colsNames;
    }
}