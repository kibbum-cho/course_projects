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
public class TeachingAssistant<E extends Comparable<E>> implements Comparable<E> {
    // THE TABLE WILL STORE TA NAMES AND EMAILS
    //############## add email property
    private BooleanProperty undergrad;
    private final StringProperty name;
    private final StringProperty email;

    /**
     * Constructor initializes the TA name
     */
    //############# add initEmail argument
    public TeachingAssistant(boolean initUndergrad, String initName, String initEmail) {
        undergrad = new SimpleBooleanProperty(initUndergrad);
        name = new SimpleStringProperty(initName);
        email = new SimpleStringProperty(initEmail);
    }

    // ACCESSORS AND MUTATORS FOR THE PROPERTIES
    //############### add methods for email
    public BooleanProperty getUndergrad(){
        return undergrad;
    }
    
    public String getName() {
        return name.get();
    }
    
    public String getEmail() {
        return email.get();
    }
    
    public void setUndergrad(boolean initUndergrad){
        undergrad.set(initUndergrad);
    }
    
    public void setName(String initName) {
        name.set(initName);
    }
    
    public void setEmail(String initEmail) {
        email.set(initEmail);
    }

    @Override
    public int compareTo(E otherTA) {
        return getName().compareTo(((TeachingAssistant)otherTA).getName());
    }
    
    @Override
    public String toString() {
        return name.getValue();
    }
}
