package model;

import javafx.beans.property.IntegerProperty;
import anomalyDetection.TimeSeries;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class Model extends Observable
{
    private Timer timer = null;
    private IntegerProperty timestep;
    private TimeSeries trainTimeSeries;
    private ObservableList<String> colsNames;
    //private ListProperty<String> colsNames;
    private File chosen;
   

    public Model(IntegerProperty timestep) 
    {
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

    /*
    public void open(){

        FileChooser fc = new FileChooser();
        fc.setTitle("open file");
        fc.setInitialDirectory(new File(".")); //Path to the file
        fc.setSelectedExtensionFilter((new FileChooser.ExtensionFilter("xml", "csv")));
        chosen = fc.showOpenDialog(null); //Give back a file: if its null-not found
        if (chosen != null) {
            // here we choose what to do with the file
            trainTimeSeries = new TimeSeries(chosen.getName());

            // ViewList
            colsNames = FXCollections.observableArrayList(trainTimeSeries.getFeatures());
            setChanged();
            notifyObservers();
    }

     */


    public void open(){

        FileChooser fc = new FileChooser();
        fc.setTitle("open file");
        fc.setInitialDirectory(new File(".")); //Path to the file
        fc.setSelectedExtensionFilter((new FileChooser.ExtensionFilter("xml", "csv")));

    }



     public void openLearn()
     {
        FileChooser fc = new FileChooser();
        fc.setTitle("open file");
        fc.setInitialDirectory(new File(".")); //Path to the file
        fc.setSelectedExtensionFilter((new FileChooser.ExtensionFilter("xml", "csv")));
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
        fc.setSelectedExtensionFilter((new FileChooser.ExtensionFilter("xml", "csv")));
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
        fc.setInitialDirectory(new File(".")); //Path to the file
        fc.setSelectedExtensionFilter((new FileChooser.ExtensionFilter("class")));
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



    public ObservableList<String> getColsNames()
    {
        return colsNames;
    }
  //  public File getFile(){ return chosen; }
}