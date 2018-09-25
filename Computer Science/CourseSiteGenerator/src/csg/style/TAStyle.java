package csg.style;


import csg.data.CSGData;
import csg.data.TAData;
import csg.data.TeachingAssistant;
import csg.workspace.TAWorkspace;
import csg.workspace.Workspace;
import djf.AppTemplate;
import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;


/**
 * This class manages all CSS style for this application.
 * 
 * @author Richard McKenna
 * @version 1.0
 */
public class TAStyle {
    // FIRST WE SHOULD DECLARE ALL OF THE STYLE TYPES WE PLAN TO USE
    public static String HEADER_LABEL = "header_label";
    public static String SUB_HEADER_LABEL = "sub_header_label";
    public static String LABEL = "label";
    public static String COMBOBOX = "combobox";
    public static String TEXTFIELD = "textfield";
    public static String BUTTON = "button";
    public static String SUB_PANE = "sub_pane";
    public static String TABLE = "table";
    public static String TABLE_COLUMN_HEADER = "table_column";
    
    
    
    // WE'LL USE THIS FOR ORGANIZING LEFT AND RIGHT CONTROLS
    public static String CLASS_PLAIN_PANE = "plain_pane";
    
    // THESE ARE THE HEADERS FOR EACH SIDE
    public static String CLASS_HEADER_PANE = "header_pane";
    public static String CLASS_HEADER_LABEL = "header_label";
    
    

    // ON THE LEFT WE HAVE THE TA ENTRY
    public static String CLASS_TA_TABLE = "ta_table";
    public static String CLASS_TA_TABLE_COLUMN_HEADER = "ta_table_column_header";
    public static String CLASS_ADD_TA_PANE = "add_ta_pane";
    public static String CLASS_ADD_TA_TEXT_FIELD = "add_ta_text_field";
    public static String CLASS_ADD_TA_BUTTON = "add_ta_button";

    // ON THE RIGHT WE HAVE THE OFFICE HOURS GRID
    public static String CLASS_OFFICE_HOURS_COMBOBOX_HBOX = "office_hours_combobox_hbox";
    public static String CLASS_OFFICE_HOURS_START_TIME_LABEL = "office_hours_start_end_time_label";
    public static String CLASS_OFFICE_HOURS_START_COMBOBOX = "office_hours_start_end_combobox";
    
    
    public static String CLASS_OFFICE_HOURS_GRID = "office_hours_grid";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_PANE = "office_hours_grid_time_column_header_pane";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_LABEL = "office_hours_grid_time_column_header_label";
    public static String CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_PANE = "office_hours_grid_day_column_header_pane";
    public static String CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_LABEL = "office_hours_grid_day_column_header_label";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_CELL_PANE = "office_hours_grid_time_cell_pane";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_CELL_LABEL = "office_hours_grid_time_cell_label";
    public static String CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE = "office_hours_grid_ta_cell_pane";
    public static String CLASS_OFFICE_HOURS_GRID_TA_CELL_LABEL = "office_hours_grid_ta_cell_label";
    
    //############ highlight cell and rows and cols
    public static String CLASS_OFFICE_HOURS_GRID_TA_CELL_HIGHLIGHT = "office_hours_grid_ta_cell_highlight";
    public static String CLASS_OFFICE_HOURS_GRID_TA_CELL_HIGHLIGHT_ROW_COL = "office_hours_grid_ta_cell_highlight_row_col";
    
    
    // THIS PROVIDES ACCESS TO OTHER COMPONENTS
    private AppTemplate app;
    
    /**
     * This constructor initializes all style for the application.
     * 
     * @param initApp The application to be stylized.
     */
    public TAStyle(AppTemplate initApp) {
        // KEEP THIS FOR LATER
        app = initApp;

        // LET'S USE THE DEFAULT STYLESHEET SETUP
        //super.initStylesheet(app);

        // INIT THE STYLE FOR THE FILE TOOLBAR
        //app.getGUI().initFileToolbarStyle();

        // AND NOW OUR WORKSPACE STYLE
        initTAWorkspaceStyle();
    }

