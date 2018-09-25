/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import csg.CourseSiteGeneratorApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 *
 * @author KI BBUM
 */
public class ScheduleData {
    CourseSiteGeneratorApp app;
    
    DatePicker startingMonDP;
    DatePicker endingFriDP;
    
    ObservableList<Schedule> schedules;
    
    ComboBox typeCB;
    DatePicker datePicker;
    TextField timeTF;
    TextField titleTF;
    TextField topicTF;
    TextField linkTF;
    TextField criteriaTF;
    
    public ScheduleData(CourseSiteGeneratorApp initApp){
        app = initApp;
        
        startingMonDP = new DatePicker();
        endingFriDP = new DatePicker();
        schedules = FXCollections.observableArrayList();
        
        typeCB = new ComboBox();
        ObservableList<String> temp = FXCollections.observableArrayList();
        typeCB.setItems(temp);
        temp.addAll("Holyday", "Lecture", "Reference","Recitation", "Homework");
        
        datePicker= new DatePicker();
        timeTF = new TextField();
        titleTF = new TextField();
        topicTF = new TextField();
        linkTF = new TextField();
        criteriaTF = new TextField();
        
    }
    
    public DatePicker getStartingMonDP(){
        return startingMonDP;
    }
    public String getStartingMonMonth(){
        return String.valueOf(startingMonDP.getValue().getMonthValue());
    }
    public String getStartingMonDay(){
        return String.valueOf(startingMonDP.getValue().getDayOfMonth());
    }
    public String getStartingMonYear(){
        return String.valueOf(startingMonDP.getValue().getYear());
    }
    
    public DatePicker getEndingFriDP(){
        return endingFriDP;
    }
    public String getEndingFriMonth(){
        return String.valueOf(endingFriDP.getValue().getMonthValue());
    }
    public String getEndingFriDay(){
        return String.valueOf(endingFriDP.getValue().getDayOfMonth());
    }
    public String getEndingFriYear(){
        return String.valueOf(endingFriDP.getValue().getYear());
    }
    
    public ObservableList getSchedules(){
        return schedules;
    }
    public void addSchedule(Schedule schedule){
        schedules.add(schedule);
    }
    public void removeSchedule(Schedule schedule){
        schedules.remove(schedule);
    }
    
    public ComboBox getTypeCB(){
        return typeCB;
    }
    public DatePicker getDateDP(){
        return datePicker;
    }
    public String getDateFromDP(){
        return datePicker.getValue().getMonthValue() + "/" + datePicker.getValue().getDayOfMonth()
                    + "/" + datePicker.getValue().getYear();
    }
    public void setDateDP(){
        
    }
    
    public TextField getTimeTF(){
        return timeTF;
    }
    public TextField getTitleTF(){
        return titleTF;
    }
    public TextField getTopicTF(){
        return topicTF;
    }
    public TextField getLinkTF(){
        return linkTF;
    }
    public TextField getCriteriaTF(){
        return criteriaTF;
    }
    
}
