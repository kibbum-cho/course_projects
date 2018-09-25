/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.style;

import csg.CourseSiteGeneratorApp;
import csg.workspace.TAWorkspace;
import csg.workspace.Workspace;
import djf.components.AppStyleComponent;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author KI BBUM
 */
public class CSGStyle extends AppStyleComponent{
    CourseSiteGeneratorApp app;
    TAStyle taStyle;

    ///////////
    public static String TAB_PANE = "tab_pane";
    public static String TAB = "tab";
    public static String HEADER_LABEL = "header_label";
    public static String SUB_HEADER_LABEL = "sub_header_label";
    public static String LABEL = "label";
    public static String COMBOBOX = "combobox";
    public static String TEXTFIELD = "textfield";
    public static String BUTTON = "button";
    public static String SUB_PANE = "sub_pane";
    public static String TABLE = "table";
    public static String TABLE_COLUMN_HEADER = "table_column";
    
    public static String CLASS_TA_TABLE_COLUMN_HEADER = "ta_table_column_header";
    
    
    //////////
    
    public CSGStyle(CourseSiteGeneratorApp initApp){
        // KEEP THIS FOR LATER
        app = initApp;

        // LET'S USE THE DEFAULT STYLESHEET SETUP
        super.initStylesheet(app);

        // INIT THE STYLE FOR THE FILE TOOLBAR
        app.getGUI().initFileToolbarStyle();

        // AND NOW OUR WORKSPACE STYLE
        initWorkspaceStyle();
        
    }
    
    private void initWorkspaceStyle() {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        taStyle = new TAStyle(app);
        
        workspace.getTabPane().getStyleClass().add(TAB_PANE);
        setStyleClassOnTabs(workspace.getTabs(), TAB);
        setStyleClassOnAll(workspace.getHeaders(), HEADER_LABEL);
        setStyleClassOnAll(workspace.getSubHeaders(), SUB_HEADER_LABEL);
        setStyleClassOnAll(workspace.getLabels(), LABEL);
        setStyleClassOnAll(workspace.getComboBoxes(), COMBOBOX);
        setStyleClassOnAll(workspace.getTextFields(), TEXTFIELD);
        setStyleClassOnAll(workspace.getButtons(), BUTTON);
        setStyleClassOnAll(workspace.getsubPanes(), SUB_PANE);
        setAllTableStyle(workspace.getTable(), TABLE, CLASS_TA_TABLE_COLUMN_HEADER);
        
        
    }
    
    
    private void setStyleClassOnAll(ArrayList nodes, String styleClass) {
        for (Object nodeObject : nodes) {
            Node n = (Node)nodeObject;
            n.getStyleClass().add(styleClass);
        }
    }
    
    private void setStyleClassOnTabs(ArrayList tabs, String styleClass) {
        for (Object nodeObject : tabs) {
            Tab t = (Tab)nodeObject;
            t.getStyleClass().add(styleClass);
        }
    }
    /*
    private void setAllMargin(ArrayList nodes, int scale) {
        for (Object nodeObject : nodes) {
            Node n = (Node)nodeObject;
            n.getStyleClass().add(styleClass);
        }
    }
    */
    
    private void setAllTableStyle(ArrayList tables, String tableStyle, String columnStyle) {
        for (Object table : tables) {
            TableView tv = (TableView)table;
            tv.getStyleClass().add(tableStyle);
            ObservableList tvol = tv.getColumns();
            for (Object tableColumn : tvol) {
                TableColumn tc = (TableColumn)tableColumn;
                tc.getStyleClass().add(columnStyle);
            }
        }
    }
    
    
    
    public TAStyle getTAStyle(){
        return taStyle;
    }
    
}
