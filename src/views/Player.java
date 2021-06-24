package views;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;


public class Player extends HBox { //needs to extends javaFX Class (HBox)

    public final PlayerController controller; //The controller is a data member (final- we have to init in the Ctor)
// The controller is public because we want to allow access to his runnable obj but we want to protect it with final.

    public Player() throws IOException {
        FXMLLoader fxl = new FXMLLoader();
        HBox hb = null;
        try {
            hb = fxl.load(getClass().getResource("Player.fxml").openStream()); //The file we want to load
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
}//Now we have an object (player) that we can add to the mainFxml & we have an access to the controller objects.