    /**
     * This function specifies all the style classes for
     * all user interface controls in the workspace.
     */
    private void initTAWorkspaceStyle() {
        // LEFT SIDE - THE HEADER
        Workspace workspaceCompo = (Workspace)app.getWorkspaceComponent();
        TAWorkspace workspaceComponent = workspaceCompo.getTAWorkspace();
        workspaceComponent.getTAsHeaderBox().getStyleClass().add(CLASS_HEADER_PANE);
        workspaceComponent.getTAsHeaderLabel().getStyleClass().add(HEADER_LABEL );

        // LEFT SIDE - THE TABLE
        TableView<TeachingAssistant> taTable = workspaceComponent.getTATable();
        taTable.getStyleClass().add(CLASS_TA_TABLE );
        for (TableColumn tableColumn : taTable.getColumns()) {
            tableColumn.getStyleClass().add(CLASS_TA_TABLE_COLUMN_HEADER );
        }

        // LEFT SIDE - THE TA DATA ENTRY
        workspaceComponent.getAddBox().getStyleClass().add(CLASS_ADD_TA_PANE);
        workspaceComponent.getUndergradHBox().getStyleClass().add(LABEL);
        workspaceComponent.getNameTextField().getStyleClass().add(TEXTFIELD );
        workspaceComponent.getEmailTextField().getStyleClass().add(TEXTFIELD );
        workspaceComponent.getAddButton().getStyleClass().add(BUTTON );
        workspaceComponent.getUpdateButton().getStyleClass().add(BUTTON );
        workspaceComponent.getClearButton().getStyleClass().add(BUTTON );

        // RIGHT SIDE - THE HEADER
        workspaceComponent.getOfficeHoursSubheaderBox().getStyleClass().add(CLASS_HEADER_PANE);
        workspaceComponent.getOfficeHoursSubheaderLabel().getStyleClass().add(HEADER_LABEL );
        // COMBO BOXES AND LABELS
        workspaceComponent.getOfficeHoursComboBox().getStyleClass().add(CLASS_OFFICE_HOURS_COMBOBOX_HBOX);
        workspaceComponent.getOfficeHoursStartTimeLabel().getStyleClass().add(LABEL );
        workspaceComponent.getOfficeHoursEndTimeLabel().getStyleClass().add(LABEL );
        workspaceComponent.getStartTimeComboBox().getStyleClass().add(COMBOBOX );
        workspaceComponent.getEndTimeComboBox().getStyleClass().add(COMBOBOX );
    }
    
