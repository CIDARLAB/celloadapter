/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.fluigi.celloadapter;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author krishna
 */
public class celloadapter {
    public static void main(String[] args) {
        Cello cello = new Cello("fluigi","potter");
        try {
            cello.getJobsList();
            FluigiResults fluigiResults = cello.getFluigiResults("test1");
        } catch (UnirestException ex) {
            Logger.getLogger(celloadapter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(celloadapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        
        
    }
}
