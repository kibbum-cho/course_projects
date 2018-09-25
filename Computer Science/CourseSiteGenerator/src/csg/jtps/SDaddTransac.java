/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.jtps;

import csg.CourseSiteGeneratorApp;
import csg.data.CSGData;
import csg.data.Schedule;
import csg.data.ScheduleData;
import csg.workspace.ScheduleWS;
import csg.workspace.Workspace;
import jtps.jTPS_Transaction;

/**
 *
 * @author KI BBUM
 */
public class SDaddTransac implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    ScheduleData schData;
    String type;
    String date;
    String time;
    String title;
    String topic;
    String link;
    String criteria;
    Schedule schedule;
    
    public SDaddTransac(CourseSiteGeneratorApp initApp){
        app =  initApp;
        Workspace ws = (Workspace)app.getWorkspaceComponent();
        ScheduleWS recitationWS= (ScheduleWS)ws.getScheduleWS();
        CSGData dataCompo = (CSGData)app.getDataComponent();
        schData = (ScheduleData)dataCompo.getScheduleData();
        
        type = (String)schData.getTypeCB().getSelectionModel().getSelectedItem();
        date = schData.getDateFromDP();
        time = schData.getTimeTF().getText();
        title = schData.getTitleTF().getText();
        topic = schData.getTopicTF().getText();
        link = schData.getLinkTF().getText();
        criteria = schData.getCriteriaTF().getText();
        schedule = new Schedule(type, date, time, title, topic, link, criteria);
    }

    @Override
    public void doTransaction() {
        schData.addSchedule(schedule);
    }

    @Override
    public void undoTransaction() {
        schData.removeSchedule(schedule);
    }
    
}
