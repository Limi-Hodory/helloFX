package views;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class Joystick extends Pane {


        public final JoyController controller;

        public Joystick() throws IOException {
            FXMLLoader fxl = new FXMLLoader();
            Pane hb = null;
            try {
                hb = fxl.load(getClass().getResource("Joystick.fxml").openStream()); //The file we want to load
                //Now we have an object type HBox that has everything that the FXML defined.
            } catch (IOException e) {
                e.printStackTrace();
                //Cannot load file
            }
            if (hb != null) { //Loading successful
                controller = fxl.getController();
                this.getChildren().add(hb);
            }else
                controller= null;
        }
    }



