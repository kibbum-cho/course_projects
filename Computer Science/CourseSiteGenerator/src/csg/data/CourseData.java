/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import csg.CourseSiteGeneratorApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author KI BBUM
 */
public class CourseData {
    CourseSiteGeneratorApp app;
    
    ComboBox subjectCB;
    ComboBox numberCB;
    ComboBox semesterCB;
    ComboBox yearCB;
    TextField titleTF;
    TextField instNameTF;
    TextField instHomeTF;
    Label exportDirPath;
    Label selectedTemplatePath;
        
    ObservableList<SitePage> sitePages;
    
    Image bannerSchoolImageFile;
    ImageView bannerSchoolImageView;
    Image leftFooterImageFile;
    ImageView leftFooterImageView;
    Image rightFooterImageFile;
    ImageView rightFooterImageView;
    ComboBox stylesheetCB;
    
    StringProperty bannerSchoolImagePath;
    StringProperty leftFooterImagePath;
    StringProperty rightFooterImagePath;
    
    final String DEFAULT_IMAGE_PATH = "file:./images/default.jpg";
    
    public CourseData(CourseSiteGeneratorApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;

        subjectCB = new ComboBox();
        subjectCB.getItems().addAll(
                "CSE", "AMS"
        );
        numberCB = new ComboBox();
        numberCB.getItems().addAll(
                "219", "214", "114", "220", "308", "320"
        );
        semesterCB = new ComboBox();
        semesterCB.getItems().addAll(
                "Spring", "Summer", "Fall", "Winter"
        );
        yearCB = new ComboBox();
        yearCB.getItems().addAll(
                "2017", "2018", "2019", "2020"
        );
        titleTF = new TextField();
        instNameTF = new TextField();
        instHomeTF = new TextField();
        exportDirPath = new Label("./work/");
        selectedTemplatePath = new Label("./templates/");
        
        // CONSTRUCT THE LIST OF TAs FOR THE TABLE
        sitePages = FXCollections.observableArrayList();
        
        sitePages.add(new SitePage(true, "Home", "index.html", "HomeBuilder.js"));
        sitePages.add(new SitePage(true, "Syllabus", "sysllabus.html", "SyllabusBuilder.js"));
        sitePages.add(new SitePage(true, "Schedule", "schedule.html", "ScheduleBuilder.js"));
        sitePages.add(new SitePage(true, "HWs", "hws.html", "HWsBuilder.js"));
        sitePages.add(new SitePage(true, "Projects", "projects.html", "ProjectsBuilder.js"));
        
        
        bannerSchoolImagePath = new SimpleStringProperty(DEFAULT_IMAGE_PATH);
        bannerSchoolImageFile = new Image(DEFAULT_IMAGE_PATH);
        bannerSchoolImageView = new ImageView(bannerSchoolImageFile);
        leftFooterImagePath = new SimpleStringProperty(DEFAULT_IMAGE_PATH);
        leftFooterImageFile = new Image(DEFAULT_IMAGE_PATH);
        leftFooterImageView = new ImageView(leftFooterImageFile);
        rightFooterImagePath = new SimpleStringProperty(DEFAULT_IMAGE_PATH);
        rightFooterImageFile = new Image(DEFAULT_IMAGE_PATH);
        rightFooterImageView = new ImageView(rightFooterImageFile);
        stylesheetCB = new ComboBox();
        stylesheetCB.getItems().addAll(
                "sea_wolf.css", "sea_wolf2.css", "sea_wolf3.css"
        );
        
    }
    
    public void resetData() {
        subjectCB.getSelectionModel().clearSelection();
        numberCB.getSelectionModel().clearSelection();
        semesterCB.getSelectionModel().clearSelection();
        yearCB.getSelectionModel().clearSelection();
        titleTF.clear();
        instNameTF.clear();
        instHomeTF.clear();
        exportDirPath.setText("./work/");
        selectedTemplatePath.setText("./templates/");
        sitePages.clear();
        bannerSchoolImagePath = new SimpleStringProperty(DEFAULT_IMAGE_PATH);
        bannerSchoolImageFile = new Image(bannerSchoolImagePath.get());
        bannerSchoolImageView = new ImageView(bannerSchoolImageFile);
        leftFooterImagePath = new SimpleStringProperty(DEFAULT_IMAGE_PATH);
        leftFooterImageFile = new Image(leftFooterImagePath.get());
        leftFooterImageView = new ImageView(leftFooterImageFile);
        rightFooterImagePath = new SimpleStringProperty(DEFAULT_IMAGE_PATH);
        rightFooterImageFile = new Image(rightFooterImagePath.get());
        rightFooterImageView = new ImageView(rightFooterImageFile);
        
        stylesheetCB.getSelectionModel().clearSelection();
        
    }
    
    public ComboBox getSubjectCB(){
        return subjectCB;
    }
    public ComboBox getNumberCB(){
        return numberCB;
    }
    public ComboBox getSemesterCB(){
        return semesterCB;
    }
    public ComboBox getYearCB(){
        return yearCB;
    }
    public TextField  getTitleTF(){
        return titleTF;
    }
    public TextField getInstNameTF(){
        return instNameTF;
    }
    public TextField getInstHomeTF(){
        return instHomeTF;
    }
    public Label  getExportDirPath(){
        return exportDirPath;
    }
    public Label  getSelectedTemplatePath(){
        return selectedTemplatePath;
    }
    public ObservableList getSitePages() {
        return sitePages;
    }
    public ImageView  getBannerSchoolImageView(){
        return bannerSchoolImageView;
    }
    public ImageView  getLeftFooterImageView(){
        return leftFooterImageView;
    }
    public ImageView  getRightFooterImageView(){
        return rightFooterImageView;
    }
    public SimpleStringProperty getBannerSchoolImagePath(){
        return (SimpleStringProperty)bannerSchoolImagePath;
    }
    public SimpleStringProperty getLeftFooterImagePath(){
        return (SimpleStringProperty)leftFooterImagePath;
    }
    public SimpleStringProperty getRightFooterImagePath(){
        return (SimpleStringProperty)rightFooterImagePath;
    }
    public ComboBox getStylesheetCB(){
        return stylesheetCB;
    }
    
    public boolean[] getExportList(){
        boolean[] el = {false,false,false,false,false};
        int i = 0;
        for(SitePage sp : sitePages){
            el[i] = sp.getUse();
            i++;
        }
        return el;
    }
    

}
