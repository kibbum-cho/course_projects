/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.jtps;

import csg.CourseSiteGeneratorApp;
import csg.data.CSGData;
import csg.data.ProjectData;
import csg.data.Student;
import csg.data.Team;
import csg.workspace.ProjectWS;
import csg.workspace.Workspace;
import javafx.scene.control.TableView;
import jtps.jTPS_Transaction;

/**
 *
 * @author KI BBUM
 */
public class PDupdateStudentTransac implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    ProjectWS projectWS;
    ProjectData prData;
    Student origStudent;
    String firstName;
    String lastName;
    String team;
    String role;
    Student newStudent;
    
    public PDupdateStudentTransac(CourseSiteGeneratorApp initApp){
        app =  initApp;
        Workspace ws = (Workspace)app.getWorkspaceComponent();
        projectWS = (ProjectWS)ws.getProjectWS();
        CSGData dataCompo = (CSGData)app.getDataComponent();
        prData = (ProjectData)dataCompo.getProjectData();
        
        TableView studentTable = projectWS.getStudentTable();
        Object selectedItem = studentTable.getSelectionModel().getSelectedItem();
        origStudent = (Student)selectedItem;
        
        firstName = projectWS.getFirstNameTF().getText();
        lastName = projectWS.getLastNameTF().getText();
        team = ((Team)projectWS.getTeamCB().getSelectionModel().getSelectedItem()).getName();
        role = projectWS.getRoleTF().getText();
        newStudent = new Student(firstName, lastName, team, role);
    }
    
    @Override
    public void doTransaction() {
        prData.removeStudent(origStudent);
        prData.addStudent(newStudent);
        TableView rcTable = projectWS.getTeamTable();
        rcTable.getSelectionModel().select(newStudent);
    }

    @Override
    public void undoTransaction() {
        prData.removeStudent(newStudent);
        prData.addStudent(origStudent);
        TableView rcTable = projectWS.getTeamTable();
        rcTable.getSelectionModel().select(origStudent);
    }
}
