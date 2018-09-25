/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import csg.CourseSiteGeneratorApp;
import csg.CourseSiteGeneratorProp;
import csg.data.CSGData;
import csg.data.CourseData;
import csg.data.SitePage;
import csg.data.TAData;
import csg.data.TeachingAssistant;
import java.util.ArrayList;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import properties_manager.PropertiesManager;

/**
 *
 * @author KI BBUM
 */
public class CourseDetailsWS {
    CourseSiteGeneratorApp app;
    
    CDWSController controller;
    
    ScrollPane courseDetailsWS;
    HBox subPane1;
    HBox subPane2;
    HBox subPane3;
    
    Label courseDetailsHeader;
    
    GridPane courseInfoGrid;
    HBox courseInfoSubHeaderBox;
    Label courseInfoSubHeaderLabel;
    Label subject;
    ComboBox subjectCB;
    Label number;
    ComboBox numberCB;
    Label semester;
    ComboBox semesterCB;
    Label year;
    ComboBox yearCB;
    Label title;
    TextField titleTF;
    Label instructorName;
    TextField instNameTF;
    Label instructorHome;
    TextField instHomeTF;
    Label exportDir;
    Label exportDirPath;
    Button exportDirChangeButton;
    
    VBox siteTemplateVBox;
    HBox siteTemplateSubHeaderBox;
    Label siteTemplateSubHeaderLabel;
    Label siteTemplateDescription;
    Label selectedTemplatePath;
    Button selectTamplateDir;
    Label sitePages;
    // FOR THE TA TABLE
    TableView<SitePage> sitePageTable;
    TableColumn<SitePage, Boolean> sitePageTableUseCol;
    TableColumn<SitePage, String>[] sitePageTableColumns;
    
    
    GridPane pageStyleGrid;
    HBox pageStyleSubHeaderBox;
    Label pageStyleSubHeaderLabel;
    Label bannerSchoolImage;
    Image bannerSchoolImageFile;
    ImageView bannerSchoolImageView;
    Label leftFooterImage;
    Image leftFooterImageFile;
    ImageView leftFooterImageView;
    Label rightFooterImage;
    Image rightFooterImageFile;
    ImageView rightFooterImageView;
    Button changeSchoolImageButton;
    Button changeLFImageButton;
    Button changeRFImageButton;
    Label stylesheet;
    ComboBox stylesheetCB;
    Label pageStyleNote;
    
