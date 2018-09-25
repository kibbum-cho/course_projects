/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import csg.CourseSiteGeneratorApp;
import csg.data.CSGData;
import csg.data.CourseData;
import static djf.settings.AppPropertyType.EXPORT_WORK_TITLE;
import static djf.settings.AppPropertyType.SAVE_AS_WORK_TITLE;
import static djf.settings.AppPropertyType.SELECT_TEMPLATE_TITLE;
import static djf.settings.AppPropertyType.WORK_FILE_EXT;
import static djf.settings.AppPropertyType.WORK_FILE_EXT_DESC;
import static djf.settings.AppStartupConstants.PATH_IMAGES;
import static djf.settings.AppStartupConstants.PATH_TEMPLATES;
import static djf.settings.AppStartupConstants.PATH_WORK;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javax.imageio.ImageIO;
import jtps.jTPS;
import properties_manager.PropertiesManager;

/**
 *
 * @author KI BBUM
 */
public class CDWSController {
    CourseSiteGeneratorApp app;
    jTPS jtps;
    //CourseDetailsWS workspace;
    //CourseData data;
    //PropertiesManager props;
    
    public CDWSController(CourseSiteGeneratorApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
        jtps = new jTPS();
    }
    
    public void changeExportDir(){
        CSGData dataCompo = (CSGData)app.getDataComponent();
        CourseData data = dataCompo.getCourseData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(PATH_WORK));
        dc.setTitle(props.getProperty(EXPORT_WORK_TITLE));
        try {
            String path = dc.showDialog(app.getGUI().getWindow()).getPath();
            data.getExportDirPath().setText(path);
        }catch(NullPointerException e){
            
        }
    }
    
    public void selectTemplateDir(){
        CSGData dataCompo = (CSGData)app.getDataComponent();
        CourseData data = dataCompo.getCourseData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(PATH_TEMPLATES));
        dc.setTitle(props.getProperty(SELECT_TEMPLATE_TITLE));
        try {
            String path = dc.showDialog(app.getGUI().getWindow()).getPath();
            data.getSelectedTemplatePath().setText(path);
        }catch(NullPointerException e){
            
        }
        //deal 
    }
    
    public void changeBannerImg(){
        CSGData dataCompo = (CSGData)app.getDataComponent();
        CourseData data = dataCompo.getCourseData();
        
        changeImgHelper(data.getBannerSchoolImageView(), data.getBannerSchoolImagePath());
    }
    public void changeLeftFooterImg(){
        CSGData dataCompo = (CSGData)app.getDataComponent();
        CourseData data = dataCompo.getCourseData();
        
        changeImgHelper(data.getLeftFooterImageView(), data.getLeftFooterImagePath());
    }
    public void changeRightFooterImg(){
        
        CSGData dataCompo = (CSGData)app.getDataComponent();
        CourseData data = dataCompo.getCourseData();
        
        changeImgHelper(data.getRightFooterImageView(), data.getRightFooterImagePath());
    }
    
    private void changeImgHelper(ImageView imgView, SimpleStringProperty imgPath){
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_IMAGES));
        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fc.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
        
        //Show open file dialog
        File file = fc.showOpenDialog(app.getGUI().getWindow());
        if (file != null){
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                imgView.setImage(image);
                imgPath.set(file.getPath());
            } catch (IOException ex) {
                //Logger.getLogger(JavaFXPixel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
