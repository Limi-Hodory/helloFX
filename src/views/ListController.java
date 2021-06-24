package views;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import viewModel.ViewModel;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("deprecation")
public class ListController implements Observer 
{
    @FXML
    ListView<String> colsList;
    private ViewModel vm;
    
    public void setViewModel(ViewModel vm) 
    { 
    	this.vm = vm; 
    	colsList.getSelectionModel().selectedItemProperty().addListener((observable, oldV, newV) ->
    		{
    			vm.selectFeature(newV);
    		});
    	
    }


    @Override
    public void update(Observable o, Object arg) 
    {
        if(o == vm)
            colsList.setItems(vm.getColsNames());
    }
}
