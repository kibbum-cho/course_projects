/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.file;

import csg.CourseSiteGeneratorApp;
import csg.data.ProjectData;
import csg.data.Student;
import csg.data.Team;
import javafx.collections.ObservableList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 *
 * @author KI BBUM
 */
public class ProjectFiles {
    CourseSiteGeneratorApp app;
    
    
    public ProjectFiles(CourseSiteGeneratorApp initApp){
        app = initApp;
    }
    
    public JsonObject getJson(ProjectData data){
        // GET THE DATA
	ProjectData dataManager = (ProjectData)data;

	// NOW BUILD THE TEAM JSON OBJCTS TO SAVE
	JsonArrayBuilder teamsArrayBuilder = Json.createArrayBuilder();
	ObservableList<Team> teams = dataManager.getTeams();
	for (Team t : teams) {	    
	    JsonObject tJson = Json.createObjectBuilder()
                    .add("name", t.getName())
                    .add("color", t.getColor())
		    .add("text_color", t.getTextColor())
                    .add("link", t.getLink()).build();
	    teamsArrayBuilder.add(tJson);
	}
	JsonArray teamsArray = teamsArrayBuilder.build();
        
        // NOW BUILD THE STUDENT JSON OBJCTS TO SAVE
	JsonArrayBuilder studentsArrayBuilder = Json.createArrayBuilder();
	ObservableList<Student> students = dataManager.getStudents();
	for (Student s : students) {	    
	    JsonObject tJson = Json.createObjectBuilder()
                    .add("first_name", s.getFirstName())
                    .add("last_name", s.getLastName())
		    .add("team", s.getTeam())
                    .add("role", s.getRole()).build();
	    studentsArrayBuilder.add(tJson);
	}
	JsonArray studentsArray = studentsArrayBuilder.build();

	
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add("teams", teamsArray)
                .add("students", studentsArray)
                .build();
        
        return dataManagerJSO;
    }
}
