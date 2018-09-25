/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import static csg.CourseSiteGeneratorProp.*;
import csg.CourseSiteGeneratorApp;
import csg.data.CSGData;
import csg.data.TAData;
import csg.data.TeachingAssistant;
import csg.file.TimeSlot;
import csg.jtps.TDaddUR;
import csg.jtps.TDchangeTimeUR;
import csg.jtps.TDdeleteUR;
import csg.jtps.TDtoggleUR;
import csg.jtps.TDupdateUR;
import djf.ui.AppMessageDialogSingleton;
import djf.ui.AppYesNoCancelDialogSingleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import jtps.jTPS;
import jtps.jTPS_Transaction;
import properties_manager.PropertiesManager;

/**
 *
 * @author KI BBUM
 */
public class TAController {
    // THE APP PROVIDES ACCESS TO OTHER COMPONENTS AS NEEDED
    CourseSiteGeneratorApp app;
    jTPS jtps;

    /**
     * Constructor, note that the app must already be constructed.
     */
    public TAController(CourseSiteGeneratorApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
        jtps = new jTPS();
    }
    
    
    public void handleChangeTime(){
        Workspace workspaceCompo = (Workspace)app.getWorkspaceComponent();
        TAWorkspace workspace = workspaceCompo.getTAWorkspace();
        CSGData dataCompo = (CSGData)app.getDataComponent();
        TAData data = dataCompo.getTAData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        String selectedStartTime = (String)workspace.startTimeComboBox.getSelectionModel().getSelectedItem();
        String startHour = workspace.toComboBoxObject(data.getStartHour());
            
        String selectedEndTime = (String)workspace.endTimeComboBox.getSelectionModel().getSelectedItem();
        String endHour = workspace.toComboBoxObject(data.getEndHour());
        
        AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
        AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
            
        boolean isAfterEndHour = false;
        boolean isBeforeStartHour = false;
        
        // START COMBOBOX HANDLE
        if (workspace.compareComboBoxObjects(selectedStartTime, selectedEndTime) >= 0){
            dialog.show(props.getProperty(COMBOBOX_HOUR_ERROR_TITLE), props.getProperty(COMBOBOX_START_HOUR_ERROR_MESSAGE));
        }
        else {
            ArrayList<TimeSlot> officeHours = TimeSlot.buildOfficeHoursList(data);
            for (TimeSlot ts : officeHours) {	 
                if (workspace.beforeStartHour(selectedStartTime, ts.getTime()))
                    isBeforeStartHour = true;
            }
            for (TimeSlot ts : officeHours) {	 
                if (workspace.afterEndHour(selectedEndTime, ts.getTime()))
                    isAfterEndHour = true;
            }
            if (isBeforeStartHour || isAfterEndHour){
                yesNoDialog.show(props.getProperty(COMBOBOX_SELECTION_CONFLICT_TITLE), props.getProperty(COMBOBOX_SELECTION_CONFLICT_MESSAGE));
                String selection = yesNoDialog.getSelection();
                if (selection.equals(AppYesNoCancelDialogSingleton.YES)){
                    jTPS_Transaction changeHourTrans = new TDchangeTimeUR(app, workspace, data);
                    jtps.addTransaction(changeHourTrans);
                    
                }
                else {
                    workspace.getStartTimeComboBox().getSelectionModel().select(workspace.toComboBoxObject(data.getStartHour()));
                    workspace.getEndTimeComboBox().getSelectionModel().select(workspace.toComboBoxObject(data.getEndHour()));
                }
            }
            else {
                jTPS_Transaction changeHourTrans = new TDchangeTimeUR(app, workspace, data);
                jtps.addTransaction(changeHourTrans);
            }
            
        }
    }
    
    
    /**
     * This method responds to when the user requests to add
     * a new TA via the UI. Note that it must first do some
     * validation to make sure a unique name and email address
     * has been provided.
     */
    public void handleAddTA() {
        // WE'LL NEED THE WORKSPACE TO RETRIEVE THE USER INPUT VALUES
        Workspace workspaceCompo = (Workspace)app.getWorkspaceComponent();
        TAWorkspace workspace = workspaceCompo.getTAWorkspace();
        TextField nameTextField = workspace.getNameTextField();
        String name = nameTextField.getText();
        //######################################
        TextField emailTextField = workspace.getEmailTextField();
        String email = emailTextField.getText();
        
        
        // WE'LL NEED TO ASK THE DATA SOME QUESTIONS TOO
        CSGData dataCompo = (CSGData)app.getDataComponent();
        TAData data = dataCompo.getTAData();
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        //####### check if email is valid
        
        
        // DID THE USER NEGLECT TO PROVIDE A TA NAME?
        if (name.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TA_NAME_TITLE), props.getProperty(MISSING_TA_NAME_MESSAGE));            
        }
        //#################################
        else if (email.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TA_EMAIL_TITLE), props.getProperty(MISSING_TA_EMAIL_MESSAGE));            
        }
        
        // DOES A TA ALREADY HAVE THE SAME NAME OR EMAIL?
        else if (data.containsTA(name)) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE), props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE));                                    
        }
        //###########################################3
        else if (data.containsEmail(email)) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE), props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE));                                    
        }
        //######### check if the email address is valid
        else if (!isValidEmail(email)){
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(TA_EMAIL_NOT_VALID_TITLE), props.getProperty(TA_EMAIL_NOT_VALID_MESSAGE));   
        }
        
        // EVERYTHING IS FINE, ADD A NEW TA
        else {
            // ADD THE NEW TA TO THE DATA
            jTPS_Transaction addTAtrans = new TDaddUR(app);
            jtps.addTransaction(addTAtrans);
            
            // CLEAR THE TEXT FIELDS
            nameTextField.setText("");
            //#####
            emailTextField.setText("");
            
            // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
            nameTextField.requestFocus();
        }
    }
    
    //#### handle edit, responds when a TA selected from the table
    public void handleUpdate(TeachingAssistant selectedTA, HashMap<String, Pane> officeHoursGridTACellPanes) {
        // WE'LL NEED THE WORKSPACE TO RETRIEVE THE USER INPUT VALUES
        Workspace workspaceCompo = (Workspace)app.getWorkspaceComponent();
        TAWorkspace workspace = workspaceCompo.getTAWorkspace();
        TextField nameTextField = workspace.getNameTextField();
        String name = nameTextField.getText();
        TextField emailTextField = workspace.getEmailTextField();
        String email = emailTextField.getText();
        
        boolean selectedUndergrad = selectedTA.getUndergrad().get();
        String selectedName = selectedTA.getName();
        String selectedEmail = selectedTA.getEmail();
        
        // ta table
        TableView taTable = workspace.getTATable();
        
        
        // WE'LL NEED TO ASK THE DATA SOME QUESTIONS TOO
        CSGData dataCompo = (CSGData)app.getDataComponent();
        TAData data = dataCompo.getTAData();
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        if (name.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TA_NAME_TITLE), props.getProperty(MISSING_TA_NAME_MESSAGE));            
        }
        else if (email.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TA_EMAIL_TITLE), props.getProperty(MISSING_TA_EMAIL_MESSAGE));            
        }
        else if (!isValidEmail(email)){
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(TA_EMAIL_NOT_VALID_TITLE), props.getProperty(TA_EMAIL_NOT_VALID_MESSAGE));
            }
        else if ((selectedUndergrad == workspace.getUndergradCheckBox().isSelected()) &&
                                        name.equals(selectedName) && email.equals(selectedEmail)){
            System.out.println("No difference");
        }
        else if ((selectedUndergrad != workspace.getUndergradCheckBox().isSelected()) &&
                                        name.equals(selectedName) && email.equals(selectedEmail)){
            jTPS_Transaction updateTrans = new TDupdateUR(app, workspace, data, selectedTA, true);
            jtps.addTransaction(updateTrans);
        }
        else if (name.equals(selectedName) && !email.equals(selectedEmail)){
            if (data.containsEmail(email)) {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE), props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE));
            }
            else {
                jTPS_Transaction updateTrans = new TDupdateUR(app, workspace, data, selectedTA, true);
                jtps.addTransaction(updateTrans);
            }
        }
        else {
            if (email.equals(selectedEmail)){
                if (data.containsTA(name)) {
                    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                    dialog.show(props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE), props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE));
                } else {
                    jTPS_Transaction updateTrans = new TDupdateUR(app, workspace, data, selectedTA, false);
                    jtps.addTransaction(updateTrans);
                }
            }
            else {
                if (data.containsTA(name)) {
                    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                    dialog.show(props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE), props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE));                                    
                }
                else if (data.containsEmail(email)) {
                    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                    dialog.show(props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE), props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE));                                    
                }
                else {
                    jTPS_Transaction updateTrans = new TDupdateUR(app, workspace, data, selectedTA, false);
                    jtps.addTransaction(updateTrans);
                }
            }
        }
        
    }
    
    //#### handle edit, responds when a TA selected from the table
    public void handleClear() {
        Workspace workspaceCompo = (Workspace)app.getWorkspaceComponent();
        TAWorkspace workspace = workspaceCompo.getTAWorkspace();
        TextField nameTextField = workspace.getNameTextField();
        String name = nameTextField.getText();
        //######################################
        TextField emailTextField = workspace.getEmailTextField();
        String email = emailTextField.getText();
        // CLEAR THE TEXT FIELDS
        nameTextField.setText("");
        //#####
        emailTextField.setText("");
        
        // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
        nameTextField.requestFocus();
    }
    
    //##### email validation helper method
    public boolean isValidEmail(String email) {
           String ePattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
           Pattern p = Pattern.compile(ePattern);
           Matcher m = p.matcher(email);
           return m.matches();
    }
    
    

    /**
     * This function provides a response for when the user clicks
     * on the office hours grid to add or remove a TA to a time slot.
     * 
     * @param pane The pane that was toggled.
     */
    public void handleCellToggle(Pane pane) {
        // GET THE TABLE
        Workspace workspaceCompo = (Workspace)app.getWorkspaceComponent();
        TAWorkspace workspace = workspaceCompo.getTAWorkspace();
        TableView taTable = workspace.getTATable();
        
        // IS A TA SELECTED IN THE TABLE?
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null ){
            // GET THE TA
            TeachingAssistant ta = (TeachingAssistant)selectedItem;
            String taName = ta.getName();
            CSGData dataCompo = (CSGData)app.getDataComponent();
            TAData data = dataCompo.getTAData();
            String cellKey = pane.getId();
        
            // AND TOGGLE THE OFFICE HOURS IN THE CLICKED CELL
            jTPS_Transaction toggleTAtrans = new TDtoggleUR(cellKey, taName, data);
            jtps.addTransaction(toggleTAtrans);
        }
    }
    
        
    
    //################## handle delete
    public void handleDeleteTA(HashMap<String, Pane> officeHoursGridTACellPanes) {
        // GET THE TABLE
        Workspace workspaceCompo = (Workspace)app.getWorkspaceComponent();
        TAWorkspace workspace = workspaceCompo.getTAWorkspace();
        TableView taTable = workspace.getTATable();
        // IS A TA SELECTED IN THE TABLE?
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        // GET THE TA
        if ( selectedItem != null ){
            TeachingAssistant ta = (TeachingAssistant)selectedItem;
            String taName = ta.getName();
            String taEmail = ta.getEmail();
            CSGData dataCompo = (CSGData)app.getDataComponent();
            TAData data = dataCompo.getTAData();
            // delete from ta table
            
            jTPS_Transaction deleteTAtrans = new TDdeleteUR(app, workspace, data);
            jtps.addTransaction(deleteTAtrans);
        }
    }
    
    public void undo(){
        jtps.undoTransaction();
    }
    public void redo(){
        jtps.doTransaction();
    }
}
