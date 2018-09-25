/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author KI BBUM
 */
public class Recitation<E extends Comparable<E>> implements Comparable<E> {
    private final StringProperty section;
    private final StringProperty instructor;
    private final StringProperty dayTime;
    private final StringProperty location;
    private final StringProperty ta1;
    private final StringProperty ta2;
    
    public Recitation(String initSec, String initInstr, String initDayTime,
            String initLoca, String initTA1, String initTA2){
        section = new SimpleStringProperty(initSec);
        instructor = new SimpleStringProperty(initInstr);
        dayTime = new SimpleStringProperty(initDayTime);
        location = new SimpleStringProperty(initLoca);
        ta1 = new SimpleStringProperty(initTA1);
        ta2 = new SimpleStringProperty(initTA2);
    }
    
    public void setSection(String initSec){
        section.set(initSec);
    }
    public void setInstructor(String initInstr){
        section.set(initInstr);
    }
    public void setDay(String initDay){
        section.set(initDay);
    }
    public void setTime(String initTime){
        section.set(initTime);
    }
    public void setLocation(String initLoca){
        section.set(initLoca);
    }
    public void setTA1(String initTA1){
        section.set(initTA1);
    }
    public void setTA2(String initTA2){
        section.set(initTA2);
    }
    
    public String getSection(){
        return section.get();
    }
    public String getInstructor(){
        return instructor.get();
    }
    public String getDayTime(){
        return dayTime.get();
    }
    public String getLocation(){
        return location.get();
    }
    public String getTa1(){
        return ta1.get();
    }
    public String getTa2(){
        return ta2.get();
    }

    @Override
    public int compareTo(E otherRC) {
        return getSection().compareTo(((Recitation)otherRC).getSection());
    }
    
}
