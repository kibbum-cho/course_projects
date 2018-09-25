/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.jtps;

import csg.CourseSiteGeneratorApp;
import csg.data.CSGData;
import csg.data.TAData;
import csg.workspace.TAWorkspace;
import csg.workspace.Workspace;
import jtps.jTPS_Transaction;

/**
 *
 * @author KI BBUM
 */
public class TDaddUR implements jTPS_Transaction{
    CourseSiteGeneratorApp app;
    boolean undergrad;
    String taName;
    String taEmail;
    TAData taData;
    
    public TDaddUR(CourseSiteGeneratorApp initApp){
        app =  initApp;
        Workspace ws = (Workspace)app.getWorkspaceComponent();
        TAWorkspace taWS= (TAWorkspace)ws.getTAWorkspace();
        CSGData dataCompo = (CSGData)app.getDataComponent();
        taData = (TAData)dataCompo.getTAData();
        undergrad = taWS.getUndergradCheckBox().isSelected();
        taName = taWS.getNameTextField().getText();
        taEmail = taWS.getEmailTextField().getText();
    }
    
    @Override
    public void doTransaction() {
        taData.addTA(undergrad, taName, taEmail);
    }

    @Override
    public void undoTransaction() {
        taData.removeTA(undergrad, taName, taEmail);
    }
    
}
