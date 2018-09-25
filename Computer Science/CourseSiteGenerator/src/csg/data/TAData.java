/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import csg.CourseSiteGeneratorApp;
import csg.CourseSiteGeneratorProp;
import csg.file.TimeSlot;
import csg.workspace.TAWorkspace;
import csg.workspace.Workspace;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import properties_manager.PropertiesManager;

/**
 *
 * @author KI BBUM
 */
public class TAData {
    // WE'LL NEED ACCESS TO THE APP TO NOTIFY THE GUI WHEN DATA CHANGES
    CourseSiteGeneratorApp app;

    // NOTE THAT THIS DATA STRUCTURE WILL DIRECTLY STORE THE
    // DATA IN THE ROWS OF THE TABLE VIEW
    ObservableList<TeachingAssistant> teachingAssistants;

    // THIS WILL STORE ALL THE OFFICE HOURS GRID DATA, WHICH YOU
    // SHOULD NOTE ARE StringProperty OBJECTS THAT ARE CONNECTED
    // TO UI LABELS, WHICH MEANS IF WE CHANGE VALUES IN THESE
    // PROPERTIES IT CHANGES WHAT APPEARS IN THOSE LABELS
    HashMap<String, StringProperty> officeHours;
    
    // THESE ARE THE LANGUAGE-DEPENDENT VALUES FOR
    // THE OFFICE HOURS GRID HEADERS. NOTE THAT WE
    // LOAD THESE ONCE AND THEN HANG ON TO THEM TO
    // INITIALIZE OUR OFFICE HOURS GRID
    ArrayList<String> gridHeaders;

    // THESE ARE THE TIME BOUNDS FOR THE OFFICE HOURS GRID. NOTE
    // THAT THESE VALUES CAN BE DIFFERENT FOR DIFFERENT FILES, BUT
    // THAT OUR APPLICATION USES THE DEFAULT TIME VALUES AND PROVIDES
    // NO MEANS FOR CHANGING THESE VALUES
    int startHour;
    int endHour;
    
    // DEFAULT VALUES FOR START AND END HOURS IN MILITARY HOURS
    public static final int MIN_START_HOUR = 9;
    public static final int MAX_END_HOUR = 20;

    /**
     * This constructor will setup the required data structures for
     * use, but will have to wait on the office hours grid, since
     * it receives the StringProperty objects from the Workspace.
     * 
     * @param initApp The application this data manager belongs to. 
     */
    public TAData(CourseSiteGeneratorApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;

        // CONSTRUCT THE LIST OF TAs FOR THE TABLE
        teachingAssistants = FXCollections.observableArrayList();

        // THESE ARE THE DEFAULT OFFICE HOURS
        startHour = MIN_START_HOUR;
        endHour = MAX_END_HOUR;
        
        //THIS WILL STORE OUR OFFICE HOURS
        officeHours = new HashMap();
        
        // THESE ARE THE LANGUAGE-DEPENDENT OFFICE HOURS GRID HEADERS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> timeHeaders = props.getPropertyOptionsList(CourseSiteGeneratorProp.OFFICE_HOURS_TABLE_HEADERS);
        ArrayList<String> dowHeaders = props.getPropertyOptionsList(CourseSiteGeneratorProp.DAYS_OF_WEEK);
        gridHeaders = new ArrayList();
        gridHeaders.addAll(timeHeaders);
        gridHeaders.addAll(dowHeaders);
    }
    
    /**
     * Called each time new work is created or loaded, it resets all data
     * and data structures such that they can be used for new values.
     */
    //@Override
    public void resetData() {
        startHour = MIN_START_HOUR;
        endHour = MAX_END_HOUR;
        teachingAssistants.clear();
        officeHours.clear();
    }
    
    // ACCESSOR METHODS

    public int getStartHour() {
        return startHour;
    }
    public void setStartHour(int sH){
        startHour = sH;
    }

    public int getEndHour() {
        return endHour;
    }
    public void setEndHour(int eH){
        endHour = eH;
    }
    
    public ArrayList<String> getGridHeaders() {
        return gridHeaders;
    }

    public ObservableList getTeachingAssistants() {
        return teachingAssistants;
    }
    
    public String getCellKey(int col, int row) {
        return col + "_" + row;
    }

    public StringProperty getCellTextProperty(int col, int row) {
        String cellKey = getCellKey(col, row);
        return officeHours.get(cellKey);
    }

    public HashMap<String, StringProperty> getOfficeHours() {
        return officeHours;
    }
    public void setOfficeHours(HashMap<String, StringProperty> rebuiltOfficeHours){
        officeHours = rebuiltOfficeHours;
    }
    
    public int getNumRows() {
        return ((endHour - startHour) * 2) + 1;
    }

