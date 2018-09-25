/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import csg.CourseSiteGeneratorApp;
import csg.CourseSiteGeneratorProp;
import csg.data.TeachingAssistant;
import csg.data.TAData;
import csg.data.CSGData;
import csg.style.CSGStyle;
import csg.style.TAStyle;
import djf.components.AppDataComponent;
import djf.controller.AppFileController;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import properties_manager.PropertiesManager;

/**
 *
 * @author KI BBUM
 */
public class TAWorkspace {
    // THIS PROVIDES US WITH ACCESS TO THE APP COMPONENTS
    CourseSiteGeneratorApp app;
    
    // THIS PROVIDES RESPONSES TO INTERACTIONS WITH THIS WORKSPACE
    TAController controller;

    // NOTE THAT EVERY CONTROL IS PUT IN A BOX TO HELP WITH ALIGNMENT
    
    BorderPane taWS;
    
    // FOR THE HEADER ON THE LEFT
    HBox tasHeaderBox;
    Label tasHeaderLabel;
    Button removeButton;
    
    // FOR THE TA TABLE
    TableView<TeachingAssistant> taTable;
    TableColumn<TeachingAssistant, Boolean> undergradColumn;
    TableColumn<TeachingAssistant, String> nameColumn;
    //*****************************************************************************
    TableColumn<TeachingAssistant, String> emailColumn;

    // THE TA INPUT
    HBox addBox;
    HBox undergradCheckBox;
    CheckBox undergrad;
    TextField nameTextField;
    //******************************************************************************
    TextField emailTextField;
    Button addButton;
    Button updateButton;
    TeachingAssistant tempTA;
    boolean isUpdateMode;
    Button clearButton;
    

    // THE HEADER ON THE RIGHT
    HBox officeHoursHeaderBox;
    Label officeHoursHeaderLabel;
    
    // COMBOBOX
    HBox officeHoursComboBox;
    Label startComboBoxLabel;
    Label endComboBoxLabel;
    ComboBox startTimeComboBox;
    ComboBox endTimeComboBox;
    Button changeHours;
    int tempStartHour;
    int tempEndHour;
    
    // THE OFFICE HOURS GRID
    GridPane officeHoursGridPane;
    HashMap<String, Pane> officeHoursGridTimeHeaderPanes;
    HashMap<String, Label> officeHoursGridTimeHeaderLabels;
    HashMap<String, Pane> officeHoursGridDayHeaderPanes;
    HashMap<String, Label> officeHoursGridDayHeaderLabels;
    HashMap<String, Pane> officeHoursGridTimeCellPanes;
    HashMap<String, Label> officeHoursGridTimeCellLabels;
    HashMap<String, Pane> officeHoursGridTACellPanes;
    HashMap<String, Label> officeHoursGridTACellLabels;

    /**
     * The contstructor initializes the user interface, except for
     * the full office hours grid, since it doesn't yet know what
     * the hours will be until a file is loaded or a new one is created.
     */
    public TAWorkspace(CourseSiteGeneratorApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;

        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // INIT THE HEADER ON THE LEFT
        tasHeaderBox = new HBox(10);
        String tasHeaderText = props.getProperty(CourseSiteGeneratorProp.TAS_HEADER_TEXT.toString());
        tasHeaderLabel = new Label(tasHeaderText);
        String removeButtonText = props.getProperty(CourseSiteGeneratorProp.REMOVE_BUTTON.toString());
        removeButton = new Button(removeButtonText);
        HBox rb = new HBox(removeButton);
        rb.setAlignment(Pos.CENTER);
        removeButton.setAlignment(Pos.BOTTOM_CENTER);
        tasHeaderBox.getChildren().addAll(tasHeaderLabel, rb);

        // MAKE THE TABLE AND SETUP THE DATA MODEL
        taTable = new TableView();
        taTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        CSGData dataComp = (CSGData) app.getDataComponent();
        TAData data = dataComp.getTAData();
        ObservableList<TeachingAssistant> tableData = data.getTeachingAssistants();
        taTable.setItems(tableData);
        String undergradColumnText = props.getProperty(CourseSiteGeneratorProp.UNDERGRAD_COLUMN_TEXT.toString());
        String nameColumnText = props.getProperty(CourseSiteGeneratorProp.NAME_COLUMN_TEXT.toString());
        String emailColumnText = props.getProperty(CourseSiteGeneratorProp.EMAIL_COLUMN_TEXT.toString());
        
        undergradColumn = new TableColumn(undergradColumnText);
        //undergradColumn.setCellValueFactory(new PropertyValueFactory<TeachingAssistant, Boolean>("undergrad"));
        undergradColumn.setCellValueFactory(cellData -> cellData.getValue().getUndergrad());
        undergradColumn.setCellFactory(column -> new CheckBoxTableCell()); 
        taTable.getColumns().add(undergradColumn);
        
        nameColumn = new TableColumn(nameColumnText);
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<TeachingAssistant, String>("name")
        );
        taTable.getColumns().add(nameColumn);
        
