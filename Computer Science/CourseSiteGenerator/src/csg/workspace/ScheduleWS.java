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
import csg.data.Schedule;
import csg.data.ScheduleData;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;

/**
 *
 * @author KI BBUM
 */
public class ScheduleWS {
    CourseSiteGeneratorApp app;
    SWSController controller;
    
    ScrollPane scheduleWS;
    HBox subPane1;
    HBox subPane2;
    
    VBox scheduleWSVB;
    
    Label scheduleHeader;
    
    VBox cbVBox;
    Label calendarBoundariesSubHeader;
    Label startingMonday;
    DatePicker startingMonDP;
    Label endingFriday;
    DatePicker endingFriDP;
    
    
    VBox scheduleItemsVBox;
    Label scheduleItemsSubHeader;
    Button scheduleItemRemoveButton;
    TableView<Schedule> scheduleItemTable;
    TableColumn<Schedule, String>[] scheduleItemTableColumns;
    VBox scheduleItemsTableVBox;
    
    Label addEditSubHeader;
    Label type;
    ComboBox typeCB;
    Label date;
    DatePicker datePicker;
    Label time;
    TextField timeTF;
    Label title;
    TextField titleTF;
    Label topic;
    TextField topicTF;
    Label link;
    TextField linkTF;
    Label criteria;
    TextField criteriaTF;
    Button addUpdateButton;
    Button addButton;
    Button updateButton;
    Button clearButton;
    GridPane addEditGridPane;
    
    
    
    ArrayList<Node> headers;
    ArrayList<Node> subHeaders;
    ArrayList<Node> labels;
    ArrayList<Node> comboBoxes;
    ArrayList<Node> textFields;
    ArrayList<Node> buttons;
    ArrayList<Node> subPanes;
    