    ArrayList<Node> headers;
    ArrayList<Node> subHeaders;
    ArrayList<Node> labels;
    ArrayList<Node> comboBoxes;
    ArrayList<Node> textFields;
    ArrayList<Node> buttons;
    ArrayList<Node> subPanes;
    
    
    public CourseDetailsWS(CourseSiteGeneratorApp initApp){
        // KEEP THIS FOR LATER
        app = initApp;

        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        CSGData dataComp = (CSGData) app.getDataComponent();
        CourseData data = dataComp.getCourseData();
        
        
        // SET THE PROPERTIES AND INITIATE THE DATA FIELDS CONNECTING COURSE DATA
        String courseDetailsHeaderText = props.getProperty(CourseSiteGeneratorProp.CT_COURSE_DETAILS_HEADER.toString());
        courseDetailsHeader = new Label(courseDetailsHeaderText);
        
        String courseInfoSubHeaderLabelText = props.getProperty(CourseSiteGeneratorProp.CT_COURSE_INFO_SUBHEADER.toString());
        courseInfoSubHeaderLabel = new Label(courseInfoSubHeaderLabelText);
        courseInfoSubHeaderBox = new HBox(courseInfoSubHeaderLabel);
        
        String subjectText = props.getProperty(CourseSiteGeneratorProp.CT_SUBJECT.toString());
        subject = new Label(subjectText);
        
        subjectCB = data.getSubjectCB();
        
        String numberText = props.getProperty(CourseSiteGeneratorProp.CT_NUMBER.toString());
        number = new Label(numberText);
        
        numberCB = data.getNumberCB();
        
        String semesterText = props.getProperty(CourseSiteGeneratorProp.CT_SEMESTER.toString());
        semester = new Label(semesterText);
        
        semesterCB  = data.getSemesterCB();
        
        String yearText = props.getProperty(CourseSiteGeneratorProp.CT_YEAR.toString());
        year = new Label(yearText);
        
        yearCB  = data.getYearCB();
        
        String titleText = props.getProperty(CourseSiteGeneratorProp.CT_TITLE.toString());
        title = new Label(titleText);
        
        titleTF  = data.getTitleTF();
        
        String instructorNameText = props.getProperty(CourseSiteGeneratorProp.CT_INSTRUCTOR_NAME.toString());
        instructorName = new Label(instructorNameText);
        
        instNameTF = data.getInstNameTF();
        
        String instructorHomeText = props.getProperty(CourseSiteGeneratorProp.CT_INSTRUCTOR_HOME.toString());
        instructorHome = new Label(instructorHomeText);
        
        instHomeTF = data.getInstHomeTF();
        
        String exportDirText = props.getProperty(CourseSiteGeneratorProp.CT_EXPORT_DIRECTORY.toString());
        exportDir = new Label(exportDirText);
        
        exportDirPath = data.getExportDirPath();
        
        String courseInfoChangeButtonText = props.getProperty(CourseSiteGeneratorProp.CHANGE_BUTTON.toString());
        exportDirChangeButton = new Button(courseInfoChangeButtonText);
        
        
        
        //
        String siteTemplateSubHeaderText = props.getProperty(CourseSiteGeneratorProp.CT_SITE_TEMPLATE_SUBHEADER.toString());
        siteTemplateSubHeaderLabel = new Label(siteTemplateSubHeaderText);
        siteTemplateSubHeaderBox = new HBox(siteTemplateSubHeaderLabel);
        
        String siteTemplateDescriptionText = props.getProperty(CourseSiteGeneratorProp.CT_SITE_TEMPLATE_DESCRIPTION.toString());
        siteTemplateDescription = new Label(siteTemplateDescriptionText);
        siteTemplateDescription.setWrapText(true);
        
        selectedTemplatePath = data.getSelectedTemplatePath();
        selectedTemplatePath.setPadding(new Insets(5, 0, 5, 30));
        
        String selectTamplateDirText = props.getProperty(CourseSiteGeneratorProp.CT_SELECT_TEMPLATE_DIRECTORY_BUTTON.toString());
        selectTamplateDir = new Button(selectTamplateDirText);
        
        String sitePagesText = props.getProperty(CourseSiteGeneratorProp.CT_SITE_PAGES.toString());
        sitePages = new Label(sitePagesText);
        
        // TABLE
        sitePageTable = new TableView();
        sitePageTable.setEditable(true);
        sitePageTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ObservableList<SitePage> tableData = data.getSitePages();
        sitePageTable.setItems(tableData);
        String[] valueFact = {"use", "title","fileName", "script"};
        ArrayList<String> sitePageTableHeaders = props.getPropertyOptionsList(CourseSiteGeneratorProp.CT_SITE_PAGE_TABLE_HEADERS);
        sitePageTableColumns = new TableColumn[sitePageTableHeaders.size()];
        sitePageTableUseCol = new TableColumn<SitePage, Boolean>(sitePageTableHeaders.get(0));
        
        sitePageTableUseCol.setCellValueFactory(new Callback<CellDataFeatures<SitePage, Boolean>, ObservableValue<Boolean>>() {
            
            @Override
            public ObservableValue<Boolean> call(CellDataFeatures<SitePage, Boolean> param) {
                SitePage sp = param.getValue();
 
                SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(sp.getUse());
 
                // Note: singleCol.setOnEditCommit(): Not work for
                // CheckBoxTableCell.
 
                // When "Single?" column change.
                booleanProp.addListener(new ChangeListener<Boolean>() {
                    //@Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                            Boolean newValue) {
                        sp.setUse(newValue);
                    }
                });
                return booleanProp;
            }
        });
 
