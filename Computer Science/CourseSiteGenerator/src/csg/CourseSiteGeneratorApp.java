/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg;

import csg.data.CSGData;
import csg.data.RecitationData;
import csg.data.TeachingAssistant;
import csg.file.CSGFiles;
import csg.style.CSGStyle;
import csg.workspace.Workspace;
import djf.AppTemplate;
import java.util.Locale;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author KI BBUM
 */
public class CourseSiteGeneratorApp extends AppTemplate {

    @Override
    public void buildAppComponentsHook() {
        // CONSTRUCT ALL FOUR COMPONENTS. NOTE THAT FOR THIS APP
        // THE WORKSPACE NEEDS THE DATA COMPONENT TO EXIST ALREADY
        // WHEN IT IS CONSTRUCTED, SO BE CAREFUL OF THE ORDER
        dataComponent = new CSGData(this);
        ((CSGData)dataComponent).syncTAsForRecitationData();
        
        workspaceComponent = new Workspace(this);
        fileComponent = new CSGFiles(this);
        styleComponent = new CSGStyle(this);
    }
    
    /**
     * This is where program execution begins. Since this is a JavaFX app it
     * will simply call launch, which gets JavaFX rolling, resulting in sending
     * the properly initialized Stage (i.e. window) to the start method inherited
     * from AppTemplate, defined in the Desktop Java Framework.
     */
    public static void main(String[] args) {
	Locale.setDefault(Locale.US);
	launch(args);
    }
}
