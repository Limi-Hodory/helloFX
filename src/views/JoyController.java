package views;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.shape.Circle;
import viewModel.ViewModel;

import java.util.Observable;
import java.util.Observer;


public class JoyController implements Observer {
    @FXML
    Circle stick;
    @FXML
    Slider sliderY;
    @FXML
    Slider sliderX;

    private ViewModel vm;

    private DoubleProperty aileron;
    private DoubleProperty elevator;

    public void setViewModel(ViewModel vm) {
        this.vm = vm;
        configurProperties();
        configureSliders();
        setListeners();
    }

    private void setListeners() {
        aileron.addListener((observable, oldV, newV) ->
        {
            stick.setLayoutX((newV.doubleValue()) * 120);
        });
        elevator.addListener((observable, oldV, newV) ->
        {
            stick.setLayoutY((newV.doubleValue()) * 120);
        });
    }

    private void configurProperties() {
        aileron = new SimpleDoubleProperty();
        elevator = new SimpleDoubleProperty();
        aileron.bind(vm.displayVariables.get(vm.getSettings().getJoystickXField().getFeatureName()));
        elevator.bind(vm.displayVariables.get(vm.getSettings().getJoystickYField().getFeatureName()));

        //Dont need to do "add listener" because this bind (with value property) doing it behind the scene
        sliderY.valueProperty().bind(vm.displayVariables.get(vm.getSettings().getThrottleField().getFeatureName()));
        sliderX.valueProperty().bind(vm.displayVariables.get(vm.getSettings().getRudderField().getFeatureName()));
    }

    @Override
    public void update(Observable o, Object arg) {
        //System.out.println(aileron);
    }

    private void configureSliders() {
        sliderX.setMin(vm.getSettings().getRudderField().getMinVal());
        sliderX.setMax(vm.getSettings().getRudderField().getMaxVal());
        sliderX.setValue(0);
        sliderY.setMin(vm.getSettings().getThrottleField().getMinVal());
        sliderY.setMax(vm.getSettings().getThrottleField().getMaxVal());
        sliderY.setValue(0);
//limi

    }
}
