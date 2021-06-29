package views;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import viewModel.ViewModel;
import java.time.LocalTime;
import java.util.Observable;
import java.util.Observer;

public class PlayerController implements Observer {

    //These functions will be activated when the buttons is pressed.
    //The controller tells us when to do but no what to do.
    //Therefore we need this runnable obj- because we need to get from outside the functions that tell us what to do.
    //The runnable obj allow us to activate an external function when we press the buttons (play,pause,play).

    public Runnable onOpen, onPlay, onPause, onStop ,onMultiply1, onMultiply2 ,onRewind1 ,onRewind2 ;
    @FXML
    Label timer;
    @FXML
    Slider slider;

    IntegerProperty timestep;
    private ViewModel vm;


    public void open() {
        if(onOpen!=null)
            onOpen.run();
    }


    public void play() {
        if(onPlay!=null)
            onPlay.run();

    }

    public void pause() {
        if(onPause!=null)
            onPause.run();

    }

    public void stop() {
        if(onStop!=null)
            onStop.run();

    }
    public void multiply1() {
        if(onMultiply1!=null)
            onMultiply1.run();

    }
    public void multiply2() {
        if(onMultiply2!=null)
            onMultiply2.run();

    }
    public void rewind1() {
        if(onRewind1!=null)
            onRewind1.run();

    }
    public void rewind2() {
        if(onRewind2!=null)
            onRewind2.run();

    }
    public void setViewModel(ViewModel vm) {
        this.vm = vm;
        vm.addObserver(this);
        timestep = new SimpleIntegerProperty();
        timestep.bind(vm.getTimestepProperty());

        timestep.addListener((observable, oldV, newV) -> {
            Platform.runLater(() -> change());
        });

        //sliderY.valueProperty().bind(vm.displayVariables.get(vm.getSettings().getThrottleField().getFeatureName()));
        slider.valueProperty().bind(vm.getTimestepProperty());
        slider.setMax(2300);
        slider.setMin(0);

    }

    private void change()  {
        timer.setText(LocalTime.ofSecondOfDay(timestep.intValue()).toString());
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}