/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import csg.CourseSiteGeneratorApp;
import csg.CourseSiteGeneratorProp;
import csg.data.CSGData;
import csg.data.Recitation;
import csg.data.RecitationData;
import csg.data.SitePage;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;

/**
 *
 * @author KI BBUM
 */
public class RecitationWS {
    CourseSiteGeneratorApp app;
    RWSController controller;    
    
    ScrollPane recitationWS;
    HBox subPane1;
    
    
    Label recitationsHeader;
    Button removeButton;
    HBox recitationHeaderHBox;
    TableView<Recitation> recitationTable;
    TableColumn<Recitation, String>[] recitationTableColumns;
    GridPane addEditGridPane;
    Label addEditSubheader;
    Label section;
    TextField sectionTF;
    Label instructor;
    TextField instructorTF;
    Label dayTime;
    TextField dayTimeTF;
    Label location;
    TextField locationTF;
    Label supervisingTA1;
    ComboBox supervisingTA1CB;
    Label supervisingTA2;
    ComboBox supervisingTA2CB;
    Button addUpdateButton;
    Button addButton;
    Button updateButton;
    Button clearButton;
    HBox buttonHBox;
    
    Recitation tempRecitation;
    
    ArrayList<Node> headers;
    ArrayList<Node> subHeaders;
    ArrayList<Node> labels;
    ArrayList<Node> comboBoxes;
    ArrayList<Node> textFields;
    ArrayList<Node> buttons;
    ArrayList<Node> subPanes;
    