    public String getTimeString(int militaryHour, boolean onHour) {
        String minutesText = "00";
        if (!onHour) {
            minutesText = "30";
        }

        // FIRST THE START AND END CELLS
        int hour = militaryHour;
        if (hour > 12) {
            hour -= 12;
        }
        String cellText = "" + hour + ":" + minutesText;
        if (militaryHour < 12) {
            cellText += "am";
        } else {
            cellText += "pm";
        }
        return cellText;
    }
    
    public String getCellKey(String day, String time) {
        int col = -1;
        if(day.equalsIgnoreCase("monday"))
            col = 2;
        else if (day.equalsIgnoreCase("tuesday"))
            col = 3;
        else if (day.equalsIgnoreCase("wednesday"))
            col = 4;
        else if (day.equalsIgnoreCase("thursday"))
            col = 5;
        else if (day.equalsIgnoreCase("friday"))
            col = 6;
        int row = 1;
        int hour = Integer.parseInt(time.substring(0, time.indexOf("_")));
        int milHour = hour;
        //if (hour < startHour)
        //    milHour += 12;
        if(time.contains("pm"))
            milHour += 12;
        if(time.contains("12"))
            milHour -= 12;
        row += (milHour - startHour) * 2;
        if (time.contains("_30"))
            row += 1;
        return getCellKey(col, row);
    }
    
