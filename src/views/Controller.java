package views;

import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import viewModel.ViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer
{
    @FXML Player player;
    @FXML Label alt;
    @FXML Label airspeed;
    @FXML Joystick joystick;
    @FXML Graph graph;
    @FXML List list;
    @FXML MenuBar menuBar;
    @FXML MenuItem csv1;
    @FXML MenuItem csv2;
    @FXML MenuItem algo;

    private Runnable onOpenL, onOpenD, onOpenA;

    ViewModel vm;

    public Controller()
    {
    }
    
    public void init(ViewModel vm)

    {
        alt.textProperty().bind(vm.getProperty(vm.getSettings().getAltitudeField().getFeatureName()).asString()); //Wrap (connect) between alt in view to alt in view model
        //DODO bind to the all properties לעשות ככה לכל התכונות

       // player.controller.onOpen= vm.open;
        player.controller.onPlay= vm.play;
        player.controller.onPause= vm.pause;
        player.controller.onStop= vm.stop;

        onOpenL = vm.openL;
        onOpenD=vm.openD;


        joystick.controller.setViewModel(vm);
        list.controller.setViewModel(vm);
        graph.controller.setViewModel(vm);

        vm.addObserver(list.controller);
        vm.addObserver(joystick.controller);

        //Just make order
        alt.setLayoutX(200);
        airspeed.setLayoutX(250);

        player.setLayoutX(100);
        player.setLayoutY(700);

        joystick.setLayoutX(1100);
        joystick.setLayoutY(80);

        list.setLayoutX(50);
        list.setLayoutY(70);

        graph.setLayoutX(300);
        graph.setLayoutY(100);


    }

    public void openFile() {

        if (csv1.idProperty().get().equals("csv1")) {
            if (onOpenL != null)
                onOpenL.run();
        }

        if (csv2.idProperty().get().equals("csv2")) {
            if (onOpenD != null)
                onOpenD.run();
        }
        if (algo.idProperty().get().equals("algo")) {
            if (onOpenA != null)
                onOpenA.run();
        }



    }

    @Override
    public void update(Observable o, Object arg) {
      //  if(o==vm){
        //    this.onOpen= vm.open;
            //this.openFile();}

        //}

    }
}