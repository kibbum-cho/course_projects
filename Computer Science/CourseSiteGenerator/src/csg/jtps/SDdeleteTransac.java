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
import javafx.scene.control.TableView;
import jtps.jTPS_Transaction;

/**
 *
 * @author KI BBUM
 */
public class SDdeleteTransac implements jTPS_Transaction{
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
    
    public SDdeleteTransac(CourseSiteGeneratorApp initApp){
        app =  initApp;
        Workspace ws = (Workspace)app.getWorkspaceComponent();
        ScheduleWS scheduleWS= (ScheduleWS)ws.getScheduleWS();
        CSGData dataCompo = (CSGData)app.getDataComponent();
        schData = (ScheduleData)dataCompo.getScheduleData();
        
        TableView schTable = scheduleWS.getTable();
        Object selectedItem = schTable.getSelectionModel().getSelectedItem();
        Schedule sch = (Schedule)selectedItem;
        
        type = sch.getType();
        date = sch.getDate();
        time = sch.getTime();
        title = sch.getTitle();
        topic = sch.getTopic();
        link = sch.getLink();
        criteria = sch.getCriteria();
        schedule = sch;
    }
    
    @Override
    public void doTransaction() {
        schData.removeSchedule(schedule);
    }

    @Override
    public void undoTransaction() {
        schData.addSchedule(schedule);
    }
    
    
}