        //***************************************************
        //email column
        emailColumn = new TableColumn(emailColumnText);
        emailColumn.setCellValueFactory(
                new PropertyValueFactory<TeachingAssistant, String>("email")
        );
        taTable.getColumns().add(emailColumn);
        
        

        // ADD BOX FOR ADDING A TA
        String namePromptText = props.getProperty(CourseSiteGeneratorProp.NAME_PROMPT_TEXT.toString());
        //*****************
        String emailPromptText = props.getProperty(CourseSiteGeneratorProp.EMAIL_PROMPT_TEXT.toString());
        
        String addButtonText = props.getProperty(CourseSiteGeneratorProp.ADD_BUTTON_TEXT.toString());
        String updateButtonText = props.getProperty(CourseSiteGeneratorProp.UPDATE_BUTTON_TEXT.toString());
        updateButton = new Button(updateButtonText);
        String clearButtonText = props.getProperty(CourseSiteGeneratorProp.CLEAR_BUTTON_TEXT.toString());
        clearButton = new Button(clearButtonText);
        
        //ADD BOX SET UP
        addBox = new HBox();
        
        undergradCheckBox = new HBox(1);
        undergradCheckBox.setAlignment(Pos.CENTER);
        String undergradText = props.getProperty(CourseSiteGeneratorProp.UNDERGRAD_CHECKBOX.toString());
        Label undergradLabel = new Label(undergradText);
        undergrad = new CheckBox();
        undergrad.setSelected(true);
        undergradCheckBox.getChildren().addAll(undergradLabel, undergrad);
        nameTextField = new TextField();
        nameTextField.setPromptText(namePromptText);
        //***********************************
        // ADD EMAIL TEXT FIELD
        emailTextField = new TextField();
        emailTextField.setPromptText(emailPromptText);
        
        addButton = new Button(addButtonText);
        
        undergradCheckBox.prefWidthProperty().bind(addBox.widthProperty().multiply(.09));
        nameTextField.prefWidthProperty().bind(addBox.widthProperty().multiply(.33));
        //***************************************
        // SET EMAIL TEXT FIELD WITH
        emailTextField.prefWidthProperty().bind(addBox.widthProperty().multiply(.35));
        
        addButton.prefWidthProperty().bind(addBox.widthProperty().multiply(.13));
        updateButton.prefWidthProperty().bind(addBox.widthProperty().multiply(.13));
        clearButton.prefWidthProperty().bind(addBox.widthProperty().multiply(.1));
        addButton.prefHeightProperty().bind(addBox.heightProperty());
        updateButton.prefHeightProperty().bind(addBox.heightProperty());
        clearButton.prefHeightProperty().bind(addBox.heightProperty());
        
        addBox.getChildren().add(undergradCheckBox);
        addBox.getChildren().add(nameTextField);
        //**************************
        addBox.getChildren().add(emailTextField);
        
        addBox.getChildren().add(addButton);
        addBox.getChildren().add(clearButton);

