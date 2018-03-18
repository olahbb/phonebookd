package phonebook;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
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
   
    public void kivetel(Exception ex, String hiba)
        {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Program kivétel");
        alert.setHeaderText("Kivétel dialóg");
        alert.setContentText(hiba);
        alert.setResizable(true);

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("Kivétel leírása:");
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
        
        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node ->
                            ((Label)node).setMinHeight(Region.USE_PREF_SIZE));

        alert.showAndWait();
        }
    }