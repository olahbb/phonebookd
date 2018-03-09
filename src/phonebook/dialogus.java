package phonebook;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class dialogus        
    {
       
    public void alert(String hiba, String megold)
        {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle("Figyelmeztetés!") ;
        alert.setHeaderText(hiba);
        alert.setContentText(megold);
        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node ->
                            ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
        
        alert.showAndWait();     
        }
    
    public boolean confirm(String kerdes, String megold)
        {
        boolean valasz = false;
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Megerősítés");
        alert.setHeaderText(kerdes);
        alert.setContentText(megold);

        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK)
            valasz = true;
        
        return valasz;
        }
    }