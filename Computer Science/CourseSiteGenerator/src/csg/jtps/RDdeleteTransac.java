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
public class RDdeleteTransac implements jTPS_Transaction{
    CourseSiteGeneratorApp app;
    RecitationData rcData;
    String section;
    String instructor;
    String dayTime;
    String location;
    String ta1;
    String ta2;
    
    public RDdeleteTransac(CourseSiteGeneratorApp initApp){
        app =  initApp;
        Workspace ws = (Workspace)app.getWorkspaceComponent();
        RecitationWS recitationWS= (RecitationWS)ws.getRecitationWS();
        CSGData dataCompo = (CSGData)app.getDataComponent();
        rcData = (RecitationData)dataCompo.getRecitationData();
        
        TableView rcTable = recitationWS.getTable();
        Object selectedItem = rcTable.getSelectionModel().getSelectedItem();
        Recitation rc = (Recitation)selectedItem;
        
        section = rc.getSection();
        instructor = rc.getInstructor();
        dayTime = rc.getDayTime();
        location = rc.getLocation();
        ta1 = rc.getTa1();
        ta2 = rc.getTa2();
    }
    
    @Override
    public void doTransaction() {
        rcData.removeRecitation(section, instructor, dayTime, location, ta1, ta2);
    }

    @Override
    public void undoTransaction() {
        rcData.addRecitaion(section, instructor, dayTime, location, ta1, ta2);
    }
    
}