    /**
     * This method initializes the style for all UI components in
     * the office hours grid. Note that this should be called every
     * time a new TA Office Hours Grid is created or loaded.
     */
    public void initOfficeHoursGridStyle() {
        // RIGHT SIDE - THE OFFICE HOURS GRID TIME HEADERS
        Workspace workspaceCompo = (Workspace)app.getWorkspaceComponent();
        TAWorkspace workspaceComponent = workspaceCompo.getTAWorkspace();
        workspaceComponent.getOfficeHoursGridPane().getStyleClass().add(CLASS_OFFICE_HOURS_GRID);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeHeaderPanes(), CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeHeaderLabels(), CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_LABEL);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridDayHeaderPanes(), CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridDayHeaderLabels(), CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_LABEL);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeCellPanes(), CLASS_OFFICE_HOURS_GRID_TIME_CELL_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeCellLabels(), CLASS_OFFICE_HOURS_GRID_TIME_CELL_LABEL);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTACellPanes(), CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTACellLabels(), CLASS_OFFICE_HOURS_GRID_TA_CELL_LABEL);
    }
    

    
    /**
     * This helper method initializes the style of all the nodes in the nodes
     * map to a common style, styleClass.
     */
    private void setStyleClassOnAll(HashMap nodes, String styleClass) {
        for (Object nodeObject : nodes.values()) {
            Node n = (Node)nodeObject;
            n.getStyleClass().add(styleClass);
        }
    }
    
    
    
    //######### Highlight
    public void highlightCellUnderMouse(Pane pane){
        Workspace workspaceCompo = (Workspace)app.getWorkspaceComponent();
        TAWorkspace workspaceComponent = workspaceCompo.getTAWorkspace();
        String cellKey = pane.getId();
        String[] key = cellKey.split("_");
        int col = Integer.parseInt(key[0]);
        int row = Integer.parseInt(key[1]);
        //#########
        CSGData dataCompo = (CSGData)app.getDataComponent();
        TAData data = dataCompo.getTAData();
        // THE MOUSED OVER COLUMN HEADER
        Pane headerPane = workspaceComponent.getOfficeHoursGridDayHeaderPanes().get(data.getCellKey(col, 0));
        headerPane.getStyleClass().add(CLASS_OFFICE_HOURS_GRID_TA_CELL_HIGHLIGHT_ROW_COL);

        // THE MOUSED OVER ROW HEADERS
        headerPane = workspaceComponent.getOfficeHoursGridTimeCellPanes().get(data.getCellKey(0, row));
        headerPane.getStyleClass().add(CLASS_OFFICE_HOURS_GRID_TA_CELL_HIGHLIGHT_ROW_COL);
        headerPane = workspaceComponent.getOfficeHoursGridTimeCellPanes().get(data.getCellKey(1, row));
        headerPane.getStyleClass().add(CLASS_OFFICE_HOURS_GRID_TA_CELL_HIGHLIGHT_ROW_COL);
        
        
        for (int r = 1; r <= row; r++){
            for (int c = 2; c <= col; c++){
                if ((c==col && r<row) || (c< col && r==row)){
                    String k = workspaceComponent.buildCellKey(c, r);
                    workspaceComponent.getTACellPane(k).getStyleClass().add(CLASS_OFFICE_HOURS_GRID_TA_CELL_HIGHLIGHT_ROW_COL);
                }
            }
        }
        
        pane.getStyleClass().add(CLASS_OFFICE_HOURS_GRID_TA_CELL_HIGHLIGHT);
        
    }
    
    public void initOfficeHoursGridStyleAsMouseMoved(Pane pane) {
        Workspace workspaceCompo = (Workspace)app.getWorkspaceComponent();
        TAWorkspace workspaceComponent = workspaceCompo.getTAWorkspace();
        String cellKey = pane.getId();
        String[] key = cellKey.split("_");
        int col = Integer.parseInt(key[0]);
        int row = Integer.parseInt(key[1]);
        
        CSGData dataCompo = (CSGData)app.getDataComponent();
        TAData data = dataCompo.getTAData();
        // THE MOUSED OVER COLUMN HEADER
        Pane headerPane = workspaceComponent.getOfficeHoursGridDayHeaderPanes().get(data.getCellKey(col, 0));
        headerPane.getStyleClass().remove(CLASS_OFFICE_HOURS_GRID_TA_CELL_HIGHLIGHT_ROW_COL);

        // THE MOUSED OVER ROW HEADERS
        headerPane = workspaceComponent.getOfficeHoursGridTimeCellPanes().get(data.getCellKey(0, row));
        headerPane.getStyleClass().remove(CLASS_OFFICE_HOURS_GRID_TA_CELL_HIGHLIGHT_ROW_COL);
        headerPane = workspaceComponent.getOfficeHoursGridTimeCellPanes().get(data.getCellKey(1, row));
        headerPane.getStyleClass().remove(CLASS_OFFICE_HOURS_GRID_TA_CELL_HIGHLIGHT_ROW_COL);
        
        for (int r = 1; r <= row; r++){
            for (int c = 2; c <= col; c++){
                if ((c==col && r<row) || (c< col && r==row)){
                    String k = workspaceComponent.buildCellKey(c, r);
                    workspaceComponent.getTACellPane(k).getStyleClass().remove(CLASS_OFFICE_HOURS_GRID_TA_CELL_HIGHLIGHT_ROW_COL);
                }
            }
        }
        
        pane.getStyleClass().remove(CLASS_OFFICE_HOURS_GRID_TA_CELL_HIGHLIGHT);
    }
        
        
}