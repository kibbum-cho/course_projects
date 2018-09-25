/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.file;

import csg.CourseSiteGeneratorApp;
import csg.data.Schedule;
import csg.data.ScheduleData;
import java.math.BigDecimal;
import javafx.collections.ObservableList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author KI BBUM
 */
public class ScheduleFiles {
    CourseSiteGeneratorApp app;
    
    public ScheduleFiles(CourseSiteGeneratorApp initApp){
        app = initApp;
    }
    
    public JsonObject getJson(ScheduleData data){
        // GET THE DATA
	ScheduleData dataManager = (ScheduleData)data;

	// NOW BUILD THE TA JSON OBJCTS TO SAVE
	JsonArrayBuilder holydaysArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder lecturesArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder referencesArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder recitationsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder hwsArrayBuilder = Json.createArrayBuilder();
	ObservableList<Schedule> schedules = dataManager.getSchedules();
	for (Schedule r : schedules) {	    
	    if (r.getType().equals("Holyday"))
                buildJsonArray(holydaysArrayBuilder, r);
            else if(r.getType().equals("Lecture"))
                buildJsonArray(lecturesArrayBuilder, r);
            else if(r.getType().equals("Reference"))
                buildJsonArray(referencesArrayBuilder, r);
            else if(r.getType().equals("Recitation"))
                buildJsonArray(recitationsArrayBuilder, r);
            else if(r.getType().equals("Homework"))
                buildJsonArray(hwsArrayBuilder, r);
	}
	JsonArray holydaysArray = holydaysArrayBuilder.build();
        JsonArray lecturesArray = lecturesArrayBuilder.build();
        JsonArray referencesArray = referencesArrayBuilder.build();
        JsonArray recitationsArray = recitationsArrayBuilder.build();
        JsonArray hwsArray = hwsArrayBuilder.build();

	
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add("startingMondayMonth", dataManager.getStartingMonMonth())
                .add("startingMondayDay", dataManager.getStartingMonDay())
                .add("startingMondayYear", dataManager.getStartingMonYear())
                .add("endingFridayMonth", dataManager.getEndingFriMonth())
                .add("endingFridayDay", dataManager.getEndingFriDay())
                .add("endingFridayYear", dataManager.getEndingFriYear())
                .add("holidays", holydaysArray)
                .add("lectures", lecturesArray)
                .add("references", referencesArray)
                .add("recitations", recitationsArray)
                .add("hws", hwsArray)
                .build();
        
        return dataManagerJSO;
    }
    
    private void buildJsonArray(JsonArrayBuilder jb, Schedule sch){
        String[] date = sch.getDate().split("/");
        JsonObject jo = Json.createObjectBuilder()
                .add("month", date[0])
                .add("day", date[1])
                .add("time", sch.getTime())
                .add("title", sch.getTitle())
                .add("topic", sch.getTopic())
                .add("link", sch.getLink())
                .add("criteria", sch.getCriteria()).build();
        jb.add(jo);
    }
}
