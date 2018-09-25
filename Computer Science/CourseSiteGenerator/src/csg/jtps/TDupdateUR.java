/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.jtps;

import csg.CourseSiteGeneratorApp;
import csg.data.CSGData;
import csg.data.TAData;
import csg.data.TeachingAssistant;
import csg.workspace.TAWorkspace;
import csg.workspace.Workspace;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import jtps.jTPS_Transaction;

/**
 *
 * @author KI BBUM
 */
public class TDupdateUR implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    TAWorkspace workspace;
    TAData data;
    TeachingAssistant selectedTA;
    boolean onlyEmail;
    
    ArrayList<StringProperty> containingCellProps;
    boolean origUndergrad;
    String origName;
    String origEmail;
    boolean newUndergrad;
    String newName;
    String newEmail;
    
    public TDupdateUR(CourseSiteGeneratorApp initApp, TAWorkspace initWorkspace, 
            TAData initData, TeachingAssistant initSelectedTA, boolean isOnlyEmail){
        app =  initApp;
        workspace = initWorkspace;
        data = initData;
        selectedTA = initSelectedTA;
        onlyEmail = isOnlyEmail;
        origUndergrad = selectedTA.getUndergrad().get();
        origName = selectedTA.getName();
        origEmail = selectedTA.getEmail();
        
        newUndergrad = workspace.getUndergradCheckBox().isSelected();
        TextField nameTextField = workspace.getNameTextField();
        newName = nameTextField.getText();
        TextField emailTextField = workspace.getEmailTextField();
        newEmail = emailTextField.getText();
        
        
        if(!onlyEmail){
            HashMap<String, Label> labels = workspace.getOfficeHoursGridTACellLabels();
            containingCellProps = new ArrayList<StringProperty>();
            for (Label label : labels.values()) {
                String cellText = label.getText();
                String[] names = cellText.split("\n");
                boolean found = false;
                boolean empty = cellText.equals("");
                if (!empty){
                    for ( int i = 0; i < names.length; i++ ){
                        if (names[i].equals(origName)){
                            found = true;
                        }
                    }
                }
                if (found){
                    containingCellProps.add(label.textProperty());
                }
            }
        }
        
    }
    
    @Override
    public void doTransaction() {
        data.removeTA(origUndergrad, origName, origEmail);
        data.addTA(newUndergrad, newName, newEmail);
        if (!onlyEmail){
            for(StringProperty cellProp : containingCellProps){
                data.updateTAFromCellHelper(cellProp, origName, newName);
            }
        }
        TableView taTable = workspace.getTATable();
        taTable.getSelectionModel().select(data.getTA(newName));
    }

    @Override
    public void undoTransaction() {
        data.removeTA(newUndergrad, newName, newEmail);
        data.addTA(origUndergrad, origName, origEmail);
        if (!onlyEmail){
            for(StringProperty cellProp : containingCellProps){
                data.updateTAFromCellHelper(cellProp, newName, origName);
            }
        }
        TableView taTable = workspace.getTATable();
        taTable.getSelectionModel().select(data.getTA(origName));
    }
    
    
    
}