        // INIT THE HEADER ON THE RIGHT
        officeHoursHeaderBox = new HBox(20);
        String officeHoursGridText = props.getProperty(CourseSiteGeneratorProp.OFFICE_HOURS_SUBHEADER.toString());
        officeHoursHeaderLabel = new Label(officeHoursGridText);
        officeHoursHeaderBox.getChildren().add(officeHoursHeaderLabel);
        // ADD SPACE
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        officeHoursHeaderBox.getChildren().add(spacer);
        // ADD COMBO BOXES
        officeHoursComboBox = new HBox();
        officeHoursComboBox.setSpacing(13);
        String startTimeText = props.getProperty(CourseSiteGeneratorProp.OFFICE_HOURS_START_TIME);
        String endTimeText = props.getProperty(CourseSiteGeneratorProp.OFFICE_HOURS_END_TIME);
        startComboBoxLabel = new Label(startTimeText);
        startComboBoxLabel.setPadding(new Insets(3, 0, 0, 0));
        endComboBoxLabel = new Label(endTimeText);
        endComboBoxLabel.setPadding(new Insets(3, 0, 0, 0));
        startTimeComboBox = new ComboBox();
        startTimeComboBox.getItems().addAll(
                "12 AM", "1 AM", "2 AM", "3 AM", "4 AM", "5 AM", "6 AM", "7 AM", "8 AM",
                "9 AM", "10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM",
                "7 PM", "8 PM", "9 PM", "10 PM", "11 PM"
        );
        endTimeComboBox = new ComboBox();
        endTimeComboBox.getItems().addAll(
                "12 AM", "1 AM", "2 AM", "3 AM", "4 AM", "5 AM", "6 AM", "7 AM", "8 AM",
                "9 AM", "10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM",
                "7 PM", "8 PM", "9 PM", "10 PM", "11 PM"
        );
        
        String changeHoursText = props.getProperty(CourseSiteGeneratorProp.CHANGE_HOURS_BUTTON_TEXT.toString());
        changeHours = new Button(changeHoursText);
        
        officeHoursComboBox.getChildren().addAll(startComboBoxLabel, startTimeComboBox, endComboBoxLabel, endTimeComboBox, changeHours);
        officeHoursHeaderBox.getChildren().add(officeHoursComboBox);
        
        
        
        
        
        // THESE WILL STORE PANES AND LABELS FOR OUR OFFICE HOURS GRID
        officeHoursGridPane = new GridPane();
        officeHoursGridTimeHeaderPanes = new HashMap();
        officeHoursGridTimeHeaderLabels = new HashMap();
        officeHoursGridDayHeaderPanes = new HashMap();
        officeHoursGridDayHeaderLabels = new HashMap();
        officeHoursGridTimeCellPanes = new HashMap();
        officeHoursGridTimeCellLabels = new HashMap();
        officeHoursGridTACellPanes = new HashMap();
        officeHoursGridTACellLabels = new HashMap();

        // ORGANIZE THE LEFT AND RIGHT PANES
        VBox leftPane = new VBox();
        leftPane.getChildren().add(tasHeaderBox);        
        leftPane.getChildren().add(taTable);        
        leftPane.getChildren().add(addBox);
        VBox rightPane = new VBox();
        rightPane.getChildren().add(officeHoursHeaderBox);
        rightPane.getChildren().add(officeHoursGridPane);
        
        // BOTH PANES WILL NOW GO IN A SPLIT PANE
        SplitPane sPane = new SplitPane(leftPane, new ScrollPane(rightPane));
        sPane.setDividerPosition(0, 0.39);
        taWS = new BorderPane();
        
        // AND PUT EVERYTHING IN THE WORKSPACE
        ((BorderPane) taWS).setCenter(sPane);

        // MAKE SURE THE TABLE EXTENDS DOWN FAR ENOUGH
        taTable.prefHeightProperty().bind(taWS.heightProperty().multiply(1.9));
        
        
        // NOW LET'S SETUP THE EVENT HANDLING
        controller = new TAController(app);
        
        //##### combobox event
        
        changeHours.setOnAction(e -> {
            
            controller.handleChangeTime();
        });
        

