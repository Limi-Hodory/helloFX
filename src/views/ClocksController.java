package views;

import eu.hansolo.medusa.Gauge;
import javafx.fxml.FXML;
import viewModel.ViewModel;

public class ClocksController
{
    @FXML Gauge altimeterClock;
    @FXML Gauge airSpeedClock;
    @FXML Gauge directionClock;
    @FXML Gauge pitchClock;
    @FXML Gauge rollClock;
    @FXML Gauge yawClock;
    private ViewModel vm;

    public void setViewModel(ViewModel vm)
    {
        this.vm = vm;

        altimeterClock.valueProperty().bind(vm.displayVariables.get(vm.getSettings().getAltitudeField().getFeatureName()));
        airSpeedClock.valueProperty().bind(vm.displayVariables.get(vm.getSettings().getSpeedField().getFeatureName()));
        directionClock.valueProperty().bind(vm.displayVariables.get(vm.getSettings().getDirectionField().getFeatureName()));
        pitchClock.valueProperty().bind(vm.displayVariables.get(vm.getSettings().getPitchField().getFeatureName()));
        rollClock.valueProperty().bind(vm.displayVariables.get(vm.getSettings().getRollField().getFeatureName()));
        yawClock.valueProperty().bind(vm.displayVariables.get(vm.getSettings().getYawField().getFeatureName()));
    }


}
