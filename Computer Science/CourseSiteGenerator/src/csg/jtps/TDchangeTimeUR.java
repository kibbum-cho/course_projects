/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.jtps;

import csg.CourseSiteGeneratorApp;
import csg.data.TAData;
import csg.file.TimeSlot;
import csg.workspace.TAWorkspace;
import java.util.ArrayList;
import javafx.scene.control.ComboBox;
import jtps.jTPS_Transaction;

/**
 *
 * @author KI BBUM
 */
public class TDchangeTimeUR implements jTPS_Transaction  {
    CourseSiteGeneratorApp app;
    TAWorkspace workspace;
    TAData data;
    private ArrayList<TimeSlot> officeHours;
    ComboBox startCB;
    ComboBox endCB;
    
    int origStartHour;
    int origEndHour;
    int newStartHour;
    int newEndHour;
    
    
    public TDchangeTimeUR(CourseSiteGeneratorApp initApp, TAWorkspace initWorkspace, TAData initData){
        app = initApp;
        workspace = initWorkspace;
        data = initData;
        officeHours = TimeSlot.buildOfficeHoursList(data);
        startCB = workspace.getStartTimeComboBox();
        endCB = workspace.getEndTimeComboBox();
        origStartHour = data.getStartHour();
        origEndHour = data.getEndHour();
        newStartHour = workspace.toMilitaryHour((String)startCB.getSelectionModel().getSelectedItem());
        newEndHour = workspace.toMilitaryHour((String)endCB.getSelectionModel().getSelectedItem());
        
    }

    @Override
    public void doTransaction() {
        data.setStartHour(newStartHour);
        data.setEndHour(newEndHour);
        workspace.resetWorkspace();
        workspace.reloadWorkspace(data);
        data.chageHour(newStartHour, newEndHour, officeHours);
        startCB.getSelectionModel().select(workspace.toComboBoxObject(newStartHour));
        endCB.getSelectionModel().select(workspace.toComboBoxObject(newEndHour));
        app.getGUI().markEdited(app.getGUI());
        
        //((TAData)app.getDataComponent()).changeTime(newStartTime, newEndTime, officeHours);
    }

    @Override
    public void undoTransaction() {
        data.setStartHour(origStartHour);
        data.setEndHour(origEndHour);
        workspace.resetWorkspace();
        workspace.reloadWorkspace(data);
        data.chageHour(origStartHour, origEndHour, officeHours);
        startCB.getSelectionModel().select(workspace.toComboBoxObject(origStartHour));
        endCB.getSelectionModel().select(workspace.toComboBoxObject(origEndHour));
        app.getGUI().markEdited(app.getGUI());
    }
    
    
}
