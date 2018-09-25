/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import csg.CourseSiteGeneratorApp;
import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 *
 * @author KI BBUM
 */
public class RecitationData {
    CourseSiteGeneratorApp app;
    
    ObservableList<Recitation> recitations;
    TextField sectionTF;
    TextField instructorTF;
    TextField dayTimeTF;
    TextField locationTF;
    
    ObservableList<TeachingAssistant> tas;
    
    ComboBox supervisingTA1CB;
    ComboBox supervisingTA2CB;
    
    public RecitationData(CourseSiteGeneratorApp initApp){
        app = initApp;
        
        recitations = FXCollections.observableArrayList();
        
        sectionTF = new TextField();
        instructorTF = new TextField();
        dayTimeTF = new TextField();
        locationTF = new TextField();
        
        
        supervisingTA1CB = new ComboBox();
        //supervisingTA1CB.setValue("name");
        
        supervisingTA2CB = new ComboBox();
    }
    
    public void setTAs(ObservableList taList){
        tas = taList;
    }
    public ObservableList getTAs(){
        return tas;
    }
    public TeachingAssistant getTANameoOf(String taName){
        for (TeachingAssistant ta : tas){
            if(ta.getName().equals(taName)){
                return ta;
            }
        }
        return null;
    }
    public ObservableList getRecitations(){
        return recitations;
    }
    public Recitation getRecitationOf(String recitationSection) {
        for (Recitation rc : recitations) {
            if (rc.getSection().equals(recitationSection)) {
                return rc;
            }
        }
        return null;
    }
    
    
    public TextField getSectionsTF(){
        return sectionTF;
    }
    public TextField getInstructorTF(){
        return instructorTF;
    }
    public TextField getDayTimeTF(){
        return dayTimeTF;
    }
    public TextField getLocationTF(){
        return locationTF;
    }
    public ComboBox getTACB1(){
        return supervisingTA1CB;
    }
    public ComboBox getTACB2(){
        return supervisingTA2CB;
    }
    
    // HELPER METHODS
    public void addRecitaion(String initSec, String initInstr, String initDayTime,
                                String initLoca, String initTA1, String initTA2){
        
        Recitation newRC = new Recitation(initSec, initInstr, initDayTime,
                                                initLoca, initTA1, initTA2);
        if (!containsRecitation(initSec))
            recitations.add(newRC);
        Collections.sort(recitations);
    }
    
    //#################### remove TA from the table
    public void removeRecitation(String initSec, String initInstr, String initDayTime,
                                String initLoca, String initTA1, String initTA2){
        // MAKE THE TA
        Recitation reci = new Recitation(initSec, initInstr, initDayTime,
                                                initLoca, initTA1, initTA2);

        // REMOVE THE TA
        for ( Recitation r : recitations ){
            if ( r.compareTo(reci) == 0 ){
                recitations.remove(r);
                break;
            }
        }
        // SORT THE TAS
        Collections.sort(recitations);
    }
    
    public boolean containsRecitation(String section){
        boolean contains = false;
        for(Recitation r : recitations){
            if(r.getSection().equals(section))
                contains = true;
        }
        return contains;
    }
    
}
