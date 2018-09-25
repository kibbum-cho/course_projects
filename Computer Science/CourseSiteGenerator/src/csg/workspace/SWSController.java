/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import csg.CourseSiteGeneratorApp;
import csg.jtps.SDaddTransac;
import csg.jtps.SDdeleteTransac;
import csg.jtps.SDupdateTransac;
import jtps.jTPS;
import jtps.jTPS_Transaction;

/**
 *
 * @author KI BBUM
 */
public class SWSController {
    CourseSiteGeneratorApp app;
    jTPS jtps;
    
    public SWSController(CourseSiteGeneratorApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
        jtps = new jTPS();
    }
    
    public void addSchedule(){
        jTPS_Transaction addScheduleTrans = new SDaddTransac(app);
        jtps.addTransaction(addScheduleTrans);
    }
    
    public void deleteSchedule(){
        Workspace workspaceComp = (Workspace)app.getWorkspaceComponent();
        ScheduleWS scheduleWS = workspaceComp.getScheduleWS();
        if(scheduleWS.getTable().getSelectionModel().getSelectedItem() != null){
            jTPS_Transaction deleteScheduleTrans = new SDdeleteTransac(app);
            jtps.addTransaction(deleteScheduleTrans);
        }
    }
    
    public void updateSchedule(){
        jTPS_Transaction updateScheduleTrans = new SDupdateTransac(app);
        jtps.addTransaction(updateScheduleTrans);
    }
    
    public void undo(){
        jtps.undoTransaction();
    }
    public void redo(){
        jtps.doTransaction();
    }
    
}
