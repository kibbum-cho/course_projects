/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.jtps;

import csg.data.TAData;
import jtps.jTPS_Transaction;

/**
 *
 * @author KI BBUM
 */
public class TDtoggleUR implements jTPS_Transaction{
    
    private String taName;
    private String cellKey;
    private TAData data;
    
    public TDtoggleUR(String cellKey, String taName, TAData data){
        this.taName = taName;
        this.cellKey = cellKey;
        this.data = data;
    }
    
    
    @Override
    public void doTransaction() {
        data.toggleTAOfficeHours(cellKey, taName);
    }

    @Override
    public void undoTransaction() {
        data.toggleTAOfficeHours(cellKey, taName);
    }
    
}
