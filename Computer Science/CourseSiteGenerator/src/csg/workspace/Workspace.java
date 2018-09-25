/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import csg.CourseSiteGeneratorApp;
import csg.CourseSiteGeneratorProp;
import csg.data.CSGData;
import csg.data.TAData;
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import properties_manager.PropertiesManager;

/**
 *
 * @author KI BBUM
 */
public class Workspace extends AppWorkspaceComponent{
    CourseSiteGeneratorApp app;
    CourseDetailsWS courseDetailsWS;
    TAWorkspace taWorkspace;
    RecitationWS recitationWS;
    ScheduleWS scheduleWS;
    ProjectWS projectWS;
    
    // TABS 
    TabPane tabPane;
    Tab courseDetailsTab;
    Tab taDataTab;
    Tab recitationDataTab;
    Tab scheduleDataTab;
    Tab projectDataTab;
    int tabNumber;
    
    
    public Workspace(CourseSiteGeneratorApp initApp){
        app = initApp;
        
        courseDetailsWS = new CourseDetailsWS(initApp);
        taWorkspace = new TAWorkspace(initApp);
        recitationWS = new RecitationWS(initApp);
        scheduleWS = new ScheduleWS(initApp);
        projectWS = new ProjectWS(initApp);
        
        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        String courseDetailsTabText = props.getProperty(CourseSiteGeneratorProp.COURSE_DETAILS_TAB.toString());
        String taDataTabText = props.getProperty(CourseSiteGeneratorProp.TA_DATA_TAB.toString());
        String recitationDataTabText = props.getProperty(CourseSiteGeneratorProp.RECITATION_DATA_TAB.toString());
        String scheduleDataTabText = props.getProperty(CourseSiteGeneratorProp.SCHEDULE_DATA_TAB.toString());
        String projectDataTabText = props.getProperty(CourseSiteGeneratorProp.PROJECT_DATA_TAB.toString());
        
        
        // SET TAB CONTENT
        courseDetailsTab = new Tab(courseDetailsTabText);
        courseDetailsTab.setContent(courseDetailsWS.getCDWS());
        
        taDataTab = new Tab(taDataTabText);
        taDataTab.setContent(taWorkspace.getTDWS());
        
        recitationDataTab = new Tab(recitationDataTabText);
        recitationDataTab.setContent(recitationWS.getRDWS());
        
        scheduleDataTab = new Tab(scheduleDataTabText);
        scheduleDataTab.setContent(scheduleWS.getSDWS());
        
        projectDataTab = new Tab(projectDataTabText);
        projectDataTab.setContent(projectWS.getPDWS());
        
        
        
        
        tabPane = new TabPane(courseDetailsTab, taDataTab, recitationDataTab, scheduleDataTab, projectDataTab);
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        
        
        // SET THE WORKSPACE
        
        workspace = new BorderPane();
        //Pane backgroundPane = new Pane();
        //backgroundPane.prefHeightProperty().bind(workspace.heightProperty());
        //backgroundPane.prefWidthProperty().bind(workspace.widthProperty());
        //backgroundPane.getChildren().add(tabPane);
        //backgroundPane.setA
        
        // AND PUT EVERYTHING IN THE WORKSPACE
        ((BorderPane) workspace).setCenter(tabPane);
        //((BorderPane) workspace).setAlignment(tabPane, Pos.CENTER);
        
        
    }

    @Override
    public void resetWorkspace() {
        taWorkspace.resetWorkspace();
    }

    @Override
    public void reloadWorkspace(AppDataComponent dataComponent) {
        CSGData dataComp = (CSGData)dataComponent;
        TAData data = dataComp.getTAData();
        
        taWorkspace.reloadWorkspace(data);
    }

