/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import csg.CourseSiteGeneratorApp;
import csg.CourseSiteGeneratorProp;
import csg.data.CSGData;
import csg.data.ProjectData;
import csg.data.Schedule;
import csg.data.Student;
import csg.data.Team;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
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
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import properties_manager.PropertiesManager;

/**
 *
 * @author KI BBUM
 */
public class ProjectWS {
    CourseSiteGeneratorApp app;
    
    PWSController controller;
    
    ScrollPane projectWS;
    HBox subPane1;
    HBox subPane2;
    VBox projectWSVB;
    
    Label projectsHeader;
    
    VBox teamsVBox;
    
    VBox teamsTableVBox;
    Label teamsSubHeader;
    Button teamRemoveButton;
    TableView<Team> teamTable;
    TableColumn<Team, String>[] teamTableColumns;
    
    GridPane teamAddEditGridPane;
    Label addEditSubHeader;
    Label name;
    TextField nameTF;
    Label color;
    ColorPicker colorPicker;
    Label textColor;
    ColorPicker textColorPicker;
    Label link;
    TextField linkTF;
    Button teamAddUpdateButton;
    Button teamAddButton;
    Button teamUpdateButton;
    Button teamClearButton;
    
    
    VBox studentsVBox;
    
    VBox studentTableVBox;
    Label studentsSubHeader;
    Button studentRemoveButton;
    TableView<Student> studentTable;
    TableColumn<Student, String>[] studentTableColumns;
    
    GridPane studentAddEditGridPane;
    Label studentAESubHeader;
    Label firstName;
    TextField firstNameTF;
    Label lastName;
    TextField lastNameTF;
    Label team;
    ComboBox teamCB;
    Label role;
    TextField roleTF;
    Button studentAddUpdateButton;
    Button studentAddButton;
    Button studentUpdateButton;
    Button studentClearButton;
    
    ArrayList<Node> headers;
    ArrayList<Node> subHeaders;
    ArrayList<Node> labels;
    ArrayList<Node> comboBoxes;
    ArrayList<Node> textFields;
    ArrayList<Node> buttons;
    ArrayList<TableView> tables;
    ArrayList<Node> subPanes;
    
