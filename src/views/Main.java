package views;

import javafx.scene.layout.AnchorPane;
import settings.UserSettings;
import viewModel.ViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application 
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        try {
            FXMLLoader fxl = new FXMLLoader(); // Create a FXML object
            AnchorPane root = fxl.load(getClass().getResource("MainView.fxml").openStream()); //We use "Open stream" so we can access the controller later
            //UserSettings settings = UserSettings.decodeXML("conf.xml");
            Controller mc = fxl.getController();
            ViewModel vm = new ViewModel();

            vm.addObserver(mc);
            mc.init(vm);

            primaryStage.setTitle("Hello World");
            primaryStage.setScene(new Scene(root, 1350, 680));
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        launch(args);	
    }
}
