/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.jtps;

import csg.CourseSiteGeneratorApp;
import csg.data.TAData;
import csg.data.TeachingAssistant;
import csg.workspace.TAWorkspace;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import jtps.jTPS_Transaction;

/**
 *
 * @author KI BBUM
 */
public class TDdeleteUR implements jTPS_Transaction{
    CourseSiteGeneratorApp app;
    TAWorkspace workspace;
    TAData data;
    boolean undergrad;
    String taName;
    String taEmail;
    ArrayList<StringProperty> containingCellProps;
    ArrayList<String> originalValues;
    
    public TDdeleteUR(CourseSiteGeneratorApp initApp,TAWorkspace initWorkspace, TAData initData){
        app = initApp;
        workspace = initWorkspace;
        data = initData;
        TableView taTable = workspace.getTATable();
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        TeachingAssistant ta = (TeachingAssistant)selectedItem;
        undergrad = workspace.getUndergradCheckBox().isSelected();
        taName = ta.getName();
        taEmail = ta.getEmail();
        HashMap<String, Label> labels = workspace.getOfficeHoursGridTACellLabels();
        HashMap<String, Pane> officeHoursGridTACellPanes =workspace.getOfficeHoursGridTACellPanes();
        
        containingCellProps = new ArrayList<StringProperty>();
        originalValues = new ArrayList();
        for (Label label : labels.values()) {
            String cellText = label.getText();
            String[] names = cellText.split("\n");
            boolean found = false;
            boolean empty = cellText.equals("");
            if (!empty){
                for ( int i = 0; i < names.length; i++ ){
                    if (names[i].equals(taName)){
                        found = true;
                    }
                }
            }
            if (found){
                containingCellProps.add(label.textProperty());
                originalValues.add(cellText);
            }
        }
    }
    
    
    @Override
    public void doTransaction() {
        data.removeTA(undergrad, taName, taEmail);
        for(StringProperty cellProp : containingCellProps){
            data.removeTAFromCell(cellProp, taName);
        }
    }

    @Override
    public void undoTransaction() {
        data.addTA(undergrad, taName, taEmail);
        int i = 0;
        for(StringProperty cellProp : containingCellProps){
            String originValue = (String)originalValues.toArray()[i];
            cellProp.setValue(originValue);
            i++;
        }
    }
    
}