    boolean isTeamUpdateMode;
    boolean isStudentUpdateMode;
    
    
    public ProjectWS(CourseSiteGeneratorApp initApp){
        // KEEP THIS FOR LATER
        app = initApp;
        
        CSGData dataComp = (CSGData) app.getDataComponent();
        ProjectData data = (ProjectData)dataComp.getProjectData();
        
        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        String projectsHeaderText = props.getProperty(CourseSiteGeneratorProp.PT_PROJECTS_HEADER.toString());
        projectsHeader = new Label(projectsHeaderText);
        
        String teamsSubHeaderText = props.getProperty(CourseSiteGeneratorProp.PT_TEAMS_SUBHEADER.toString());
        teamsSubHeader = new Label(teamsSubHeaderText);
        
        String teamRemoveButtonText = props.getProperty(CourseSiteGeneratorProp.REMOVE_BUTTON.toString());
        teamRemoveButton = new Button(teamRemoveButtonText);
        
        //TABLE
        teamTable = new TableView();
        teamTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ObservableList<Team> teams = data.getTeams();
        teamTable.setItems(teams);
        String[] valueFact = {"name","color","textColor","link"};
        ArrayList<String> teamTableHeaders = props.getPropertyOptionsList(CourseSiteGeneratorProp.PT_PROJECT_TABLE_HEADERS);
        teamTableColumns = new TableColumn[teamTableHeaders.size()];
        for ( int i = 0; i < teamTableColumns.length; i++ ){
            teamTableColumns[i] = new TableColumn(teamTableHeaders.get(i));
            teamTableColumns[i].setCellValueFactory(
                new PropertyValueFactory<Team, String>(valueFact[i])
            );
            teamTable.getColumns().add(teamTableColumns[i]);
        }
        teamTable.setPrefHeight(150);
        
        
        String addEditSubHeaderText = props.getProperty(CourseSiteGeneratorProp.PT_ADD_EDIT_SUBHEADER.toString());
        addEditSubHeader = new Label(addEditSubHeaderText);
        
        String nameText = props.getProperty(CourseSiteGeneratorProp.PT_NAME.toString());
        name = new Label(nameText);
        
        nameTF = data.getNameTF();
        nameTF.setPrefWidth(200);
        
        String colorText = props.getProperty(CourseSiteGeneratorProp.PT_COLOR.toString());
        color = new Label(colorText);
        
        colorPicker = data.getColorPicker();
        
        String textColorText = props.getProperty(CourseSiteGeneratorProp.PT_TEXT_COLOR.toString());
        textColor = new Label(textColorText);
        
        textColorPicker = data.getTextColorPicker();
        
        String linkText = props.getProperty(CourseSiteGeneratorProp.PT_LINK.toString());
        link = new Label(linkText);
        
        linkTF = data.getLinkTF();
        
        String teamAddUpdateButtonText = props.getProperty(CourseSiteGeneratorProp.ADD_UPDATE_BUTTON.toString());
        teamAddUpdateButton = new Button(teamAddUpdateButtonText);
        
        String addButtonText = props.getProperty(CourseSiteGeneratorProp.ADD_BUTTON_TEXT.toString());
        String updateButtonText = props.getProperty(CourseSiteGeneratorProp.UPDATE_BUTTON_TEXT.toString());
        teamAddButton = new Button(addButtonText);
        teamUpdateButton = new Button(updateButtonText);
        
        String teamClearButtonText = props.getProperty(CourseSiteGeneratorProp.CLEAR_BUTTON.toString());
        teamClearButton = new Button(teamClearButtonText);
        
        
        String studentsSubHeaderText = props.getProperty(CourseSiteGeneratorProp.PT_STUDENTS_SUBHEADER.toString());
        studentsSubHeader = new Label(studentsSubHeaderText);
        
        String studentRemoveButtonText = props.getProperty(CourseSiteGeneratorProp.REMOVE_BUTTON.toString());
        studentRemoveButton = new Button(studentRemoveButtonText);
        
        //TABLE
        studentTable = new TableView();
        studentTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ObservableList<Student> studnets = data.getStudents();
        studentTable.setItems(studnets);
        String[] valueFact2 = {"firstName","lastName","team","role"};
        ArrayList<String> studentTableHeaders = props.getPropertyOptionsList(CourseSiteGeneratorProp.PT_STUDENTS_TABLE_HEADERS);
        studentTableColumns = new TableColumn[studentTableHeaders.size()];
        for ( int i = 0; i < studentTableColumns.length; i++ ){
            studentTableColumns[i] = new TableColumn(studentTableHeaders.get(i));
            studentTableColumns[i].setCellValueFactory(
                new PropertyValueFactory<Student, String>(valueFact2[i])
            );
            studentTable.getColumns().add(studentTableColumns[i]);
        }
        studentTable.setPrefHeight(200);
        
        
        String studentAESubHeaderText = props.getProperty(CourseSiteGeneratorProp.PT_ADD_EDIT_SUBHEADER.toString());
        studentAESubHeader = new Label(studentAESubHeaderText);
        
        String firstNameText = props.getProperty(CourseSiteGeneratorProp.PT_FIRST_NAME.toString());
        firstName = new Label(firstNameText);
        
        firstNameTF = data.getFirstNameTF();
        firstNameTF.setPrefWidth(250);
        
        String lastNameText = props.getProperty(CourseSiteGeneratorProp.PT_LAST_NAME.toString());
        lastName = new Label(lastNameText);
        
        lastNameTF = data.getLastNameTF();
        
        String teamText = props.getProperty(CourseSiteGeneratorProp.PT_TEAM.toString());
        team = new Label(teamText);
        
        teamCB = data.getTeamCB();
        teamCB.setItems(teams);
        StringConverter<Team> converter = new StringConverter<Team>() {
            @Override
            public String toString(Team t) {
                return t.getName();
            }
            @Override
            public Team fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        teamCB.setConverter(converter);
        
        String roleText = props.getProperty(CourseSiteGeneratorProp.PT_ROLE.toString());
        role = new Label(roleText);
        
        roleTF = data.getRoleTF();
        
        String studentAddUpdateButtonText = props.getProperty(CourseSiteGeneratorProp.ADD_UPDATE_BUTTON.toString());
        studentAddUpdateButton = new Button(studentAddUpdateButtonText);
        
        studentAddButton = new Button(addButtonText);
        studentUpdateButton = new Button(updateButtonText);
        
        String studentClearButtonText = props.getProperty(CourseSiteGeneratorProp.CLEAR_BUTTON.toString());
        studentClearButton = new Button(studentClearButtonText);
        
        // BUILD WORKSPACE
        
        // TEAM PART
        HBox teamHeaderButton = new HBox(10);
        teamHeaderButton.getChildren().addAll(teamsSubHeader, teamRemoveButton);
        teamsTableVBox = new VBox(10);
        teamsTableVBox.getChildren().addAll(teamHeaderButton, teamTable);
        
        teamAddEditGridPane = new GridPane();
        teamAddEditGridPane.setHgap(10);
        teamAddEditGridPane.setVgap(10);
        
        teamAddEditGridPane.add(addEditSubHeader, 0, 0);
        teamAddEditGridPane.add(name, 0, 1);
        teamAddEditGridPane.add(nameTF, 1, 1, 2, 1);
        teamAddEditGridPane.add(color, 0, 2);
        teamAddEditGridPane.add(colorPicker, 1, 2);
        teamAddEditGridPane.add(textColor, 2, 2);
        teamAddEditGridPane.add(textColorPicker, 3, 2);
        teamAddEditGridPane.add(link, 0, 3);
        teamAddEditGridPane.add(linkTF, 1, 3, 3, 1);
        HBox buttonsHBox = new HBox(5);
        buttonsHBox.getChildren().addAll(teamAddButton, teamClearButton);
        teamAddEditGridPane.add(buttonsHBox, 0, 4, 2, 1);
        
        teamsVBox = new VBox(40);
        teamsVBox.getChildren().addAll(teamsTableVBox, teamAddEditGridPane);
        
        
        // STUDENTS
        HBox studentHeaderButton = new HBox(10);
        studentHeaderButton.getChildren().addAll(studentsSubHeader, studentRemoveButton);
        studentTableVBox = new VBox(10);
        studentTableVBox.getChildren().addAll(studentHeaderButton, studentTable);
        
        studentAddEditGridPane = new GridPane();
        studentAddEditGridPane.setHgap(10);
        studentAddEditGridPane.setVgap(10);
        
        studentAddEditGridPane.add(studentAESubHeader, 0, 0);
        studentAddEditGridPane.add(firstName, 0, 1);
        studentAddEditGridPane.add(firstNameTF, 1, 1);
        studentAddEditGridPane.add(lastName, 0, 2);
        studentAddEditGridPane.add(lastNameTF, 1, 2);
        studentAddEditGridPane.add(team, 0, 3);
        studentAddEditGridPane.add(teamCB, 1, 3);
        studentAddEditGridPane.add(role, 0, 4);
        studentAddEditGridPane.add(roleTF, 1, 4);
        HBox studentButtonsHBox = new HBox(5);
        studentButtonsHBox.getChildren().addAll(studentAddButton, studentClearButton);
        studentAddEditGridPane.add(studentButtonsHBox, 0, 5, 2, 1);
        
        studentsVBox = new VBox(40);
        studentsVBox.getChildren().addAll(studentTableVBox, studentAddEditGridPane);
        
        
        projectWSVB = new VBox(12);
        projectWSVB.setPadding(new Insets(10, 0, 15, 0));
        projectWSVB.setMaxWidth(800);
        subPane1 = new HBox(teamsVBox);
        subPane1.setMargin(teamsVBox, new Insets(15, 21, 17, 21));
        subPane2 = new HBox(studentsVBox);
        subPane2.setMargin(studentsVBox, new Insets(15, 21, 17, 21));
        projectWSVB.getChildren().addAll(projectsHeader, subPane1, subPane2);
        
        // TABLE WIDTH
        teamTable.prefWidthProperty().bind(projectWSVB.widthProperty());
        studentTable.prefWidthProperty().bind(projectWSVB.widthProperty());
        
        
        projectWS = new ScrollPane();
        projectWS.setFitToWidth(true);
        
        StackPane p = new StackPane();
        p.prefWidthProperty().bind(projectWS.widthProperty());
        p.setAlignment(Pos.CENTER);
        p.getChildren().add(projectWSVB);
        
        projectWS.setContent(p);
        
        // SET EVENT HANDLING
        controller = new PWSController(app);
        
        teamAddButton.setOnAction(e -> {
            controller.addTeam();
            
            nameTF.setText("");
            colorPicker.setValue(Color.WHITE);
            textColorPicker.setValue(Color.WHITE);
            linkTF.setText("");
            nameTF.requestFocus();
        });
        
        teamRemoveButton.setOnAction(e -> {
            controller.deleteTeam();
        });
        
        teamUpdateButton.setOnAction(e -> {
            controller.updateTeam();
        });
        
        
        //###### clear button
        teamClearButton.setOnAction(e -> {
            nameTF.setText("");
            colorPicker.setValue(Color.WHITE);
            textColorPicker.setValue(Color.WHITE);
            linkTF.setText("");
            nameTF.requestFocus();
            teamTable.getSelectionModel().clearSelection();
            if ( isTeamUpdateMode ){
                buttonsHBox.getChildren().remove(teamUpdateButton);
                buttonsHBox.getChildren().remove(teamClearButton);
                buttonsHBox.getChildren().add(teamAddButton);
                buttonsHBox.getChildren().add(teamClearButton);
                isTeamUpdateMode = false;
            }
        });
        
        //##### change add button to Update button
        teamTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if ( newSelection != null ){
                Workspace workspaceProp = (Workspace)app.getWorkspaceComponent();
                ProjectWS workspace = workspaceProp.getProjectWS();
                
                TableView tTable = workspace.getTeamTable();
                Team tp = (Team)tTable.getSelectionModel().getSelectedItem();
                
                nameTF.setText(tp.getName());
                colorPicker.setValue(Color.web(tp.getColor()));
                textColorPicker.setValue(Color.web(tp.getTextColor()));
                linkTF.setText(tp.getLink());
                
                if ( !isTeamUpdateMode ){
                    buttonsHBox.getChildren().remove(teamAddButton);
                    buttonsHBox.getChildren().remove(teamClearButton);
                    buttonsHBox.getChildren().add(teamUpdateButton);
                    buttonsHBox.getChildren().add(teamClearButton);
                    isTeamUpdateMode = true;
                }
            }
        });
        
