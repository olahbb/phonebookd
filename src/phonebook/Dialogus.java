package phonebook;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class Dialogus        
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
   
    public void except()
        {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Look, an Exception Dialog");
        alert.setContentText("Could not find file blabla.txt!");

        Exception ex = new FileNotFoundException("Could not find file blabla.txt");

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
        }
    }