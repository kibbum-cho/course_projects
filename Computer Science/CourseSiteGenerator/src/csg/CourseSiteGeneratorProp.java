/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg;

/**
 *
 * @author KI BBUM
 */
public enum CourseSiteGeneratorProp {
    // FOR SIMPLE OK/CANCEL DIALOG BOXES
    OK_PROMPT,
    CANCEL_PROMPT,
    
    // BASIC PROPERTIES FOR WORKSPACE
    // TAB NAMES
    COURSE_DETAILS_TAB,
    TA_DATA_TAB,
    RECITATION_DATA_TAB,
    SCHEDULE_DATA_TAB,
    PROJECT_DATA_TAB,
    
    //COMMON STUFF
    CHANGE_BUTTON,
    ADD_UPDATE_BUTTON,
    CLEAR_BUTTON,
    REMOVE_BUTTON,
    
    // FOR COURSE DETAILS TAB
    CT_COURSE_DETAILS_HEADER,
    CT_COURSE_INFO_SUBHEADER,
    CT_SUBJECT,
    CT_NUMBER,
    CT_SEMESTER,
    CT_YEAR,
    CT_TITLE,
    CT_INSTRUCTOR_NAME,
    CT_INSTRUCTOR_HOME,
    CT_EXPORT_DIRECTORY,
    
    
    CT_SITE_TEMPLATE_SUBHEADER,
    CT_SITE_TEMPLATE_DESCRIPTION,
    CT_SELECT_TEMPLATE_DIRECTORY_BUTTON,
    CT_SITE_PAGES,
    CT_SITE_PAGE_TABLE_HEADERS,
    
    CT_PAGE_STYLE_SUBHEADER,
    CT_BANNER_SCHOOL_IMAGE,
    CT_LEFT_FOOTER_IMAGE,
    CT_RIGHT_FOOTER_IMAGE,
    CT_STYLESHEET,
    CT_PAGE_STYLE_NOTE,
    
    
    // THESE ARE FOR TEXT PARTICULAR TO THE APP'S TADATA TAB CONTROLS
    TAS_HEADER_TEXT,
    UNDERGRAD_COLUMN_TEXT,
    UNDERGRAD_CHECKBOX,
    NAME_COLUMN_TEXT,
    EMAIL_COLUMN_TEXT,
    NAME_PROMPT_TEXT,
    EMAIL_PROMPT_TEXT,
    ADD_BUTTON_TEXT,
    
    UPDATE_BUTTON_TEXT,
    CLEAR_BUTTON_TEXT,
    OFFICE_HOURS_SUBHEADER,
    OFFICE_HOURS_TABLE_HEADERS,
    DAYS_OF_WEEK,
    OFFICE_HOURS_START_TIME,
    OFFICE_HOURS_END_TIME,
    CHANGE_HOURS_BUTTON_TEXT,
    
    // THESE ARE FOR ERROR MESSAGES PARTICULAR TO THE APP
    MISSING_TA_NAME_TITLE,
    MISSING_TA_NAME_MESSAGE,
    MISSING_TA_EMAIL_TITLE,
    MISSING_TA_EMAIL_MESSAGE,
    TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE,
    TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE,
    
    TA_EMAIL_NOT_VALID_TITLE,
    TA_EMAIL_NOT_VALID_MESSAGE,
    
    COMBOBOX_HOUR_ERROR_TITLE,
    COMBOBOX_START_HOUR_ERROR_MESSAGE,
    COMBOBOX_END_HOUR_ERROR_MESSAGE,
    COMBOBOX_SELECTION_CONFLICT_TITLE,
    COMBOBOX_SELECTION_CONFLICT_MESSAGE,
    
    
    // RECITATION TAB
    RT_RECITATIONS_HEADER,
    RT_RECITATION_TABLE_HEADERS,
    RT_ADD_EDIT_SUBHEADER,
    RT_SECTION,
    RT_INSTRUCTOR,
    RT_DAY_TIME,
    RT_LOCATION,
    RT_SUPERVISING_TA,
    RT_ADD_UPDATE_BUTTON,
    RT_CLEAR_BUTTON,
    
    // SCHEDULE TAB
    ST_SCHEDULE_HEADER,
    ST_CALENDAR_BOUNDARIES_SUBHEADER,
    ST_STARTING_MONDAY,
    ST_ENDING_FRIDAY,
    ST_SCHEDULE_ITEMS_SUBHEADER,
    ST_SCHEDULE_ITEMS_TABLE_HEADERS,
    ST_ADD_EDIT_SUBHEADER,
    ST_TYPE,
    ST_DATE,
    ST_TIME,
    ST_TITLE,
    ST_TOPIC,
    ST_LINK,
    ST_CRITERIA,
    
    // PROJECT TAB
    PT_PROJECTS_HEADER,
    PT_TEAMS_SUBHEADER,
    PT_PROJECT_TABLE_HEADERS,
    PT_ADD_EDIT_SUBHEADER,
    PT_NAME,
    PT_COLOR,
    PT_TEXT_COLOR,
    PT_LINK,
    PT_STUDENTS_SUBHEADER,
    PT_STUDENTS_TABLE_HEADERS,
    PT_FIRST_NAME,
    PT_LAST_NAME,
    PT_TEAM,
    PT_ROLE
    
    
    
    
}