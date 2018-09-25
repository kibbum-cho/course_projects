/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import csg.CourseSiteGeneratorApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

/**
 *
 * @author KI BBUM
 */
public class ProjectData {
    CourseSiteGeneratorApp app;
    
    ObservableList<Team> teams;
    
    TextField nameTF;
    ColorPicker colorPicker;
    ColorPicker textColorPicker;
    TextField linkTF;
    
    ObservableList<Student> students;
    
    TextField firstNameTF;
    TextField lastNameTF;
    ComboBox teamCB;
    TextField roleTF;
    
    public ProjectData(CourseSiteGeneratorApp initApp){
        app = initApp;
        
        teams = FXCollections.observableArrayList();
        nameTF= new TextField();
        colorPicker = new ColorPicker();
        textColorPicker = new ColorPicker();
        linkTF = new TextField();
        
        students = FXCollections.observableArrayList();
        firstNameTF = new TextField();
        lastNameTF = new TextField();
        teamCB = new ComboBox();
        teamCB.setItems(teams);
        StringConverter<Team> converter = new StringConverter<Team>() {
            @Override
            public String toString(Team t) {
                return t.getName();
            }
            @Override
            public Team fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        teamCB.setConverter(converter);
        roleTF = new TextField();
    }
    
    public ObservableList getTeams(){
        return teams;
    }
    public Team getTeamOf(String name){
        for (Team t : teams){
            if(t.getName().equals(name))
                return t;
        }
        return null;
    }
    
    public TextField getNameTF(){
        return nameTF;
    }
    public ColorPicker getColorPicker(){
        return colorPicker;
    }
    public ColorPicker getTextColorPicker(){
        return textColorPicker;
    }
    public TextField getLinkTF(){
        return linkTF;
    }
    public ObservableList getStudents(){
        return students;
    }
    
    
    public TextField getFirstNameTF(){
        return firstNameTF;
    }
    public TextField getLastNameTF(){
        return lastNameTF;
    }
    public ComboBox getTeamCB(){
        return teamCB;
    }
    public TextField getRoleTF(){
        return roleTF;
    }
    
    public void addTeam(Team team){
        teams.add(team);
    }
    public void removeTeam(Team team){
        teams.remove(team);
    }
    public void addStudent(Student st){
        students.add(st);
    }
    public void removeStudent(Student st){
        students.remove(st);
    }
    
    
}