    public TeachingAssistant getTA(String testName) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (ta.getName().equals(testName)) {
                return ta;
            }
        }
        return null;
    }
    
    /**
     * This method is for giving this data manager the string property
     * for a given cell.
     */
    public void setCellProperty(int col, int row, StringProperty prop) {
        String cellKey = getCellKey(col, row);
        officeHours.put(cellKey, prop);
    }    
    
    /**
     * This method is for setting the string property for a given cell.
     */
    public void setGridProperty(ArrayList<ArrayList<StringProperty>> grid,
                                int column, int row, StringProperty prop) {
        grid.get(row).set(column, prop);
    }
    
    public void chageHour(int initStartHour, int initEndHour, ArrayList<TimeSlot> officeHours){
        //startHour = initStartHour;
        //endHour = initEndHour;
        for(TimeSlot ts : officeHours){
            String temp = ts.getTime();
            int tempint = Integer.parseInt(temp.substring(0, temp.indexOf('_')));
            if(temp.contains("pm"))
                tempint += 12;
            if(temp.contains("12"))
                tempint -= 12;
            if(tempint >= startHour && tempint <= endHour)
                addOfficeHoursReservation(ts.getDay(), ts.getTime(), ts.getName());
        }
        
    }
    
    private void initOfficeHours(int initStartHour, int initEndHour) {
        // NOTE THAT THESE VALUES MUST BE PRE-VERIFIED
        startHour = initStartHour;
        endHour = initEndHour;
        
        // EMPTY THE CURRENT OFFICE HOURS VALUES
        officeHours.clear();
            
        // WE'LL BUILD THE USER INTERFACE COMPONENT FOR THE
        // OFFICE HOURS GRID AND FEED THEM TO OUR DATA
        // STRUCTURE AS WE GO
        Workspace workspaceComponent = (Workspace)app.getWorkspaceComponent();
        TAWorkspace taWorkspace = (TAWorkspace)workspaceComponent.getTAWorkspace();
        taWorkspace.reloadOfficeHoursGrid(this);
    }
    // 
    public void initHours(String startHourText, String endHourText) {
        int initStartHour = Integer.parseInt(startHourText);
        int initEndHour = Integer.parseInt(endHourText);
        if ((initStartHour >= 0)
                && (initEndHour < 24)
                && (initStartHour <= initEndHour)) {
            // THESE ARE VALID HOURS SO KEEP THEM
            initOfficeHours(initStartHour, initEndHour);
        }
    }

    public boolean containsTA(String testName) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (ta.getName().equals(testName)) {
                return true;
            }
        }
        return false;
    }
    // ###### check email
    public boolean containsEmail(String testEmail) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (ta.getEmail().equals(testEmail)) {
                return true;
            }
        }
        return false;
    }
    
    //########### add initEmail argument
    public void addTA(boolean initUndergrad, String initName, String initEmail) {
        // MAKE THE TA
        TeachingAssistant ta = new TeachingAssistant(initUndergrad, initName, initEmail);

        // ADD THE TA
        if (!containsTA(initName)) {
            teachingAssistants.add(ta);
        }

        // SORT THE TAS
        Collections.sort(teachingAssistants);
    }
    
    //#################### remove TA from the table
    public void removeTA(boolean initUndergrad, String initName, String initEmail) {
        // MAKE THE TA
        TeachingAssistant ta = new TeachingAssistant(initUndergrad, initName, initEmail);

        // REMOVE THE TA
        for ( TeachingAssistant tas : teachingAssistants ){
            if ( tas.compareTo(ta) == 0 ){
                teachingAssistants.remove(tas);
                break;
            }
        }

        // SORT THE TAS
        Collections.sort(teachingAssistants);
    }
    
    

    public void addOfficeHoursReservation(String day, String time, String taName) {
        String cellKey = getCellKey(day, time);
        toggleTAOfficeHours(cellKey, taName);
    }
    
    /**
     * This function toggles the taName in the cell represented
     * by cellKey. Toggle means if it's there it removes it, if
     * it's not there it adds it.
     */
    //############ remove ta name if it's already there
    public void toggleTAOfficeHours(String cellKey, String taName) {
        StringProperty cellProp = officeHours.get(cellKey);
        String cellText = cellProp.getValue();
        String[] names = cellText.split("\n");
        boolean found = false;
        
        boolean empty = cellText.equals("");
        if (!empty){
            for ( int i = 0; i < names.length; i++ ){
                if (names[i].equals(taName)){
                    found = true;
                    
                }
            }
        }
        
        if (found){
            removeTAFromCell(cellProp, taName);
        } else{
            if (cellText.equals("")){
                cellProp.setValue(taName);
            } else
                cellProp.setValue(cellText + "\n" + taName);
        }
    }
    
    //############### remove all selected ta's office hours
    public void removeSelectedTAFromCell(String taName, HashMap<String, Pane> officeHoursGridTACellPanes) {
        
        for (Pane p : officeHoursGridTACellPanes.values()){
            String cellKey = p.getId();
            StringProperty cellProp = officeHours.get(cellKey);
            String cellText = cellProp.getValue();
            String[] names = cellText.split("\n");
            boolean found = false;
        
            boolean empty = cellText.equals("");
            if (!empty){
                for ( int i = 0; i < names.length; i++ ){
                    if (names[i].equals(taName)){
                        found = true;
                    }
                }
            }
            if (found){
                removeTAFromCell(cellProp, taName);
            }
        }
    }
    
    
    /**
     * This method removes taName from the office grid cell
     * represented by cellProp.
     */
    public void removeTAFromCell(StringProperty cellProp, String taName) {
        // GET THE CELL TEXT
        String cellText = cellProp.getValue();
        String[] names = cellText.split("\n");
        boolean found = false;
        int index = -1;
        boolean empty = cellText.equals("");
        if (!empty){
            for ( int i = 0; i < names.length; i++ ){
                if (names[i].equals(taName)){
                    found = true;
                    index = i;
                }
            }
        }
        // IS IT THE ONLY TA IN THE CELL?
        if (cellText.equals(taName)) {
            cellProp.setValue("");
        }
        // IS IT THE FIRST TA IN A CELL WITH MULTIPLE TA'S?
        else if (index == 0) {
            int startIndex = cellText.indexOf("\n") + 1;
            cellText = cellText.substring(startIndex);
            cellProp.setValue(cellText);
        }
        // IT MUST BE ANOTHER TA IN THE CELL
        else {
            String temp = "";
            for ( int j = 0; j < names.length; j++){
                if ( j != index ){
                    if (j==0)
                        temp += names[j];
                    else
                        temp += ("\n"+names[j]);
                }
            }
            cellProp.setValue(temp);
        }
    }
    
    //############### update all selected ta's office hours
    public void updateTAFromCell(String cellKey, String selectedName, String newName) {
        StringProperty cellProp = officeHours.get(cellKey);
        String cellText = cellProp.getValue();
        String[] names = cellText.split("\n");
        boolean found = false;
        
        boolean empty = cellText.equals("");
        if (!empty){
            for ( int i = 0; i < names.length; i++ ){
                if (names[i].equals(selectedName)){
                    found = true;
                }
            }
        }
        
        if (found){
            updateTAFromCellHelper(cellProp, selectedName, newName);
        }
    }
    
    public void updateTAFromCellHelper(StringProperty cellProp, String selectedName, String newName) {
        // GET THE CELL TEXT
        String cellText = cellProp.getValue();
        String[] names = cellText.split("\n");
        boolean found = false;
        int index = -1;
        boolean empty = cellText.equals("");
        if (!empty){
            for ( int i = 0; i < names.length; i++ ){
                if (names[i].equals(selectedName)){
                    found = true;
                    index = i;
                }
            }
        }
        // IS IT THE ONLY TA IN THE CELL?
        if (cellText.equals(selectedName)) {
            cellProp.setValue(newName);
        }
        // IS IT THE FIRST TA IN A CELL WITH MULTIPLE TA'S?
        else if (index == 0) {
            int startIndex = cellText.indexOf("\n") + 1;
            cellText = cellText.substring(startIndex);
            cellProp.setValue(newName+ "\n"+cellText);
        }
        // IT MUST BE ANOTHER TA IN THE CELL
        else {
            String temp = "";
            for ( int j = 0; j < names.length; j++){
                if ( j == index )
                    temp += "\n" + newName;
                if ( j != index ){
                    if (j==0)
                        temp += names[j];
                    else
                        temp += ("\n"+names[j]);
                }
            }
            cellProp.setValue(temp);
        }
    }
 
}
