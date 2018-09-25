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
import javafx.scene.paint.Color;
import jtps.jTPS_Transaction;

/**
 *
 * @author KI BBUM
 */
public class PDaddTeamTransac implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    ProjectData prData;
    String name;
    String color;
    String textColor;
    String link;
    Team team;
    
    public PDaddTeamTransac(CourseSiteGeneratorApp initApp){
        app =  initApp;
        Workspace ws = (Workspace)app.getWorkspaceComponent();
        ProjectWS projectWS= (ProjectWS)ws.getProjectWS();
        CSGData dataCompo = (CSGData)app.getDataComponent();
        prData = (ProjectData)dataCompo.getProjectData();
        
        name = projectWS.getNameTF().getText();
        color = "#" + colorToHex(projectWS.getColorCP().getValue()); 
        textColor = "#" + colorToHex(projectWS.getTextColorCP().getValue()); 
        link = projectWS.getLinkTF().getText();
        team = new Team(name, color, textColor, link);
    }
    
    @Override
    public void doTransaction() {
        prData.addTeam(team);
    }

    @Override
    public void undoTransaction() {
        prData.removeTeam(team);
    }
    
    public String colorToHex(Color color) {
        return colorChanelToHex(color.getRed())
                + colorChanelToHex(color.getGreen())
                + colorChanelToHex(color.getBlue());
    }

    private String colorChanelToHex(double chanelValue) {
        String rtn = Integer.toHexString((int) Math.min(Math.round(chanelValue * 255), 255));
        if (rtn.length() == 1) {
            rtn = "0" + rtn;
        }
        return rtn;
    }
    
}