    boolean isUpdateMode;
    
    
    public ScheduleWS(CourseSiteGeneratorApp initApp){
        // KEEP THIS FOR LATER
        app = initApp;
        
        CSGData dataComp = (CSGData) app.getDataComponent();
        ScheduleData data = (ScheduleData)dataComp.getScheduleData();
        
        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        String scheduleHeaderText = props.getProperty(CourseSiteGeneratorProp.ST_SCHEDULE_HEADER.toString());
        scheduleHeader = new Label(scheduleHeaderText);
        
        String calendarBoundariesSubHeaderText = props.getProperty(CourseSiteGeneratorProp.ST_CALENDAR_BOUNDARIES_SUBHEADER.toString());
        calendarBoundariesSubHeader = new Label(calendarBoundariesSubHeaderText);
        
        String startingMondayText = props.getProperty(CourseSiteGeneratorProp.ST_STARTING_MONDAY.toString());
        startingMonday = new Label(startingMondayText);
        startingMonDP = data.getStartingMonDP();
        
        String endingFridayText = props.getProperty(CourseSiteGeneratorProp.ST_ENDING_FRIDAY.toString());
        endingFriday = new Label(endingFridayText);
        endingFriDP = data.getEndingFriDP();
        
        String scheduleItemsSubHeaderText = props.getProperty(CourseSiteGeneratorProp.ST_SCHEDULE_ITEMS_SUBHEADER.toString());
        scheduleItemsSubHeader = new Label(scheduleItemsSubHeaderText);
        
        String scheduleItemRemoveButtonText = props.getProperty(CourseSiteGeneratorProp.REMOVE_BUTTON.toString());
        scheduleItemRemoveButton = new Button(scheduleItemRemoveButtonText);
        
        //TABLE
        scheduleItemTable = new TableView();
        scheduleItemTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ObservableList<Schedule> schedules = data.getSchedules();
        scheduleItemTable.setItems(schedules);
        String[] valueFact = {"type","date","title","topic"};
        ArrayList<String> recitationTableHeaders = props.getPropertyOptionsList(CourseSiteGeneratorProp.ST_SCHEDULE_ITEMS_TABLE_HEADERS);
        scheduleItemTableColumns = new TableColumn[recitationTableHeaders.size()];
        for ( int i = 0; i < scheduleItemTableColumns.length; i++ ){
            scheduleItemTableColumns[i] = new TableColumn(recitationTableHeaders.get(i));
            scheduleItemTableColumns[i].setCellValueFactory(
                new PropertyValueFactory<Schedule, String>(valueFact[i])
            );
            scheduleItemTable.getColumns().add(scheduleItemTableColumns[i]);
        }
        scheduleItemTable.setPrefHeight(200);
        
        
        String addEditSubHeaderText = props.getProperty(CourseSiteGeneratorProp.ST_ADD_EDIT_SUBHEADER.toString());
        addEditSubHeader = new Label(addEditSubHeaderText);
        
        String typeText = props.getProperty(CourseSiteGeneratorProp.ST_TYPE.toString());
        type = new Label(typeText);
        typeCB = data.getTypeCB();
        
        String dateText = props.getProperty(CourseSiteGeneratorProp.ST_DATE.toString());
        date = new Label(dateText);
        datePicker = data.getDateDP();
        
        String timeText = props.getProperty(CourseSiteGeneratorProp.ST_TIME.toString());
        time = new Label(timeText);
        timeTF = data.getTimeTF();
        timeTF.setPrefWidth(280);
        
        String titleText = props.getProperty(CourseSiteGeneratorProp.ST_TITLE.toString());
        title = new Label(titleText);
        titleTF = data.getTitleTF();
        
        String topicText = props.getProperty(CourseSiteGeneratorProp.ST_TOPIC.toString());
        topic = new Label(topicText);
        topicTF = data.getTopicTF();
        
        String linkText = props.getProperty(CourseSiteGeneratorProp.ST_LINK.toString());
        link = new Label(linkText);
        linkTF = data.getLinkTF();
        
        String criteriaText = props.getProperty(CourseSiteGeneratorProp.ST_CRITERIA.toString());
        criteria = new Label(criteriaText);
        criteriaTF = data.getCriteriaTF();
        
        String addUpdateButtonText = props.getProperty(CourseSiteGeneratorProp.ADD_UPDATE_BUTTON.toString());
        addUpdateButton = new Button(addUpdateButtonText);
        
        String addButtonText = props.getProperty(CourseSiteGeneratorProp.ADD_BUTTON_TEXT.toString());
        String updateButtonText = props.getProperty(CourseSiteGeneratorProp.UPDATE_BUTTON_TEXT.toString());
        addButton = new Button(addButtonText);
        updateButton = new Button(updateButtonText);
        
        String clearButtonText = props.getProperty(CourseSiteGeneratorProp.CLEAR_BUTTON.toString());
        clearButton = new Button(clearButtonText);
        
        
        // BUILD THE WORKSPACE
        cbVBox = new VBox(10);
        HBox smHB = new HBox(5);
        smHB.setMinWidth(400);
        smHB.getChildren().addAll(startingMonday, startingMonDP);
        //space
        HBox spacer = new HBox();
        spacer.setMinWidth(45);
        smHB.getChildren().add(spacer);
        smHB.getChildren().addAll(endingFriday, endingFriDP);
        cbVBox.getChildren().addAll(calendarBoundariesSubHeader, smHB);
        
        // SCHEDULE ITEMS PART
        HBox siHeaderButton = new HBox(10);
        siHeaderButton.getChildren().addAll(scheduleItemsSubHeader, scheduleItemRemoveButton);
        VBox siHeaderNTable = new VBox(10);
        siHeaderNTable.getChildren().addAll(siHeaderButton, scheduleItemTable);
        
        addEditGridPane = new GridPane();
        addEditGridPane.setHgap(10);
        addEditGridPane.setVgap(10);
        
        addEditGridPane.add(addEditSubHeader, 0, 0);
        addEditGridPane.add(type, 0, 1);
        addEditGridPane.add(typeCB, 1, 1);
        addEditGridPane.add(date, 0, 2);
        addEditGridPane.add(datePicker, 1, 2);
        addEditGridPane.add(time, 0, 3);
        addEditGridPane.add(timeTF, 1, 3);
        addEditGridPane.add(title, 0, 4);
        addEditGridPane.add(titleTF, 1, 4);
        addEditGridPane.add(topic, 0, 5);
        addEditGridPane.add(topicTF, 1, 5);
        addEditGridPane.add(link, 0, 6);
        addEditGridPane.add(linkTF, 1, 6);
        addEditGridPane.add(criteria, 0, 7);
        addEditGridPane.add(criteriaTF, 1, 7);
        HBox buttonsHBox = new HBox(5);
        buttonsHBox.getChildren().addAll(addButton, clearButton);
        addEditGridPane.add(buttonsHBox, 0, 8, 2, 1);
        
        scheduleItemsVBox = new VBox(40);
        scheduleItemsVBox.getChildren().addAll(siHeaderNTable, addEditGridPane);
        
        scheduleWSVB = new VBox(12);
        scheduleWSVB.setPadding(new Insets(10, 0, 15, 0));
        scheduleWSVB.setMaxWidth(800);
        subPane1 = new HBox(cbVBox);
        subPane1.setMargin(cbVBox, new Insets(15, 21, 17, 21));
        subPane2 = new HBox(scheduleItemsVBox);
        subPane2.setMargin(scheduleItemsVBox, new Insets(15, 21, 17, 21));
        scheduleWSVB.getChildren().addAll(scheduleHeader, subPane1, subPane2);
        
        // TABLE WIDTH
        scheduleItemTable.prefWidthProperty().bind(scheduleWSVB.widthProperty());
        
        scheduleWS = new ScrollPane();
        StackPane p = new StackPane();
        p.prefWidthProperty().bind(scheduleWS.widthProperty());
        p.setAlignment(Pos.CENTER);
        p.getChildren().add(scheduleWSVB);
        
        scheduleWS.setContent(p);
        
        // SET EVENT HANDLING
        controller = new SWSController(app);
        
        addButton.setOnAction(e -> {
            controller.addSchedule();
            
            typeCB.getSelectionModel().clearSelection();
            datePicker.setValue(null);
            timeTF.setText("");
            titleTF.setText("");
            topicTF.setText("");
            linkTF.setText("");
            criteriaTF.setText("");
        });
        
        scheduleItemRemoveButton.setOnAction(e -> {
            controller.deleteSchedule();
        });
        
        updateButton.setOnAction(e -> {
            controller.updateSchedule();
        });
        
        
        //###### clear button
        clearButton.setOnAction(e -> {
            typeCB.getSelectionModel().clearSelection();
            datePicker.setValue(null);
            timeTF.setText("");
            titleTF.setText("");
            topicTF.setText("");
            linkTF.setText("");
            criteriaTF.setText("");
            scheduleItemTable.getSelectionModel().clearSelection();
            if ( isUpdateMode ){
                buttonsHBox.getChildren().remove(updateButton);
                buttonsHBox.getChildren().remove(clearButton);
                buttonsHBox.getChildren().add(addButton);
                buttonsHBox.getChildren().add(clearButton);
                isUpdateMode = false;
            }
        });
        
        //##### change add button to Update button
        scheduleItemTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if ( newSelection != null ){
                Workspace workspaceProp = (Workspace)app.getWorkspaceComponent();
                ScheduleWS workspace = workspaceProp.getScheduleWS();
                
                TableView scheTable = workspace.getTable();
                Schedule tempSchedule = (Schedule)scheTable.getSelectionModel().getSelectedItem();
                
                typeCB.getSelectionModel().select(tempSchedule.getType());
                String[] date = tempSchedule.getDate().split("/");
                datePicker.setValue(LocalDate.of(Integer.parseInt(date[2]), Integer.parseInt(date[0]), Integer.parseInt(date[1])));
                timeTF.setText(tempSchedule.getTime());
                titleTF.setText(tempSchedule.getTitle());
                topicTF.setText(tempSchedule.getTopic());
                linkTF.setText(tempSchedule.getLink());
                criteriaTF.setText(tempSchedule.getCriteria());
                
                if ( !isUpdateMode ){
                    buttonsHBox.getChildren().remove(addButton);
                    buttonsHBox.getChildren().remove(clearButton);
                    buttonsHBox.getChildren().add(updateButton);
                    buttonsHBox.getChildren().add(clearButton);
                    isUpdateMode = true;
                }
            }
        });
        
        
        scheduleWS.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.DELETE)) {
                controller.deleteSchedule();
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
        headers.add(scheduleHeader);
        return headers;
    }
    
    public ArrayList<Node> getSubHeaders(){
        subHeaders = new ArrayList<Node>();
        subHeaders.add(calendarBoundariesSubHeader);
        subHeaders.add(scheduleItemsSubHeader);
        subHeaders.add(addEditSubHeader);
        return subHeaders;
    }
    
    public ArrayList<Node> getLabels(){
        labels = new ArrayList<Node>();
        labels.add(startingMonday);
        labels.add(endingFriday);
        labels.add(addEditSubHeader);
        labels.add(type);
        labels.add(date);
        labels.add(time);
        labels.add(title);
        labels.add(topic);
        labels.add(link);
        labels.add(criteria);
        return labels;
    }
    
    public ArrayList<Node> getComboBoxes(){
        comboBoxes = new ArrayList<Node>();
        comboBoxes.add(typeCB);
        return comboBoxes;
    }
    
    public ArrayList<Node> getTextFields(){
        textFields = new ArrayList<Node>();
        textFields.add(timeTF);
        textFields.add(titleTF);
        textFields.add(topicTF);
        textFields.add(linkTF);
        textFields.add(criteriaTF);
        return textFields;
    }
    
    public ArrayList<Node> getButtons(){
        buttons = new ArrayList<Node>();
        buttons.add(scheduleItemRemoveButton);
        buttons.add(addUpdateButton);
        buttons.add(addButton);
        buttons.add(updateButton);
        buttons.add(clearButton);
        return buttons;
    }
    
    public TableView getTable(){
        return scheduleItemTable;
    }
    
    public ArrayList<Node> getsubPanes(){
        subPanes = new ArrayList<Node>();
        subPanes.add(subPane1);
        subPanes.add(subPane2);
        return subPanes;
    }
    
    
    public ScrollPane getSDWS(){
        return scheduleWS;
    }
    
    
}
