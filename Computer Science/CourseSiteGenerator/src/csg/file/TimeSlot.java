/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.file;

import csg.data.TAData;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.property.StringProperty;

/**
 *
 * @author KI BBUM
 */
public class TimeSlot {
    private String day;
    private String time;
    private String name;
    
    public TimeSlot(String initDay, String initTime, String initName) {
        day = initDay;
        time = initTime;
        name = initName;
    }
    
    // ACCESSORS AND MUTATORS
    
    public String getDay() { return day; }
    public String getTime() { return time; }
    public String getName() { return name; }
    public String getKey() {
        return day + " " + time + " " + name;
    }    
    
    public void setDay(String initDay) {
        day = initDay;
    }
    public void setTime(String initTime) {
        time = initTime;
    }
    public void setName(String initName) {
        name = initName;
    }
    
    /**
     * This function builds a list of all the office hours stored in 
     * the grid excluding empty time slots. This helps us do our file
     * input since it reflects how office hours are stored in the 
     * JSON file.
     */
    public static ArrayList<TimeSlot> buildOfficeHoursList(TAData data) {
        // BUILD AND RETURN THIS LIST
        ArrayList<TimeSlot> officeHoursList = new ArrayList();
        ArrayList<String> gridHeaders = data.getGridHeaders();
        HashMap<String, StringProperty> officeHours = data.getOfficeHours();
        for (int row = 1; row < data.getNumRows(); row++) {
            for (int col = 2; col < 7; col++) {
                // WE ONLY WANT THE DATA, NOTE THE HEADERS
                String cellKey = data.getCellKey(col, row);
                StringProperty timeSlotProp = officeHours.get(cellKey);
                String timeSlotText = timeSlotProp.getValue();
                String[] taNames = timeSlotText.split("\n");
                //String day = gridHeaders.get(col);
                String day = new String("");
                if (col == 2)
                    day = "MONDAY";
                else if (col == 3)
                    day = "TUESDAY";
                else if (col == 4)
                    day = "WEDNESDAY";
                else if (col == 5)
                    day = "THURSDAY";
                else
                    day = "FRIDAY";
                String timeCellKey = data.getCellKey(0, row);
                String time = officeHours.get(timeCellKey).getValue().replace(":", "_");
                for (int i = 0; i < taNames.length; i++) {
                    String taName = taNames[i];
                    if (taName.length() > 0) {
                        // ADD A TIME SLOT FOR EACH TA FOR EACH GRID CELL
                        TimeSlot ts = new TimeSlot(day, time, taName);
                        officeHoursList.add(ts);
                    }
                }
            }
        }
        return officeHoursList;
    }
}
