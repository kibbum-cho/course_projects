/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.jtps;

import csg.CourseSiteGeneratorApp;
import csg.data.ProjectData;
import csg.data.Student;
import jtps.jTPS_Transaction;
import csg.data.CSGData;
import csg.workspace.ProjectWS;
import csg.workspace.Workspace;
import csg.data.Team;

/**
 *
 * @author KI BBUM
 */
public class PDaddStudentTransac implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    ProjectData prData;
    String firstName;
    String lastName;
    String team;
    String role;
    Student student;
    
    public PDaddStudentTransac(CourseSiteGeneratorApp initApp){
        app =  initApp;
        Workspace ws = (Workspace)app.getWorkspaceComponent();
        ProjectWS projectWS= (ProjectWS)ws.getProjectWS();
        CSGData dataCompo = (CSGData)app.getDataComponent();
        prData = (ProjectData)dataCompo.getProjectData();
        
        firstName = projectWS.getFirstNameTF().getText();
        lastName = projectWS.getLastNameTF().getText();
        team = ((Team)projectWS.getTeamCB().getSelectionModel().getSelectedItem()).getName();
        role = projectWS.getRoleTF().getText();
        student = new Student(firstName, lastName, team, role);
    }
    
    @Override
    public void doTransaction() {
        prData.addStudent(student);
    }

    @Override
    public void undoTransaction() {
        prData.removeStudent(student);
    }
    
}
