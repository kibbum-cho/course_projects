/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.file;

import csg.CourseSiteGeneratorApp;
import csg.data.CourseData;
import csg.data.SitePage;
import javafx.collections.ObservableList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 *
 * @author KI BBUM
 */
public class CourseFiles {
    CourseSiteGeneratorApp app;
    
    
    public CourseFiles(CourseSiteGeneratorApp initApp){
        app = initApp;
    }
    
    public JsonObject getJson(CourseData data){
        // GET THE DATA
	CourseData dataManager = (CourseData)data;

	// NOW BUILD THE TA JSON OBJCTS TO SAVE
	JsonArrayBuilder spArrayBuilder = Json.createArrayBuilder();
	ObservableList<SitePage> sps = dataManager.getSitePages();
	for (SitePage sp : sps) {	    
	    JsonObject spJson = Json.createObjectBuilder()
                    .add("use", sp.getUse())
                    .add("title", sp.getTitle())
		    .add("file_name", sp.getFileName())
		    .add("script", sp.getScript()).build();
	    spArrayBuilder.add(spJson);
	}
	JsonArray sitePagesArray = spArrayBuilder.build();

	
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add("subject", (String)dataManager.getSubjectCB().getSelectionModel().getSelectedItem())
                .add("number", (String)dataManager.getNumberCB().getSelectionModel().getSelectedItem())
                .add("semester", (String)dataManager.getSemesterCB().getSelectionModel().getSelectedItem())
                .add("year", (String)dataManager.getYearCB().getSelectionModel().getSelectedItem())
                .add("title", dataManager.getTitleTF().getText())
                .add("instructor_name", dataManager.getInstNameTF().getText())
                .add("instructor_home", dataManager.getInstHomeTF().getText())
                .add("export_dir", dataManager.getExportDirPath().getText())
                .add("template_dir", dataManager.getSelectedTemplatePath().getText())
                .add("site_pages", sitePagesArray)
                .add("school_image_path", dataManager.getBannerSchoolImagePath().get())
                .add("left_footer_path", dataManager.getLeftFooterImagePath().get())
                .add("right_footer_path", dataManager.getRightFooterImagePath().get())
                .add("stylesheet", (String)dataManager.getStylesheetCB().getSelectionModel().getSelectedItem())
		.build();
        
        return dataManagerJSO;
    }
    
    
    
}