        // STUDENT
        studentAddButton.setOnAction(e -> {
            controller.addStudent();
            
            firstNameTF.setText("");
            lastNameTF.setText("");
            teamCB.getSelectionModel().clearSelection();
            roleTF.setText("");
            firstNameTF.requestFocus();
        });
        
        studentRemoveButton.setOnAction(e -> {
            controller.deleteStudent();
        });
        
        studentUpdateButton.setOnAction(e -> {
            controller.updateStudent();
        });
        
        
        //###### clear button
        studentClearButton.setOnAction(e -> {
            firstNameTF.setText("");
            lastNameTF.setText("");
            teamCB.getSelectionModel().clearSelection();
            roleTF.setText("");
            firstNameTF.requestFocus();
            studentTable.getSelectionModel().clearSelection();
            if ( isStudentUpdateMode ){
                studentButtonsHBox.getChildren().remove(studentUpdateButton);
                studentButtonsHBox.getChildren().remove(studentClearButton);
                studentButtonsHBox.getChildren().add(studentAddButton);
                studentButtonsHBox.getChildren().add(studentClearButton);
                isStudentUpdateMode = false;
            }
        });
        
        //##### change add button to Update button
        studentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if ( newSelection != null ){
                Workspace workspaceProp = (Workspace)app.getWorkspaceComponent();
                ProjectWS workspace = workspaceProp.getProjectWS();
                
                TableView tTable = workspace.getStudentTable();
                Student st = (Student)tTable.getSelectionModel().getSelectedItem();
                
                firstNameTF.setText(st.getFirstName());
                lastNameTF.setText(st.getLastName());
                teamCB.getSelectionModel().select(data.getTeamOf(st.getTeam()));
                roleTF.setText(st.getRole());
                
                if ( !isStudentUpdateMode ){
                    studentButtonsHBox.getChildren().remove(studentAddButton);
                    studentButtonsHBox.getChildren().remove(studentClearButton);
                    studentButtonsHBox.getChildren().add(studentUpdateButton);
                    studentButtonsHBox.getChildren().add(studentClearButton);
                    isStudentUpdateMode = true;
                }
            }
        });
        
        
        
        teamsVBox.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.DELETE)) {
                controller.deleteTeam();
            }
        });
        
        studentsVBox.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.DELETE)) {
                controller.deleteStudent();
            }
        });
        
        projectWS.setOnKeyPressed(e -> {
            if(e.isControlDown())
                if(e.getCode() == KeyCode.Z)
                    controller.undo();
                else if(e.getCode() == KeyCode.Y)
                    controller.redo();
        });
        
        
    }
    
    // get methods for style
    public ArrayList<Node> getHeaders(){
        headers = new ArrayList<Node>();
        headers.add(projectsHeader);
        return headers;
    }
    
    public ArrayList<Node> getSubHeaders(){
        subHeaders = new ArrayList<Node>();
        subHeaders.add(teamsSubHeader);
        subHeaders.add(addEditSubHeader);
        subHeaders.add(studentsSubHeader);
        subHeaders.add(studentAESubHeader);
        return subHeaders;
    }
    
    public ArrayList<Node> getLabels(){
        labels = new ArrayList<Node>();
        labels.add(name);
        labels.add(color);
        labels.add(textColor);
        labels.add(link);
        labels.add(firstName);
        labels.add(lastName);
        labels.add(team);
        labels.add(role);
        return labels;
    }
    
    public ArrayList<Node> getComboBoxes(){
        comboBoxes = new ArrayList<Node>();
        comboBoxes.add(teamCB);
        return comboBoxes;
    }
    
    public ArrayList<Node> getTextFields(){
        textFields = new ArrayList<Node>();
        textFields.add(nameTF);
        textFields.add(linkTF);
        textFields.add(firstNameTF);
        textFields.add(lastNameTF);
        textFields.add(roleTF);
        return textFields;
    }
    public TextField getNameTF(){
        return nameTF;
    }
    public ColorPicker getColorCP(){
        return colorPicker;
    }
    public ColorPicker getTextColorCP(){
        return textColorPicker;
    }
    public TextField getLinkTF(){
        return linkTF;
    }
    public TextField getFirstNameTF(){
        return firstNameTF;
    }
    public TextField getLastNameTF(){
        return lastNameTF;
    }
    public ComboBox getTeamCB(){
        return teamCB;
    }
    public TextField getRoleTF(){
        return roleTF;
    }
    
    public TableView getTeamTable(){
        return teamTable;
    }
    public TableView getStudentTable(){
        return studentTable;
    }
    
    
    public ArrayList<Node> getButtons(){
        buttons = new ArrayList<Node>();
        buttons.add(teamRemoveButton);
        buttons.add(teamAddUpdateButton);
        buttons.add(teamClearButton);
        buttons.add(studentRemoveButton);
        buttons.add(studentAddUpdateButton);
        buttons.add(studentClearButton);
        return buttons;
    }
    
    public ArrayList<TableView> getTable(){
        tables = new ArrayList<TableView>();
        tables.add(teamTable);
        tables.add(studentTable);
        return tables;
    }
    
    public ArrayList<Node> getsubPanes(){
        subPanes = new ArrayList<Node>();
        subPanes.add(subPane1);
        subPanes.add(subPane2);
        return subPanes;
    }
    
    
    public ScrollPane getPDWS(){
        return projectWS;
    }
    
    
}
