/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.jtps;

import csg.CourseSiteGeneratorApp;
import csg.data.CSGData;
import csg.data.ProjectData;
import csg.data.Team;
import csg.workspace.ProjectWS;
import csg.workspace.Workspace;
import javafx.scene.control.TableView;
import jtps.jTPS_Transaction;

/**
 *
 * @author KI BBUM
 */
public class PDdeleteTeamTransac implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    ProjectData prData;
    String name;
    String color;
    String textColor;
    String link;
    Team team;
    public PDdeleteTeamTransac(CourseSiteGeneratorApp initApp){
        app =  initApp;
        Workspace ws = (Workspace)app.getWorkspaceComponent();
        ProjectWS projectWS= (ProjectWS)ws.getProjectWS();
        CSGData dataCompo = (CSGData)app.getDataComponent();
        prData = (ProjectData)dataCompo.getProjectData();
        
        TableView teamTable = projectWS.getTeamTable();
        Object selectedItem = teamTable.getSelectionModel().getSelectedItem();
        team = (Team)selectedItem;
        /*
        name = projectWS.getNameTF().getText();
        color = "#" + Integer.toHexString(projectWS.getColorCP().getValue().hashCode()); 
        textColor = "#" + Integer.toHexString(projectWS.getTextColorCP().getValue().hashCode()); 
        link = projectWS.getLinkTF().getText();
        team = new Team(name, color, textColor, link); */
        
    }

    @Override
    public void doTransaction() {
        prData.removeTeam(team);
    }

    @Override
    public void undoTransaction() {
        prData.addTeam(team);
    }
    
}
