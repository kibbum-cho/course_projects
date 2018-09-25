/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import csg.CourseSiteGeneratorApp;
import csg.jtps.PDaddStudentTransac;
import csg.jtps.PDaddTeamTransac;
import csg.jtps.PDdeleteStudentTransac;
import csg.jtps.PDdeleteTeamTransac;
import csg.jtps.PDupdateStudentTransac;
import csg.jtps.PDupdateTeamTransac;
import jtps.jTPS;
import jtps.jTPS_Transaction;

/**
 *
 * @author KI BBUM
 */
public class PWSController {
    CourseSiteGeneratorApp app;
    jTPS jtps;
    
    public PWSController(CourseSiteGeneratorApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
        jtps = new jTPS();
    }
    
    public void addTeam(){
        jTPS_Transaction addTeamTrans = new PDaddTeamTransac(app);
        jtps.addTransaction(addTeamTrans);
    }
    
    public void deleteTeam(){
        Workspace workspaceComp = (Workspace)app.getWorkspaceComponent();
        ProjectWS projecWS = workspaceComp.getProjectWS();
        if(projecWS.getTeamTable().getSelectionModel().getSelectedItem() != null){
            jTPS_Transaction deleteTeamTrans = new PDdeleteTeamTransac(app);
            jtps.addTransaction(deleteTeamTrans);
        }
    }
    
    public void updateTeam(){
        jTPS_Transaction updateTeamTrans = new PDupdateTeamTransac(app);
        jtps.addTransaction(updateTeamTrans);
    }
    
    
    public void addStudent(){
        jTPS_Transaction addStudentTrans = new PDaddStudentTransac(app);
        jtps.addTransaction(addStudentTrans);
    }
    
    public void deleteStudent(){
        Workspace workspaceComp = (Workspace)app.getWorkspaceComponent();
        ProjectWS projecWS = workspaceComp.getProjectWS();
        if(projecWS.getStudentTable().getSelectionModel().getSelectedItem() != null){
            jTPS_Transaction deleteStudentTrans = new PDdeleteStudentTransac(app);
            jtps.addTransaction(deleteStudentTrans);
        }
    }
    
    public void updateStudent(){
        jTPS_Transaction updateStudentTrans = new PDupdateStudentTransac(app);
        jtps.addTransaction(updateStudentTrans);
    }
    
    
    public void undo(){
        jtps.undoTransaction();
    }
    public void redo(){
        jtps.doTransaction();
    }
}
