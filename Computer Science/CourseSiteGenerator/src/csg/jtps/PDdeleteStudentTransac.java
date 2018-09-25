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
public class PDdeleteStudentTransac implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    ProjectData prData;
    String firstName;
    String lastName;
    String team;
    String role;
    Student student;
    
    public PDdeleteStudentTransac(CourseSiteGeneratorApp initApp){
        app =  initApp;
        Workspace ws = (Workspace)app.getWorkspaceComponent();
        ProjectWS projectWS= (ProjectWS)ws.getProjectWS();
        CSGData dataCompo = (CSGData)app.getDataComponent();
        prData = (ProjectData)dataCompo.getProjectData();
        
        TableView studentTable = projectWS.getStudentTable();
        Object selectedItem = studentTable.getSelectionModel().getSelectedItem();
        student = (Student)selectedItem;
    }

    @Override
    public void doTransaction() {
        prData.removeStudent(student);
    }

    @Override
    public void undoTransaction() {
        prData.addStudent(student);
    }
}