        sitePageTableUseCol.setCellFactory(new Callback<TableColumn<SitePage, Boolean>, TableCell<SitePage, Boolean>>() {
            //@Override
            public TableCell<SitePage, Boolean> call(TableColumn<SitePage, Boolean> p) {
                CheckBoxTableCell<SitePage, Boolean> cell = new CheckBoxTableCell<SitePage, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
        
        /*
        sitePageTableUseCol.setCellValueFactory(cellData -> cellData.getValue().getUse());
        
        //sitePageTableUseCol.setCellValueFactory(
        //        new PropertyValueFactory<SitePage, Boolean>("use")
        //);
        
        sitePageTableUseCol.setCellFactory(column -> new CheckBoxTableCell());
        sitePageTableUseCol.setEditable( true );
        */
        
        
        sitePageTable.getColumns().add(sitePageTableUseCol);
        
        
        for ( int i = 1; i < sitePageTableColumns.length; i++ ){
            sitePageTableColumns[i] = new TableColumn(sitePageTableHeaders.get(i));
            sitePageTableColumns[i].setCellValueFactory(
                new PropertyValueFactory<SitePage, String>(valueFact[i])
            );
            sitePageTable.getColumns().add(sitePageTableColumns[i]);
        }
        
        sitePageTable.setPrefHeight(150);
        
        
        //
        String pageStyleSubHeaderText = props.getProperty(CourseSiteGeneratorProp.CT_PAGE_STYLE_SUBHEADER.toString());
        pageStyleSubHeaderLabel = new Label(pageStyleSubHeaderText);
        pageStyleSubHeaderBox = new HBox(pageStyleSubHeaderLabel);
        
        String bannerSchoolImageText = props.getProperty(CourseSiteGeneratorProp.CT_BANNER_SCHOOL_IMAGE.toString());
        bannerSchoolImage = new Label(bannerSchoolImageText);
        
        //bannerSchoolImageFile = new Image("file:./images/SBULogo.jpg");
        bannerSchoolImageView = data.getBannerSchoolImageView();
        
        String leftFooterImageText = props.getProperty(CourseSiteGeneratorProp.CT_LEFT_FOOTER_IMAGE.toString());
        leftFooterImage = new Label(leftFooterImageText);
        
        //leftFooterImageFile = new Image("file:./images/SBULogo.jpg");
        leftFooterImageView = data.getLeftFooterImageView();
        
        String rightFooterImageText = props.getProperty(CourseSiteGeneratorProp.CT_RIGHT_FOOTER_IMAGE.toString());
        rightFooterImage = new Label(rightFooterImageText);
        
        //rightFooterImageFile  = new Image("file:./images/SBULogo.jpg");
        rightFooterImageView = data.getRightFooterImageView();
        
        String changeSchoolImageButtonText = props.getProperty(CourseSiteGeneratorProp.CHANGE_BUTTON.toString());
        changeSchoolImageButton = new Button(changeSchoolImageButtonText);
        String changeLFImageButtonText = props.getProperty(CourseSiteGeneratorProp.CHANGE_BUTTON.toString());
        changeLFImageButton = new Button(changeLFImageButtonText);
        String changeRFImageButtonText = props.getProperty(CourseSiteGeneratorProp.CHANGE_BUTTON.toString());
        changeRFImageButton = new Button(changeRFImageButtonText);
        
        String stylesheetText = props.getProperty(CourseSiteGeneratorProp.CT_STYLESHEET.toString());
        stylesheet = new Label(stylesheetText);
        
        stylesheetCB = data.getStylesheetCB();
        
        String pageStyleNoteText = props.getProperty(CourseSiteGeneratorProp.CT_PAGE_STYLE_NOTE.toString());
        pageStyleNote = new Label(pageStyleNoteText);
        
        // SET EVENT HANDLERS
        controller = new CDWSController(app);
        
        exportDirChangeButton.setOnAction(e -> {
            controller.changeExportDir();
        });
        selectTamplateDir.setOnAction(e -> {
            controller.selectTemplateDir();
        });
        changeSchoolImageButton.setOnAction(e -> {
            controller.changeBannerImg();
        });
        changeLFImageButton.setOnAction(e -> {
            controller.changeLeftFooterImg();
        });
        changeRFImageButton.setOnAction(e -> {
            controller.changeRightFooterImg();
        });
        
        
        
        
        // BUILD THE PANE
        // COURSE INFO PART
        courseInfoGrid = new GridPane();
        courseInfoGrid.setHgap(10);
        courseInfoGrid.setVgap(10);
        
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        ColumnConstraints column3 = new ColumnConstraints(50);
        //column2.setHgrow(Priority.ALWAYS);
        courseInfoGrid.getColumnConstraints().addAll(column1, column2, column3);
        //Pane spacer = new Pane();
        //HBox.setHgrow(spacer, Priority.ALWAYS);
        
        courseInfoGrid.add(courseInfoSubHeaderBox, 0, 0);
        courseInfoGrid.add(subject, 0, 1);
        
        //HBox subCBnumLabNumCB = new HBox(subjectCB, spacer, number, numberCB);
        courseInfoGrid.add(subjectCB, 1, 1);
        courseInfoGrid.add(number, 3, 1);
        courseInfoGrid.add(numberCB, 4, 1);
        
        courseInfoGrid.add(semester, 0, 2);
        //HBox semCByearLabYearCB = new HBox(semesterCB, spacer, year, yearCB);
        courseInfoGrid.add(semesterCB, 1, 2);
        courseInfoGrid.add(year, 3, 2);
        courseInfoGrid.add(yearCB, 4, 2);
        
        courseInfoGrid.add(title, 0, 3);
        courseInfoGrid.add(titleTF, 1, 3, 4, 1);
        courseInfoGrid.add(instructorName, 0, 4);
        courseInfoGrid.add(instNameTF, 1, 4, 4, 1);
        courseInfoGrid.add(instructorHome, 0, 5);
        courseInfoGrid.add(instHomeTF, 1, 5, 4, 1);
        courseInfoGrid.add(exportDir, 0, 6);
        //HBox dirPathChangeButton = new HBox(exportDirPath, spacer, courseInfoChangeButton);
        courseInfoGrid.add(exportDirPath, 1, 6, 4, 1);
        courseInfoGrid.add(exportDirChangeButton, 5, 6);
        
        
        // SITE TEMPLATE PART
        siteTemplateVBox = new VBox(15);
        sitePages.setPadding(new Insets(16, 0, 0, 0));
        siteTemplateVBox.getChildren().addAll(siteTemplateSubHeaderBox, siteTemplateDescription, selectedTemplatePath,
                                                selectTamplateDir, sitePages, sitePageTable);
        
        // PAGE STYLE PART
        pageStyleGrid = new GridPane();
        pageStyleGrid.setHgap(15);
        pageStyleGrid.setVgap(14);
        
        pageStyleGrid.add(pageStyleSubHeaderBox, 0, 0);
        pageStyleGrid.add(bannerSchoolImage, 0, 1);
        pageStyleGrid.add(bannerSchoolImageView, 1, 1);
        pageStyleGrid.add(changeSchoolImageButton, 2, 1);
        pageStyleGrid.add(leftFooterImage, 0, 2);
        pageStyleGrid.add(leftFooterImageView, 1, 2);
        pageStyleGrid.add(changeLFImageButton, 2, 2);
        pageStyleGrid.add(rightFooterImage, 0, 3);
        pageStyleGrid.add(rightFooterImageView, 1, 3);
        pageStyleGrid.add(changeRFImageButton, 2, 3);
        pageStyleGrid.add(stylesheet, 0, 4);
        pageStyleGrid.add(stylesheetCB, 1, 4);
        pageStyleGrid.add(pageStyleNote, 0, 5, 3, 1);

        
        
        VBox courseDetailsWSVB = new VBox(12);
        courseDetailsWSVB.setPadding(new Insets(10, 0, 15, 0));
        //courseDetailsWSVB.setAlignment(Pos.CENTER);
        courseDetailsWSVB.setMaxWidth(800);
        subPane1 = new HBox(courseInfoGrid);
        subPane1.setMargin(courseInfoGrid, new Insets(15, 21, 17, 21));
        //subPane1.prefWidthProperty().bind(courseDetailsWSVB.widthProperty().multiply(0.95));
        subPane2 = new HBox(siteTemplateVBox);
        subPane2.setMargin(siteTemplateVBox, new Insets(15, 21, 17, 21));
        //subPane2.prefWidthProperty().bind(courseDetailsWSVB.widthProperty().multiply(0.95));
        subPane3 = new HBox(pageStyleGrid);
        subPane3.setMargin(pageStyleGrid, new Insets(15, 21, 17, 21));
        //subPane3.prefWidthProperty().bind(courseDetailsWSVB.widthProperty().multiply(0.95));
        courseDetailsWSVB.getChildren().addAll(courseDetailsHeader, subPane1, subPane2, subPane3);
        
        // TABLE WIDTH
        sitePageTable.prefWidthProperty().bind(courseDetailsWSVB.widthProperty());
        
        courseDetailsWS = new ScrollPane();
        //courseDetailsWS.setFitToHeight(true);
        //courseDetailsWS.setFitToWidth(true);
        StackPane p = new StackPane();
        p.prefWidthProperty().bind(courseDetailsWS.widthProperty());
        p.setAlignment(Pos.CENTER);
        p.getChildren().add(courseDetailsWSVB);
        
        courseDetailsWS.setContent(p);
        
        
        
    }
    
    
    
    // get methods for style
    public ArrayList<Node> getHeaders(){
        headers = new ArrayList<Node>();
        headers.add(courseDetailsHeader);
        return headers;
    }
    
    public ArrayList<Node> getSubHeaders(){
        subHeaders = new ArrayList<Node>();
        subHeaders.add(courseInfoSubHeaderLabel);
        subHeaders.add(siteTemplateSubHeaderLabel);
        subHeaders.add(pageStyleSubHeaderLabel);
        return subHeaders;
    }
    
    public ArrayList<Node> getLabels(){
        labels = new ArrayList<Node>();
        labels.add(subject);
        labels.add(number);
        labels.add(semester);
        labels.add(year);
        labels.add(title);
        labels.add(instructorName);
        labels.add(instructorHome);
        labels.add(exportDir);
        labels.add(exportDirPath);
        labels.add(siteTemplateDescription);
        labels.add(selectedTemplatePath);
        labels.add(sitePages);
        labels.add(bannerSchoolImage);
        labels.add(leftFooterImage);
        labels.add(rightFooterImage);
        labels.add(stylesheet);
        labels.add(pageStyleNote);
        return labels;
    }
    
    public ArrayList<Node> getComboBoxes(){
        comboBoxes = new ArrayList<Node>();
        comboBoxes.add(subjectCB);
        comboBoxes.add(numberCB);
        comboBoxes.add(semesterCB);
        comboBoxes.add(yearCB);
        comboBoxes.add(stylesheetCB);
        return comboBoxes;
    }
    
    public ArrayList<Node> getTextFields(){
        textFields = new ArrayList<Node>();
        textFields.add(titleTF);
        textFields.add(instNameTF);
        textFields.add(instHomeTF);
        return textFields;
    }
    
    public ArrayList<Node> getButtons(){
        buttons = new ArrayList<Node>();
        buttons.add(exportDirChangeButton);
        buttons.add(selectTamplateDir);
        buttons.add(changeSchoolImageButton);
        buttons.add(changeLFImageButton);
        buttons.add(changeRFImageButton);
        return buttons;
    }
    
    public TableView getTable(){
        return sitePageTable;
    }
    
    public ArrayList<Node> getsubPanes(){
        subPanes = new ArrayList<Node>();
        subPanes.add(subPane1);
        subPanes.add(subPane2);
        subPanes.add(subPane3);
        return subPanes;
    }
    
    
    public ScrollPane getCDWS(){
        return courseDetailsWS;
    }

    private Insets Insets() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
