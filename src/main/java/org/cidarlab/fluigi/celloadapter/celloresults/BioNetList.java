/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.fluigi.celloadapter.celloresults;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;

/**
 *
 * @author krishna
 */
public class BioNetList extends Result{

    /**
     *
     * @param jobidString the value of jobidString
     * @param filenameString the value of filenameString
     */
    public BioNetList(String jobidString, String filenameString) throws UnirestException, IOException {
        super(jobidString, filenameString);
        downloadFile();
        parseFile();
    }
    
    

        
}
