/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import csg.CourseSiteGeneratorApp;
import djf.components.AppDataComponent;
import javafx.util.StringConverter;

/**
 *
 * @author KI BBUM
 */
public class CSGData implements AppDataComponent {
    CourseSiteGeneratorApp app;
    TAData taData;
    CourseData courseData;
    RecitationData recitationData;
    ScheduleData scheduleData;
    ProjectData projectData;
    
    
    public CSGData(CourseSiteGeneratorApp initApp){
        app = initApp;
        taData = new TAData(initApp);
        courseData = new CourseData(initApp);
        recitationData = new RecitationData(initApp);
        scheduleData = new ScheduleData(initApp);
        projectData = new ProjectData(initApp);
    }

    @Override
    public void resetData() {
        taData.resetData();
    }
    
    public TAData getTAData(){
        return taData;
    }
    
    public CourseData getCourseData(){
        return courseData;
    }
    
    public RecitationData getRecitationData(){
        return recitationData;
    }
    public ScheduleData getScheduleData(){
        return scheduleData;
    }
    public ProjectData getProjectData(){
        return projectData;
    }
    
    
    
    
    public void syncTAsForRecitationData(){
        //need to sync tas in recitation data with ta data
        CSGData dataComp = (CSGData)app.getDataComponent();
        RecitationData rd = dataComp.getRecitationData();
        rd.setTAs(dataComp.getTAData().getTeachingAssistants());
        rd.getTACB1().setItems(dataComp.getTAData().getTeachingAssistants());
        rd.getTACB2().setItems(dataComp.getTAData().getTeachingAssistants());
        StringConverter<TeachingAssistant> converter = new StringConverter<TeachingAssistant>() {
            @Override
            public String toString(TeachingAssistant t) {
                return t.toString();
            }
            @Override
            public TeachingAssistant fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        rd.getTACB1().setConverter(converter);
        rd.getTACB2().setConverter(converter);
    }
}
