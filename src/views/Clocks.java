package views;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import java.io.IOException;


public class Clocks extends Pane {

    public final ClocksController controller;

    public Clocks() throws IOException {
        FXMLLoader fxl = new FXMLLoader();
        Pane hb = null;
        try {
            hb = fxl.load(getClass().getResource("Clocks.fxml").openStream()); //The file we want to load
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