        // CONTROLS FOR ADDING TAs
        nameTextField.setOnAction(e -> {
            if ( !isUpdateMode )
                controller.handleAddTA();
            else
                controller.handleUpdate(tempTA, officeHoursGridTACellPanes);
        });
        //###################### set emailTextFienl on action
        emailTextField.setOnAction(e -> {
            if ( !isUpdateMode )
                controller.handleAddTA();
            else
                controller.handleUpdate(tempTA, officeHoursGridTACellPanes);
        });
        
        addButton.setOnAction(e -> {
            controller.handleAddTA();
        });
        
        //###### update button
        updateButton.setOnAction(e -> {
            controller.handleUpdate(tempTA, officeHoursGridTACellPanes);
        });
        //###### clear button
        clearButton.setOnAction(e -> {
            controller.handleClear();
            taTable.getSelectionModel().clearSelection();
            if ( isUpdateMode ){
                addBox.getChildren().remove(updateButton);
                addBox.getChildren().remove(clearButton);
                addBox.getChildren().add(addButton);
                addBox.getChildren().add(clearButton);
                isUpdateMode = false;
            }
        });
        
        //##### change add button to Update button
        taTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if ( newSelection != null ){
                Workspace workspaceProp = (Workspace)app.getWorkspaceComponent();
                TAWorkspace workspace = workspaceProp.getTAWorkspace();
                
                TableView taTable = workspace.getTATable();
                
                tempTA = (TeachingAssistant)taTable.getSelectionModel().getSelectedItem();
                undergrad.setSelected(tempTA.getUndergrad().get());
                nameTextField.setText(tempTA.getName());
                emailTextField.setText(tempTA.getEmail());
                if ( !isUpdateMode ){
                    addBox.getChildren().remove(addButton);
                    addBox.getChildren().remove(clearButton);
                    addBox.getChildren().add(updateButton);
                    addBox.getChildren().add(clearButton);
                    isUpdateMode = true;
                }
            }
        });
        
        
        //########## add function on pressing delete key
        removeButton.setOnAction(e -> {
            controller.handleDeleteTA(officeHoursGridTACellPanes);
        });
        
        taWS.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.DELETE)) {
                controller.handleDeleteTA(officeHoursGridTACellPanes);
            }
            else if(e.isControlDown())
                if(e.getCode() == KeyCode.Z)
                    controller.undo();
                else if(e.getCode() == KeyCode.Y)
                    controller.redo();
        });
        
        
        //########################## add filecontrl and update save butt
        // whene ta is added or removed
        data.getTeachingAssistants().addListener(new ListChangeListener<TeachingAssistant>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends TeachingAssistant> c) {
                AppFileController appController = new AppFileController(app);
                app.getGUI().markEdited(app.getGUI());
            }
        });
        
    }
    
    
    
    // WE'LL PROVIDE AN ACCESSOR METHOD FOR EACH VISIBLE COMPONENT
    // IN CASE A CONTROLLER OR STYLE CLASS NEEDS TO CHANGE IT
    
    
    public HBox getTAsHeaderBox() {
        return tasHeaderBox;
    }

    public Label getTAsHeaderLabel() {
        return tasHeaderLabel;
    }

    public TableView getTATable() {
        return taTable;
    }

    public HBox getAddBox() {
        return addBox;
    }
    public HBox getUndergradHBox(){
        return undergradCheckBox;
    }
    public TextField getNameTextField() {
        return nameTextField;
    }
    //##############################
    public CheckBox getUndergradCheckBox(){
        return undergrad;
    }
    
    public TextField getEmailTextField() {
        return emailTextField;
    }
    //##### set temp TA after update
    public void setTempTA(boolean undergrad, String name, String email){
        tempTA = new TeachingAssistant(undergrad, name, email);
    }

    public Button getAddButton() {
        return addButton;
    }
    public Button getUpdateButton() {
        return updateButton;
    }
    public Button getClearButton() {
        return clearButton;
    }

    public HBox getOfficeHoursSubheaderBox() {
        return officeHoursHeaderBox;
    }

    public Label getOfficeHoursSubheaderLabel() {
        return officeHoursHeaderLabel;
    }
    // office hour start and end time labels
    public HBox getOfficeHoursComboBox() {
        return officeHoursComboBox;
    }
    
    public Label getOfficeHoursStartTimeLabel() {
        return startComboBoxLabel;
    }
    public ComboBox getStartTimeComboBox() {
        return startTimeComboBox;
    }
    public Label getOfficeHoursEndTimeLabel() {
        return endComboBoxLabel;
    }
    public ComboBox getEndTimeComboBox() {
        return endTimeComboBox;
    }

    public GridPane getOfficeHoursGridPane() {
        return officeHoursGridPane;
    }

    public HashMap<String, Pane> getOfficeHoursGridTimeHeaderPanes() {
        return officeHoursGridTimeHeaderPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTimeHeaderLabels() {
        return officeHoursGridTimeHeaderLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridDayHeaderPanes() {
        return officeHoursGridDayHeaderPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridDayHeaderLabels() {
        return officeHoursGridDayHeaderLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridTimeCellPanes() {
        return officeHoursGridTimeCellPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTimeCellLabels() {
        return officeHoursGridTimeCellLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridTACellPanes() {
        return officeHoursGridTACellPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTACellLabels() {
        return officeHoursGridTACellLabels;
    }
    
    
    
    public String getCellKey(Pane testPane) {
        for (String key : officeHoursGridTACellLabels.keySet()) {
            if (officeHoursGridTACellPanes.get(key) == testPane) {
                return key;
            }
        }
        return null;
    }

    public Label getTACellLabel(String cellKey) {
        return officeHoursGridTACellLabels.get(cellKey);
    }

    public Pane getTACellPane(String cellPane) {
        return officeHoursGridTACellPanes.get(cellPane);
    }

    public String buildCellKey(int col, int row) {
        return "" + col + "_" + row;
    }

    public String buildCellText(int militaryHour, String minutes) {
        // FIRST THE START AND END CELLS
        int hour = militaryHour;
        if (hour > 12) {
            hour -= 12;
        }
        String cellText = "" + hour + ":" + minutes;
        if (militaryHour < 12) {
            cellText += "am";
        } else {
            cellText += "pm";
        }
        return cellText;
    }
    
    //#### set combobox methods
    public int toMilitaryHour(String comboBoxHour){
        String[] hourComponents = comboBoxHour.split(" ");
        if (hourComponents[1].equals("AM")){
            if (hourComponents[0].equals("12"))
                return 0;
            else
                return Integer.parseInt(hourComponents[0]);
        }
        else {
            if (hourComponents[0].equals("12"))
                return 12;
            else
                return Integer.parseInt(hourComponents[0]) + 12;
        }
    }
    public String toComboBoxObject(int militaryHour){
        String comboBoxObject;
        if ( militaryHour < 12 ){
                if (militaryHour == 0)
                    comboBoxObject = "12 AM";
                else
                    comboBoxObject = String.valueOf(militaryHour) + " AM";
        }
        else {
            if ( militaryHour == 12 )
                comboBoxObject = "12 PM";
            else
                comboBoxObject = String.valueOf(militaryHour - 12) + " PM";
        }
        return comboBoxObject;
    }
    public int compareComboBoxObjects(String ob1, String ob2){
        String[] temp1 = ob1.split(" ");
        String[] temp2 = ob2.split(" ");
        int tempOb1 = Integer.parseInt(temp1[0])==12 ? 0:Integer.parseInt(temp1[0]);
        int tempOb2 = Integer.parseInt(temp2[0])==12 ? 0:Integer.parseInt(temp2[0]);
        
        if (temp1[1].compareTo(temp2[1]) < 0){
            return -(tempOb1 - tempOb2 + 12);
        }
        else if(temp1[1].compareTo(temp2[1]) > 0){
            return (tempOb2 - tempOb1 + 12);
        }
        else {
            return tempOb1-tempOb2;
        }
    }
    public boolean beforeStartHour(String selectedHour, String taOfficeHour){
        String[] sh = selectedHour.split(" ");
        String[] toh = new String[2];
        if (taOfficeHour.length() == 6){
            toh[0] = taOfficeHour.substring(0, 1);
            toh[1] = taOfficeHour.substring(4);
        } else{
            toh[0] = taOfficeHour.substring(0, 2);
            toh[1] = taOfficeHour.substring(5);
        }
        
        if (toh[1].compareToIgnoreCase(sh[1]) < 0){
            return true;
        }
        else if(toh[1].compareToIgnoreCase(sh[1]) > 0){
            return false;
        }
        else {
            int shInt = Integer.parseInt(sh[0]) == 12 ? 0 : Integer.parseInt(sh[0]);
            int tohInt = Integer.parseInt(toh[0]) == 12 ? 0 : Integer.parseInt(toh[0]);
            if (tohInt < shInt)
                return true;
            else
                return false;
        }
    }
    
    public boolean afterEndHour(String selectedHour, String taOfficeHour){
        String[] sh = selectedHour.split(" ");
        String[] toh = new String[2];
        boolean thirtyMin = false;
        if (taOfficeHour.length() == 6){
            toh[0] = taOfficeHour.substring(0, 1);
            toh[1] = taOfficeHour.substring(4);
            thirtyMin = taOfficeHour.substring(2,4).equals("30");
        } else{
            toh[0] = taOfficeHour.substring(0, 2);
            toh[1] = taOfficeHour.substring(5);
            thirtyMin = taOfficeHour.substring(3,5).equals("30");
        }
        
        if (toh[1].compareToIgnoreCase(sh[1]) < 0){
            return false;
        }
        else if(toh[1].compareToIgnoreCase(sh[1]) > 0){
            return true;
        }
        else {
            int shInt = Integer.parseInt(sh[0]) == 12 ? 0 : Integer.parseInt(sh[0]);
            int tohInt = Integer.parseInt(toh[0]) == 12 ? 0 : Integer.parseInt(toh[0]);
            if (tohInt >= shInt)
                return true;
            else
                return false;
        }
    }
    
    
    public void resetWorkspace() {
        // CLEAR OUT THE GRID PANE
        officeHoursGridPane.getChildren().clear();
        
        // AND THEN ALL THE GRID PANES AND LABELS
        officeHoursGridTimeHeaderPanes.clear();
        officeHoursGridTimeHeaderLabels.clear();
        officeHoursGridDayHeaderPanes.clear();
        officeHoursGridDayHeaderLabels.clear();
        officeHoursGridTimeCellPanes.clear();
        officeHoursGridTimeCellLabels.clear();
        officeHoursGridTACellPanes.clear();
        officeHoursGridTACellLabels.clear();
        nameTextField.clear();
        emailTextField.clear();
        undergrad.setSelected(true);
    }
    
    
    public void reloadWorkspace(TAData dataComponent) {
        //TAData taData = (TAData)dataComponent;
        reloadOfficeHoursGrid(dataComponent);
    }

    public void reloadOfficeHoursGrid(TAData dataComponent) {        
        ArrayList<String> gridHeaders = dataComponent.getGridHeaders();

        // ADD THE TIME HEADERS
        for (int i = 0; i < 2; i++) {
            addCellToGrid(dataComponent, officeHoursGridTimeHeaderPanes, officeHoursGridTimeHeaderLabels, i, 0);
            dataComponent.getCellTextProperty(i, 0).set(gridHeaders.get(i));
        }
        
        // THEN THE DAY OF WEEK HEADERS
        for (int i = 2; i < 7; i++) {
            addCellToGrid(dataComponent, officeHoursGridDayHeaderPanes, officeHoursGridDayHeaderLabels, i, 0);
            dataComponent.getCellTextProperty(i, 0).set(gridHeaders.get(i));            
        }
        
        // SET COMBOBOX ###########
        int[] comboHours = new int[2];
        comboHours[0] = dataComponent.getStartHour();
        comboHours[1] = dataComponent.getEndHour();
        //### update temp start and end hour
        tempStartHour = comboHours[0];
        tempEndHour = comboHours[1];
        
        String[] comboHoursString = new String[2];
        for ( int i = 0; i < 2 ; i++ ){
            if ( comboHours[i] < 12 ){
                if (comboHours[i] == 0)
                    comboHoursString[i] = "12 AM";
                else
                    comboHoursString[i] = String.valueOf(comboHours[i]) + " AM";
            }
            else {
                if ( comboHours[i] == 12 )
                    comboHoursString[i] = "12 PM";
                else
                    comboHoursString[i] = String.valueOf(comboHours[i] - 12) + " PM";
            }
        }
        startTimeComboBox.getSelectionModel().select(comboHoursString[0]);
        endTimeComboBox.getSelectionModel().select(comboHoursString[1]);
        
        
        // THEN THE TIME AND TA CELLS
        int row = 1;
        for (int i = dataComponent.getStartHour(); i < dataComponent.getEndHour(); i++) {
            // START TIME COLUMN
            int col = 0;
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row);
            dataComponent.getCellTextProperty(col, row).set(buildCellText(i, "00"));
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row+1);
            dataComponent.getCellTextProperty(col, row+1).set(buildCellText(i, "30"));

            // END TIME COLUMN
            col++;
            int endHour = i;
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row);
            dataComponent.getCellTextProperty(col, row).set(buildCellText(endHour, "30"));
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row+1);
            dataComponent.getCellTextProperty(col, row+1).set(buildCellText(endHour+1, "00"));
            col++;

            // AND NOW ALL THE TA TOGGLE CELLS
            while (col < 7) {
                addCellToGrid(dataComponent, officeHoursGridTACellPanes, officeHoursGridTACellLabels, col, row);
                addCellToGrid(dataComponent, officeHoursGridTACellPanes, officeHoursGridTACellLabels, col, row+1);
                col++;
            }
            row += 2;
        }

        // CONTROLS FOR TOGGLING TA OFFICE HOURS
        
        //#################
        //AppFileController appController = new AppFileController(app);
        //##################
        CSGStyle csgStyle = (CSGStyle)app.getStyleComponent();
        TAStyle taStyle = csgStyle.getTAStyle();
        
        for (Pane p : officeHoursGridTACellPanes.values()) {
            p.setOnMouseClicked(e -> {
                controller.handleCellToggle((Pane) e.getSource());
                //########################## add filecontrl and update save butt
                app.getGUI().markEdited(app.getGUI());
              
            });
            
            //######### highlignting
            p.setOnMouseEntered(e -> {
                
                taStyle.highlightCellUnderMouse((Pane) e.getSource());
            });
            
            p.setOnMouseExited(e -> {
                taStyle.initOfficeHoursGridStyleAsMouseMoved((Pane) e.getSource());
                
            });
            
        }
        
        // AND MAKE SURE ALL THE COMPONENTS HAVE THE PROPER STYLE
        
        taStyle.initOfficeHoursGridStyle();
    }
    
    public void addCellToGrid(TAData dataComponent, HashMap<String, Pane> panes, HashMap<String, Label> labels, int col, int row) {       
        // MAKE THE LABEL IN A PANE
        Label cellLabel = new Label("");
        HBox cellPane = new HBox();
        cellPane.setAlignment(Pos.CENTER);
        cellPane.getChildren().add(cellLabel);

        // BUILD A KEY TO EASILY UNIQUELY IDENTIFY THE CELL
        String cellKey = dataComponent.getCellKey(col, row);
        cellPane.setId(cellKey);
        cellLabel.setId(cellKey);
        
        // NOW PUT THE CELL IN THE WORKSPACE GRID
        officeHoursGridPane.add(cellPane, col, row);
        
        // AND ALSO KEEP IN IN CASE WE NEED TO STYLIZE IT
        panes.put(cellKey, cellPane);
        labels.put(cellKey, cellLabel);
        
        // AND FINALLY, GIVE THE TEXT PROPERTY TO THE DATA MANAGER
        // SO IT CAN MANAGE ALL CHANGES
        dataComponent.setCellProperty(col, row, cellLabel.textProperty());        
    }
    
    public BorderPane getTDWS(){
        return taWS;
    }
}
