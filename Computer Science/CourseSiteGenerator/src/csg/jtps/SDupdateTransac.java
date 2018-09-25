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
public class SDupdateTransac implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    ScheduleWS scheduleWS;
    ScheduleData schData;
    String type;
    String date;
    String time;
    String title;
    String topic;
    String link;
    String criteria;
    
    String newType;
    String newDate;
    String newTime;
    String newTitle;
    String newTopic;
    String newLink;
    String newCriteria;
    
    Schedule origSchedule;
    Schedule newSchedule;
    
    public SDupdateTransac(CourseSiteGeneratorApp initApp){
        app =  initApp;
        Workspace ws = (Workspace)app.getWorkspaceComponent();
        scheduleWS= (ScheduleWS)ws.getScheduleWS();
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
        origSchedule = sch;
        
        newType = (String)schData.getTypeCB().getSelectionModel().getSelectedItem();
        newDate = schData.getDateFromDP();
        newTime = schData.getTimeTF().getText();
        newTitle = schData.getTitleTF().getText();
        newTopic = schData.getTopicTF().getText();
        newLink = schData.getLinkTF().getText();
        newCriteria = schData.getCriteriaTF().getText();
        newSchedule = new Schedule(newType, newDate, newTime, newTitle, newTopic, newLink, newCriteria);
    }
    
    
    @Override
    public void doTransaction() {
        schData.removeSchedule(origSchedule);
        schData.addSchedule(newSchedule);
        TableView rcTable = scheduleWS.getTable();
        rcTable.getSelectionModel().select(newSchedule);
    }

    @Override
    public void undoTransaction() {
        schData.removeSchedule(newSchedule);
        schData.addSchedule(origSchedule);
        TableView rcTable = scheduleWS.getTable();
        rcTable.getSelectionModel().select(origSchedule);
    }
    
}