    private void CourseDetailsWS(CourseSiteGeneratorApp initApp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public CourseDetailsWS getCourseDetailsWS(){
        return courseDetailsWS;
    }
    public TAWorkspace getTAWorkspace(){
        return taWorkspace;
    }
    public RecitationWS getRecitationWS(){
        return recitationWS;
    }
    public ScheduleWS getScheduleWS(){
        return scheduleWS;
    }
    public ProjectWS getProjectWS(){
        return projectWS;
    }
    
    
    // get methods for style
    public TabPane getTabPane(){
        return tabPane;
    }
    
    public ArrayList<Tab> getTabs(){
        ArrayList<Tab> tabs = new ArrayList<>();
        tabs.add(courseDetailsTab);
        tabs.add(taDataTab);
        tabs.add(recitationDataTab);
        tabs.add(scheduleDataTab);
        tabs.add(projectDataTab);
        return tabs;
    }
    /*
    public int getTabNumber(){
        #################################33
        #################################################3
    }
    */
    public ArrayList<Node> getHeaders(){
        ArrayList<Node> headers = new ArrayList<Node>();
        headers.addAll(courseDetailsWS.getHeaders());
        headers.addAll(recitationWS.getHeaders());
        headers.addAll(scheduleWS.getHeaders());
        headers.addAll(projectWS.getHeaders());
        return headers;
    }
    
    public ArrayList<Node> getSubHeaders(){
        ArrayList<Node> subHeaders = new ArrayList<Node>();
        subHeaders.addAll(courseDetailsWS.getSubHeaders());
        subHeaders.addAll(recitationWS.getSubHeaders());
        subHeaders.addAll(scheduleWS.getSubHeaders());
        subHeaders.addAll(projectWS.getSubHeaders());
        return subHeaders;
    }
    
    public ArrayList<Node> getLabels(){
        ArrayList<Node> labels = new ArrayList<Node>();
        labels.addAll(courseDetailsWS.getLabels());
        labels.addAll(recitationWS.getLabels());
        labels.addAll(scheduleWS.getLabels());
        labels.addAll(projectWS.getLabels());
        return labels;
    }
    
    public ArrayList<Node> getComboBoxes(){
        ArrayList<Node> comboBoxes = new ArrayList<Node>();
        comboBoxes.addAll(courseDetailsWS.getComboBoxes());
        comboBoxes.addAll(recitationWS.getComboBoxes());
        comboBoxes.addAll(scheduleWS.getComboBoxes());
        comboBoxes.addAll(projectWS.getComboBoxes());
        return comboBoxes;
    }
    
    public ArrayList<Node> getTextFields(){
        ArrayList<Node> textFields = new ArrayList<Node>();
        textFields.addAll(courseDetailsWS.getTextFields());
        textFields.addAll(recitationWS.getTextFields());
        textFields.addAll(scheduleWS.getTextFields());
        textFields.addAll(projectWS.getTextFields());
        return textFields;
    }
    
    public ArrayList<Node> getButtons(){
        ArrayList<Node> buttons = new ArrayList<Node>();
        buttons.addAll(courseDetailsWS.getButtons());
        buttons.addAll(recitationWS.getButtons());
        buttons.addAll(scheduleWS.getButtons());
        buttons.addAll(projectWS.getButtons());
        
        return buttons;
    }
    
    public ArrayList<TableView> getTable(){
        ArrayList<TableView> tables = new ArrayList<TableView>();
        tables.add(courseDetailsWS.getTable());
        tables.add(recitationWS.getTable());
        tables.add(scheduleWS.getTable());
        tables.addAll(projectWS.getTable());
        return tables;
    }
    
    public ArrayList<Node> getsubPanes(){
        ArrayList<Node> subPanes = new ArrayList<Node>();
        subPanes.addAll(courseDetailsWS.getsubPanes());
        subPanes.addAll(recitationWS.getsubPanes());
        subPanes.addAll(scheduleWS.getsubPanes());
        subPanes.addAll(projectWS.getsubPanes());
        return subPanes;
    }
    
    // GET METHODS FOR STYLE
    
}
