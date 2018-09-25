/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import csg.CourseSiteGeneratorApp;
import csg.data.CSGData;
import csg.data.RecitationData;
import csg.jtps.RDaddTransac;
import csg.jtps.RDdeleteTransac;
import csg.jtps.RDupdateTransac;
import jtps.jTPS;
import jtps.jTPS_Transaction;

/**
 *
 * @author KI BBUM
 */
public class RWSController {
    CourseSiteGeneratorApp app;
    jTPS jtps;
    
    public RWSController(CourseSiteGeneratorApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
        jtps = new jTPS();
    }
    
    public void addRecitation(){
        jTPS_Transaction addTAtrans = new RDaddTransac(app);
        jtps.addTransaction(addTAtrans);
    }
    
    public void deleteRecitation(){
        Workspace workspaceComp = (Workspace)app.getWorkspaceComponent();
        RecitationWS recitationWS = workspaceComp.getRecitationWS();
        if(recitationWS.getTable().getSelectionModel().getSelectedItem() != null){
            jTPS_Transaction deleteTAtrans = new RDdeleteTransac(app);
            jtps.addTransaction(deleteTAtrans);
        }
    }
    
    public void updateRecitation(){
        jTPS_Transaction updateTAtrans = new RDupdateTransac(app);
        jtps.addTransaction(updateTAtrans);
    }
    
    public void undo(){
        jtps.undoTransaction();
    }
    public void redo(){
        jtps.doTransaction();
    }
}