    boolean isUpdateMode;
    
    
    public RecitationWS(CourseSiteGeneratorApp initApp){
        // KEEP THIS FOR LATER
        app = initApp;
        
        CSGData dataComp = (CSGData) app.getDataComponent();
        RecitationData data = dataComp.getRecitationData();
        
        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // SET THE PROPERTIES
        String recitationsHeaderText = props.getProperty(CourseSiteGeneratorProp.RT_RECITATIONS_HEADER.toString());
        recitationsHeader = new Label(recitationsHeaderText);
        String removeButtonText = props.getProperty(CourseSiteGeneratorProp.REMOVE_BUTTON.toString());
        removeButton = new Button(removeButtonText);
        HBox rb = new HBox(removeButton);
        rb.setAlignment(Pos.CENTER);
        removeButton.setAlignment(Pos.BOTTOM_CENTER);
        recitationHeaderHBox = new HBox(10);
        recitationHeaderHBox.getChildren().addAll(recitationsHeader, rb);
        
        //TABLE
        recitationTable = new TableView();
        recitationTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ObservableList<Recitation> recitations = data.getRecitations();
        recitationTable.setItems(recitations);
        String[] valueFact = {"section","instructor","dayTime","location", "ta1", "ta2"};
        ArrayList<String> recitationTableHeaders = props.getPropertyOptionsList(CourseSiteGeneratorProp.RT_RECITATION_TABLE_HEADERS);
        recitationTableColumns = new TableColumn[recitationTableHeaders.size()];
        for ( int i = 0; i < recitationTableColumns.length; i++ ){
            recitationTableColumns[i] = new TableColumn(recitationTableHeaders.get(i));
            recitationTableColumns[i].setCellValueFactory(
                new PropertyValueFactory<Recitation, String>(valueFact[i])
            );
            recitationTable.getColumns().add(recitationTableColumns[i]);
        }
        recitationTable.setPrefHeight(250);
        
        String addEditSubheaderText = props.getProperty(CourseSiteGeneratorProp.RT_ADD_EDIT_SUBHEADER.toString());
        addEditSubheader = new Label(addEditSubheaderText);
        
        String sectionText = props.getProperty(CourseSiteGeneratorProp.RT_SECTION.toString());
        section = new Label(sectionText);
        sectionTF = data.getSectionsTF();
        sectionTF.setPrefWidth(250);
        
        String instructorText = props.getProperty(CourseSiteGeneratorProp.RT_INSTRUCTOR.toString());
        instructor = new Label(instructorText);
        instructorTF = data.getInstructorTF();
        
        String dayTimeText = props.getProperty(CourseSiteGeneratorProp.RT_DAY_TIME.toString());
        dayTime = new Label(dayTimeText);
        dayTimeTF = data.getDayTimeTF();
        
        String locationText = props.getProperty(CourseSiteGeneratorProp.RT_LOCATION.toString());
        location = new Label(locationText);
        locationTF = data.getLocationTF();
        
        String supervisingTA1Text = props.getProperty(CourseSiteGeneratorProp.RT_SUPERVISING_TA.toString());
        supervisingTA1 = new Label(supervisingTA1Text);
        supervisingTA1CB = data.getTACB1();
        
        String supervisingTA2Text = props.getProperty(CourseSiteGeneratorProp.RT_SUPERVISING_TA.toString());
        supervisingTA2 = new Label(supervisingTA2Text);
        supervisingTA2CB = data.getTACB2();
        
        
        String addButtonText = props.getProperty(CourseSiteGeneratorProp.ADD_BUTTON_TEXT.toString());
        String updateButtonText = props.getProperty(CourseSiteGeneratorProp.UPDATE_BUTTON_TEXT.toString());
        addButton = new Button(addButtonText);
        updateButton = new Button(updateButtonText);
        
        //String addUpdateButtonText = props.getProperty(CourseSiteGeneratorProp.ADD_UPDATE_BUTTON.toString());
        //addUpdateButton = addButton;
        String clearButtonText = props.getProperty(CourseSiteGeneratorProp.CLEAR_BUTTON.toString());
        clearButton = new Button(clearButtonText);
        buttonHBox = new HBox(20);
        buttonHBox.getChildren().addAll(addButton, clearButton);
        
        
        // BUILD WORKSPACE
        
        // ADD/ EDIT Part
        addEditGridPane = new GridPane();
        addEditGridPane.setHgap(10);
        addEditGridPane.setVgap(20);
        
        addEditGridPane.add(addEditSubheader, 0, 0);
        addEditGridPane.add(section, 0, 1);
        addEditGridPane.add(sectionTF, 1, 1);
        addEditGridPane.add(instructor, 0, 2);
        addEditGridPane.add(instructorTF, 1, 2);
        addEditGridPane.add(dayTime, 0, 3);
        addEditGridPane.add(dayTimeTF, 1, 3);
        addEditGridPane.add(location, 0, 4);
        addEditGridPane.add(locationTF, 1, 4);
        addEditGridPane.add(supervisingTA1, 0, 5);
        addEditGridPane.add(supervisingTA1CB, 1, 5);
        addEditGridPane.add(supervisingTA2, 0, 6);
        addEditGridPane.add(supervisingTA2CB, 1, 6);
        addEditGridPane.add(buttonHBox, 0, 7, 2, 1);
        
        VBox recitationHeaderTable = new VBox(12);
        recitationHeaderTable.getChildren().addAll(recitationHeaderHBox, recitationTable);
        
        VBox recitationWSVB = new VBox(12);
        recitationWSVB.setPadding(new Insets(10, 0, 15, 0));
        recitationWSVB.setMaxWidth(800);
        subPane1 = new HBox(addEditGridPane);
        subPane1.setMargin(addEditGridPane, new Insets(15, 21, 17, 21));
        recitationWSVB.getChildren().addAll(recitationHeaderTable, subPane1);
        
        recitationWS = new ScrollPane();
        StackPane p = new StackPane();
        p.prefWidthProperty().bind(recitationWS.widthProperty());
        p.setAlignment(Pos.CENTER);
        p.getChildren().add(recitationWSVB);
        recitationWS.setContent(p);
        
        
        // SET EVENT HANDLERING
        controller = new RWSController(app);
        
        addButton.setOnAction(e -> {
            controller.addRecitation();
            
            sectionTF.setText("");
            instructorTF.setText("");
            dayTimeTF.setText("");
            locationTF.setText("");
            supervisingTA1CB.getSelectionModel().clearSelection();
            supervisingTA2CB.getSelectionModel().clearSelection();
            sectionTF.requestFocus();
        });
        
        removeButton.setOnAction(e -> {
            controller.deleteRecitation();
        });
        
        updateButton.setOnAction(e -> {
            controller.updateRecitation();
        });
        
        //###### clear button
        clearButton.setOnAction(e -> {
            recitationTable.getSelectionModel().clearSelection();
            sectionTF.setText("");
            instructorTF.setText("");
            dayTimeTF.setText("");
            locationTF.setText("");
            supervisingTA1CB.getSelectionModel().clearSelection();
            supervisingTA2CB.getSelectionModel().clearSelection();
            sectionTF.requestFocus();
            if ( isUpdateMode ){
                buttonHBox.getChildren().remove(updateButton);
                buttonHBox.getChildren().remove(clearButton);
                buttonHBox.getChildren().add(addButton);
                buttonHBox.getChildren().add(clearButton);
                isUpdateMode = false;
            }
        });
        
        //##### change add button to Update button
        recitationTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if ( newSelection != null ){
                Workspace workspaceProp = (Workspace)app.getWorkspaceComponent();
                RecitationWS workspace = workspaceProp.getRecitationWS();
                
                TableView rcTable = workspace.getTable();
                
                tempRecitation = (Recitation)rcTable.getSelectionModel().getSelectedItem();
                sectionTF.setText(tempRecitation.getSection());
                instructorTF.setText(tempRecitation.getInstructor());
                dayTimeTF.setText(tempRecitation.getDayTime());
                locationTF.setText(tempRecitation.getLocation());
                supervisingTA1CB.getSelectionModel().select(data.getTANameoOf(tempRecitation.getTa1()));
                supervisingTA2CB.getSelectionModel().select(data.getTANameoOf(tempRecitation.getTa2()));
                
                if ( !isUpdateMode ){
                    buttonHBox.getChildren().remove(addButton);
                    buttonHBox.getChildren().remove(clearButton);
                    buttonHBox.getChildren().add(updateButton);
                    buttonHBox.getChildren().add(clearButton);
                    isUpdateMode = true;
                }
            }
        });
        
        recitationWSVB.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.DELETE)) {
                controller.deleteRecitation();
            }
            else if(e.isControlDown())
                if(e.getCode() == KeyCode.Z)
                    controller.undo();
                else if(e.getCode() == KeyCode.Y)
                    controller.redo();
        });
        
        
    }
    
    
    // get methods for style
    public ArrayList<Node> getHeaders(){
        headers = new ArrayList<Node>();
        headers.add(recitationsHeader);
        return headers;
    }
    
    public ArrayList<Node> getSubHeaders(){
        subHeaders = new ArrayList<Node>();
        subHeaders.add(addEditSubheader);
        return subHeaders;
    }
    
    public ArrayList<Node> getLabels(){
        labels = new ArrayList<Node>();
        labels.add(section);
        labels.add(instructor);
        labels.add(dayTime);
        labels.add(location);
        labels.add(supervisingTA1);
        labels.add(supervisingTA2);
        return labels;
    }
    
    public ArrayList<Node> getComboBoxes(){
        comboBoxes = new ArrayList<Node>();
        comboBoxes.add(supervisingTA1CB);
        comboBoxes.add(supervisingTA2CB);
        return comboBoxes;
    }
    
    public ArrayList<Node> getTextFields(){
        textFields = new ArrayList<Node>();
        textFields.add(sectionTF);
        textFields.add(instructorTF);
        textFields.add(dayTimeTF);
        textFields.add(locationTF);
        return textFields;
    }
    
    public ArrayList<Node> getButtons(){
        buttons = new ArrayList<Node>();
        buttons.add(removeButton);
        //buttons.add(addUpdateButton);
        buttons.add(addButton);
        buttons.add(updateButton);
        buttons.add(clearButton);
        return buttons;
    }
    
    public TableView getTable(){
        return recitationTable;
    }
    
    public ArrayList<Node> getsubPanes(){
        subPanes = new ArrayList<Node>();
        subPanes.add(subPane1);
        return subPanes;
    }
    
    
    public ScrollPane getRDWS(){
        return recitationWS;
    }
    
    
}
