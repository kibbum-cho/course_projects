/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.jtps;

import csg.CourseSiteGeneratorApp;
import csg.data.CSGData;
import csg.data.RecitationData;
import csg.workspace.RecitationWS;
import csg.workspace.Workspace;
import java.util.ArrayList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import jtps.jTPS_Transaction;

/**
 *
 * @author KI BBUM
 */
public class RDaddTransac implements jTPS_Transaction{
    CourseSiteGeneratorApp app;
    RecitationData rcData;
    String section;
    String instructor;
    String dayTime;
    String location;
    String ta1;
    String ta2;
    
    public RDaddTransac(CourseSiteGeneratorApp initApp){
        app =  initApp;
        Workspace ws = (Workspace)app.getWorkspaceComponent();
        RecitationWS recitationWS= (RecitationWS)ws.getRecitationWS();
        CSGData dataCompo = (CSGData)app.getDataComponent();
        rcData = (RecitationData)dataCompo.getRecitationData();
        ArrayList tfs = recitationWS.getTextFields();
        ArrayList cbs = recitationWS.getComboBoxes();
        section = ((TextField)tfs.get(0)).getText();
        instructor = ((TextField)tfs.get(1)).getText();
        dayTime = ((TextField)tfs.get(2)).getText();
        location = ((TextField)tfs.get(3)).getText();
        
        ta1 = ((ComboBox)cbs.get(0)).getSelectionModel().getSelectedItem().toString();
        ta2 = ((ComboBox)cbs.get(1)).getSelectionModel().getSelectedItem().toString();
    }
    
    
    @Override
    public void doTransaction() {
        rcData.addRecitaion(section, instructor, dayTime, location, ta1, ta2);
    }

    @Override
    public void undoTransaction() {
        rcData.removeRecitation(section, instructor, dayTime, location, ta1, ta2);
    }
    
}
