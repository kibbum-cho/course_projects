/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.file;

import csg.CourseSiteGeneratorApp;
import csg.data.Recitation;
import csg.data.RecitationData;
import javafx.collections.ObservableList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 *
 * @author KI BBUM
 */
public class RecitationFiles {
    CourseSiteGeneratorApp app;
    
    
    public RecitationFiles(CourseSiteGeneratorApp initApp){
        app = initApp;
    }
    
    public JsonObject getJson(RecitationData data){
        // GET THE DATA
	RecitationData dataManager = (RecitationData)data;

	// NOW BUILD THE TA JSON OBJCTS TO SAVE
	JsonArrayBuilder rcArrayBuilder = Json.createArrayBuilder();
	ObservableList<Recitation> recitations = dataManager.getRecitations();
	for (Recitation r : recitations) {	    
	    JsonObject rcJson = Json.createObjectBuilder()
                    .add("section", r.getSection())
                    .add("instructor", r.getInstructor())
		    .add("day_time", r.getDayTime())
                    .add("location", r.getLocation())
                    .add("ta_1", r.getTa1())
		    .add("ta_2", r.getTa2()).build();
	    rcArrayBuilder.add(rcJson);
	}
	JsonArray recitationsArray = rcArrayBuilder.build();

	
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add("recitations", recitationsArray)
                .build();
        
        return dataManagerJSO;
    }
}
