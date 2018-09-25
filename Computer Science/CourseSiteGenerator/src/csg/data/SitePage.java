/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author KI BBUM
 */
public class SitePage {
    private boolean use;
    private final StringProperty title;
    private final StringProperty fileName;
    private final StringProperty script;
    
    public SitePage(boolean initUse, String initTitle, String initFileName, String initScript) {
        use = initUse;
        title = new SimpleStringProperty(initTitle);
        fileName = new SimpleStringProperty(initFileName);
        script = new SimpleStringProperty(initScript);
    }
    
    public boolean getUse(){
        return use;
    }
    public void setUse(boolean initUse){
        use = initUse;
    }
    
    public String getTitle() {
        return title.get();
    }
    
    public void setTitle(String initTitle) {
        title.set(initTitle);
    }
    
    public String getFileName() {
        return fileName.get();
    }
    
    public void setFileName(String initFileName) {
        fileName.set(initFileName);
    }
    
    public String getScript() {
        return script.get();
    }
    
    public void setScript(String initScript) {
        script.set(initScript);
    }
    
}
