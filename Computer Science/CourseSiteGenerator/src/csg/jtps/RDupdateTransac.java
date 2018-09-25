/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.jtps;

import csg.CourseSiteGeneratorApp;
import csg.data.CSGData;
import csg.data.Recitation;
import csg.data.RecitationData;
import csg.workspace.RecitationWS;
import csg.workspace.Workspace;
import java.util.ArrayList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import jtps.jTPS_Transaction;

/**
 *
 * @author KI BBUM
 */
public class RDupdateTransac implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    RecitationData rcData;
    RecitationWS recitationWS;
    String origSection;
    String origInstructor;
    String origDayTime;
    String origLocation;
    String origTa1;
    String origTa2;
    String newSection;
    String newInstructor;
    String newDayTime;
    String newLocation;
    String newTa1;
    String newTa2;
    
    public RDupdateTransac(CourseSiteGeneratorApp initApp){
        app =  initApp;
        Workspace ws = (Workspace)app.getWorkspaceComponent();
        recitationWS = (RecitationWS)ws.getRecitationWS();
        CSGData dataCompo = (CSGData)app.getDataComponent();
        rcData = (RecitationData)dataCompo.getRecitationData();
        
        TableView rcTable = recitationWS.getTable();
        Object selectedItem = rcTable.getSelectionModel().getSelectedItem();
        Recitation rc = (Recitation)selectedItem;
        
        origSection = rc.getSection();
        origInstructor = rc.getInstructor();
        origDayTime = rc.getDayTime();
        origLocation = rc.getLocation();
        origTa1 = rc.getTa1();
        origTa2 = rc.getTa2();
        
        ArrayList tfs = recitationWS.getTextFields();
        ArrayList cbs = recitationWS.getComboBoxes();
        newSection = ((TextField)tfs.get(0)).getText();
        newInstructor = ((TextField)tfs.get(1)).getText();
        newDayTime = ((TextField)tfs.get(2)).getText();
        newLocation = ((TextField)tfs.get(3)).getText();
        newTa1 = ((ComboBox)cbs.get(0)).getSelectionModel().getSelectedItem().toString();
        newTa2 = ((ComboBox)cbs.get(1)).getSelectionModel().getSelectedItem().toString();
    }
    
    
    @Override
    public void doTransaction() {
        rcData.removeRecitation(origSection, origInstructor, origDayTime, origLocation, origTa1, origTa2);
        rcData.addRecitaion(newSection, newInstructor, newDayTime, newLocation, newTa1, newTa2);
        TableView rcTable = recitationWS.getTable();
        rcTable.getSelectionModel().select(rcData.getRecitationOf(newSection));
    }

    @Override
    public void undoTransaction() {
        rcData.removeRecitation(newSection, newInstructor, newDayTime, newLocation, newTa1, newTa2);
        rcData.addRecitaion(origSection, origInstructor, origDayTime, origLocation, origTa1, origTa2);
        TableView rcTable = recitationWS.getTable();
        rcTable.getSelectionModel().select(rcData.getRecitationOf(origSection));
    }
    
}
