/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.file;

import csg.CourseSiteGeneratorApp;
import csg.data.CSGData;
import csg.data.CourseData;
import csg.data.TAData;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import static djf.settings.AppPropertyType.EXPORT_COMPLETED_MESSAGE;
import static djf.settings.AppPropertyType.EXPORT_COMPLETED_TITLE;
import static djf.settings.AppPropertyType.EXPORT_WORK_TITLE;
import static djf.settings.AppStartupConstants.PATH_WORK;
import djf.ui.AppMessageDialogSingleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javafx.stage.DirectoryChooser;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.stream.JsonGenerator;
import javax.json.JsonWriterFactory;
import properties_manager.PropertiesManager;

/**
 *
 * @author KI BBUM
 */
public class CSGFiles implements AppFileComponent{
    CourseSiteGeneratorApp app;
    CourseFiles courseFiles;
    TAFiles taFiles;
    RecitationFiles recitationFiles;
    ScheduleFiles scheduleFiles;
    ProjectFiles projectFiles;
    
    public CSGFiles(CourseSiteGeneratorApp initApp){
        app = initApp;
        courseFiles = new CourseFiles(app);
        taFiles = new TAFiles(app);
        recitationFiles = new RecitationFiles(app);
        scheduleFiles = new ScheduleFiles(app);
        projectFiles = new ProjectFiles(app);
    }

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        CSGData csgData = (CSGData)data;
        
        //taFiles.saveData(csgData.getTAData(), filePath);
        //cdFiles.getJson(csgData.getCourseData());
        
        
        // THEN PUT IT ALL TOGETHER IN A JsonObject
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add("course_details_json", courseFiles.getJson(csgData.getCourseData()))
                .add("ta_data_json", taFiles.getJson(csgData.getTAData()))
                .add("recitation_data_json", recitationFiles.getJson(csgData.getRecitationData()))
                .add("schedule_data_json", scheduleFiles.getJson(csgData.getScheduleData()))
                .add("project_data_json", projectFiles.getJson(csgData.getProjectData()))
                .build();
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(dataManagerJSO);
        jsonWriter.close();
        
        // INIT THE WRITER
        OutputStream os = new FileOutputStream(filePath);
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter(filePath);
        pw.write(prettyPrinted);
        pw.close();
    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        CSGData csgData = (CSGData)data;
        
        JsonObject json = loadJSONFile(filePath);
        JsonObject courseJson = json.getJsonObject("course_details_json");
        JsonObject taJson = json.getJsonObject("ta_data_json");
        JsonObject recitationJson = json.getJsonObject("recitation_data_json");
        JsonObject scheduleJson = json.getJsonObject("schedule_data_json");
        JsonObject projectJson = json.getJsonObject("project_data_json");
        
        
        taFiles.loadData(csgData.getTAData(), taJson);
    }
    
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    
    @Override
    public void exportData() throws IOException {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        CSGData dataComp = (CSGData)app.getDataComponent();
        CourseData data = (CourseData)dataComp.getCourseData();
        
        String exportPath = data.getExportDirPath().getText();
        String templatePath = data.getSelectedTemplatePath().getText();
        String stylesheetPath = ".\\work\\css\\" + data.getStylesheetCB().getSelectionModel().getSelectedItem();
        
        String homeJsonPath = templatePath+"\\Home\\js\\HomeData.json";
        String syllabusJsonPath1 = templatePath+"\\Syllabus\\js\\OfficeHoursGridData.json";
        String syllabusJsonPath2 = templatePath+"\\Syllabus\\js\\RecitationsData.json";
        String scheduleJsonPath = templatePath+"\\Schedule\\js\\ScheduleData.json";
        String hwsJsonPath = templatePath+"\\HWs\\js\\HWsData.json";
        String projectJsonPath = templatePath+"\\Projects\\js\\TeamsAndStudents.json";
        
        
        //String newPath = "..\\CourseSiteGeneratorTester\\Syllabus\\js\\OfficeHoursGridData.json";
        boolean[] exportList = data.getExportList();
        if(exportList[0]){
            File target = new File(homeJsonPath);
            saveForExport(courseFiles.getJson(dataComp.getCourseData()), target);
            copy(templatePath+"\\Home", exportPath);
        }
        if(exportList[1]){
            File target = new File(syllabusJsonPath1);
            saveForExport(taFiles.getJson(dataComp.getTAData()), target);
            File target2 = new File(syllabusJsonPath2);
            saveForExport(recitationFiles.getJson(dataComp.getRecitationData()), target2);
            copy(templatePath+"\\Syllabus", exportPath);
        }
        if(exportList[2]){
            File target = new File(scheduleJsonPath);
            saveForExport(scheduleFiles.getJson(dataComp.getScheduleData()), target);
            copy(templatePath+"\\Schedule", exportPath);
        }
        if(exportList[3]){
            JsonArray hwJson = scheduleFiles.getJson(dataComp.getScheduleData()).getJsonArray("hws");
            JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add("hws", hwJson)
                .build();
            File target = new File(hwsJsonPath);
            saveForExport(dataManagerJSO, target);
            copy(templatePath+"\\HWs", exportPath);
        }
        if(exportList[4]){
            File target = new File(projectJsonPath);
            saveForExport(projectFiles.getJson(dataComp.getProjectData()), target);
            copy(templatePath+"\\Projects", exportPath);
        }
        //copy(stylesheetPath, exportPath+ "\\css");
        
        AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
        dialog.show(props.getProperty(EXPORT_COMPLETED_TITLE),props.getProperty(EXPORT_COMPLETED_MESSAGE));
        
    }

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // EXPORT HELPER
    public void copy(String oldPath, String newPath) { 
        try { 
            (new File(newPath)).mkdirs();
            File old =new File(oldPath); 
            String[] file = old.list(); 
            File targget = null; 
            for (int i = 0; i < file.length; i++) { 
                if(oldPath.endsWith(File.separator))
                    targget = new File(oldPath+file[i]); 
                else
                    targget = new File(oldPath+File.separator+file[i]); 
                if(targget.isFile()){ 
                    FileInputStream input = new FileInputStream(targget); 
                    FileOutputStream output = new FileOutputStream(newPath + "/" + (targget.getName()).toString()); 
                    byte[] b = new byte[1024 * 5]; 
                    int templength; 
                    while ( (templength = input.read(b)) != -1)
                        output.write(b, 0, templength); 
                    output.flush(); 
                    output.close(); 
                    input.close(); 
                } 
                if(targget.isDirectory())
                    copy(oldPath+"/"+file[i],newPath+"/"+file[i]); 
            } 
        } 
        catch (Exception e) {  
            e.printStackTrace(); 
        } 
    }
    
    private void saveForExport(JsonObject jo, File selectedFile) throws IOException {
	// SAVE IT TO A FILE
        
	Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(jo);
        jsonWriter.close();
        
        // INIT THE WRITER
        OutputStream os = new FileOutputStream(selectedFile.getPath());
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(jo);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter(selectedFile.getPath());
        pw.write(prettyPrinted);
        pw.close();
    }
    
}
