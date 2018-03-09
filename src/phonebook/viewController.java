package phonebook;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public class viewController implements Initializable 
    {

    @FXML
    StackPane menuPane;
    
    @FXML
    SplitPane mainSplit;

    @FXML
    Pane exportPane;
    
    @FXML
    AnchorPane anchor;

    @FXML
    Pane contactPane;

    @FXML
    TableView table;

    @FXML
    TextField inputLastName;

    @FXML
    TextField inputFirstName;

    @FXML
    TextField inputEmail;
    
    @FXML
    TextField inputExportName;

    @FXML
    Button addNewContactButton;
    
    @FXML
    Button exportButton;
     
    private final String MENU_CONTACTS = "Kontaktok";
    private final String MENU_LIST = "Lista";
    private final String MENU_EXPORT = "Exportálás";
    private final String MENU_EXIT = "Kilépés";

    DB db = new DB();
    dialogus dialog = new dialogus();
    private final ObservableList<Person> data
            = FXCollections.observableArrayList();
    
    private void mainSplitSet(boolean disable, double opacity)
        {
        mainSplit.setDisable(disable);
        mainSplit.setOpacity(opacity);
        }

    @FXML
    private void addContact(ActionEvent event)
        {
        String email = inputEmail.getText();
        
        if(email.length() > 3 && email.contains("@") && email.contains("."))
            {
            Person newPerson = new Person(inputLastName.getText(), inputFirstName.getText(), email);
            data.add(newPerson);
            db.addContact(newPerson);  
            inputLastName.clear();
            inputFirstName.clear();
            inputEmail.clear();
            }
        else
            {
            mainSplitSet(true, 0.4);
            dialog.alert("Adj meg egy valódi e-mail címet!", "Az e-mail címnek minimum 3 karakternek kell lennie");
            mainSplitSet(false, 1);
            }            
        }
    
    @FXML
    private void exportList(ActionEvent event)
        {
        String fileName = inputExportName.getText();
        fileName = fileName.replaceAll("\\s", "");
        
        if(fileName != null && !fileName.equals(""))
            {
            PdfGeneration pdfCreator = new PdfGeneration();
            pdfCreator.pdfGeneration(fileName, data);
            }
        
        else
            {
            mainSplitSet(true, 0.4);
            dialog.alert("Adj meg egy fájlnevet",  "");
            mainSplitSet(false, 1);
            }
        }
    
    public void setTableData() 
        {
        TableColumn lastNameCol = new TableColumn("Vezetéknév");
        lastNameCol.setMinWidth(130);
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
        
        lastNameCol.setOnEditCommit
            (
            new EventHandler<TableColumn.CellEditEvent<Person, String>>()   
                {
                @Override
                public void handle(TableColumn.CellEditEvent<Person, String> t)
                    {
                    Person actualPerson = (Person) t.getTableView().getItems().get(t.getTablePosition().getRow());
                    actualPerson.setLastName(t.getNewValue());
                    db.updateContact(actualPerson);
                    }                
                }
            );
                           
        TableColumn firstNameCol = new TableColumn("Keresztév");
        firstNameCol.setMinWidth(130);
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
        
        firstNameCol.setOnEditCommit
            (
            new EventHandler<TableColumn.CellEditEvent<Person, String>>()
                {
                @Override
                public void handle(TableColumn.CellEditEvent<Person, String> t)
                    {
                    Person actualPerson = (Person) t.getTableView().getItems().get(t.getTablePosition().getRow());
                    actualPerson.setFirstName(t.getNewValue());
                    db.updateContact(actualPerson);
                    }
                
                }
            );
                           
        TableColumn emailCol = new TableColumn("E-mai cím");
        emailCol.setMinWidth(220);
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setCellValueFactory(new PropertyValueFactory<Person, String>("email")); 

        emailCol.setOnEditCommit
            (
            new EventHandler<TableColumn.CellEditEvent<Person, String>>()
                {
                @Override
                public void handle(TableColumn.CellEditEvent<Person, String> t)
                    {
                    Person actualPerson = (Person) t.getTableView().getItems().get(t.getTablePosition().getRow());
                    actualPerson.setEmail(t.getNewValue());
                    db.updateContact(actualPerson);
                    }
                }
            );
        
        TableColumn removeCol = new TableColumn("Törlés");
        removeCol.setMinWidth(100);
        
        Callback<TableColumn<Person, String>, TableCell<Person, String>> cellFactory=
                new Callback<TableColumn<Person, String>, TableCell<Person, String>>() 
            {
            @Override
            public TableCell call(final TableColumn<Person, String> param)    
                {
                final TableCell<Person, String> cell = new TableCell<Person, String>()
                    {
                    final Button btn = new Button("Törlés");
                        
                    @Override
                    public void updateItem(String item, boolean empty)
                        {
                        super.updateItem(item, empty);
                        
                        if(empty)
                            {
                            setGraphic(null);
                            setText(null);
                            }
                        else
                            {
                            btn.setOnAction((ActionEvent event) ->
                                    {
                                    boolean valasz = dialog.confirm("Törlés", "Biztosan törli a kontaktot?");
                                    
                                    if(valasz)
                                        {
                                        mainSplitSet(true, 0.4);
                                        Person person = getTableView().getItems().get(getIndex());
                                        data.remove(person);
                                        db.removeContact(person);
                                        mainSplitSet(false, 1); 
                                        }
                                    }
                                );
                                setGraphic(btn);
                                setText(null);
                            }
                        }
                    };
                return  cell;
                }
            };
        
        removeCol.setCellFactory(cellFactory);
                           
        table.getColumns().addAll(lastNameCol, firstNameCol, emailCol, removeCol);
        data.addAll(db.getAllContacts());
        table.setItems(data);
        }
    
    private void setMenuData() 
        {
        TreeItem<String> treeItemRoot1 = new TreeItem<>("Menü");
        TreeView<String> treeView = new TreeView<>(treeItemRoot1);
        treeView.setShowRoot(false); 
        
        TreeItem<String> nodeItemA = new TreeItem<>(MENU_CONTACTS);
        TreeItem<String> nodeItemB = new TreeItem<>(MENU_EXIT);
        
        nodeItemA.setExpanded(true);
        
        Node contactsNode = new ImageView(new Image(getClass().getResourceAsStream("/contacts.png")));
        Node exportNode = new ImageView(new Image(getClass().getResourceAsStream("/export.png")));
                
        
        TreeItem<String> nodeItemA1 = new TreeItem<>(MENU_LIST, contactsNode);
        TreeItem<String> nodeItemA2 = new TreeItem(MENU_EXPORT, exportNode);
        
        nodeItemA.getChildren().addAll(nodeItemA1, nodeItemA2);
        treeItemRoot1.getChildren().addAll(nodeItemA, nodeItemB);
        
        menuPane.getChildren().add(treeView); 
        
        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
                {
                public void changed(ObservableValue observable, Object oldValue, Object newValue)
                    {
                    TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                    String selectedMenu;
                    selectedMenu = selectedItem.getValue();
                    
                    if(null != selectedMenu)
                        {
                        switch(selectedMenu)
                            {
                            case MENU_CONTACTS:
                                selectedItem.setExpanded(true);
                                break;
                            case MENU_LIST:
                                contactPane.setVisible(true);
                                exportPane.setVisible(false);
                                break;
                            case MENU_EXPORT:
                                contactPane.setVisible(false);
                                exportPane.setVisible(true);
                                break;
                            case MENU_EXIT:
                                System.exit(0);
                                break;
                            }
                        }
                    }
                }
            );
        }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
        {
        setTableData();
        setMenuData(); 
        }
   } 