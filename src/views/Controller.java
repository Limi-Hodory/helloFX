package views;

import javafx.scene.control.MenuBar;
import viewModel.ViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {
    @FXML
    Player player;
    @FXML
    Label alt;
    @FXML
    Label airspeed;
    @FXML
    Joystick joystick;
    @FXML
    Graph graph;
    @FXML
    List list;
    @FXML
    MenuBar menuBar;
    @FXML Clocks clocks;

    private Runnable onOpenD, onOpenA;

    ViewModel vm;

    public Controller() {
    }

    public void init(ViewModel vm) {

        // player.controller.onOpen= vm.open;
        player.controller.onPlay = vm.play;
        player.controller.onPause = vm.pause;
        player.controller.onStop = vm.stop;
        player.controller.onMultiply1=vm.multiply1;
        player.controller.onMultiply2=vm.multiply2;

        onOpenD = vm.openD;
        onOpenA = vm.openA;


        joystick.controller.setViewModel(vm);
        list.controller.setViewModel(vm);
        graph.controller.setViewModel(vm);
        clocks.controller.setViewModel(vm);
        player.controller.setViewModel(vm);

        vm.addObserver(list.controller);
        vm.addObserver(joystick.controller);

        //Just make order

        player.setLayoutX(15);
        player.setLayoutY(600);

        joystick.setLayoutX(960);
        joystick.setLayoutY(80);

        list.setLayoutX(50);
        list.setLayoutY(70);

        graph.setLayoutX(230);
        graph.setLayoutY(5);

        clocks.setLayoutX(900);
        clocks.setLayoutY(400);

    }


    public void openFile2() {
        if (onOpenD != null)
            onOpenD.run();
    }

    public void openFile3() {
        if (onOpenA != null)
            onOpenA.run();
    }




    @Override
    public void update(Observable o, Object arg) {
        //  if(o==vm){
        //    this.onOpen= vm.open;
        //this.openFile();}

        //}

    }
}