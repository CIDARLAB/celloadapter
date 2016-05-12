/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.fluigi.celloadapter;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.cidarlab.fluigi.celloadapter.results.BioNetList;
import org.cidarlab.fluigi.celloadapter.results.ResponseFunction;

/**
 *
 * @author krishna
 */
public class FluigiResults {
    
    private BioNetList gateNetList;
    
    HashMap<String, ResponseFunction> gateResponses;
    
    ArrayList<String> filesList;
    
    String jobID;
    
    
    
    public FluigiResults() {
        gateResponses = new HashMap<>();
    }

    /**
     *
     * @param jobidString
     * @param fileslist
     * @throws UnirestException
     * @throws IOException
     */
    public FluigiResults(String jobidString, ArrayList<String> fileslist) throws UnirestException, IOException {
        this();
        // Look for the following files and then use them to instantiate
        // the respective responses.
        filesList = fileslist;
        jobID = jobidString;
        
        //1. Get the Netlist file
        String netlistfile = findFile("(.*)_bionetlist.txt");
        gateNetList = new BioNetList(jobID, netlistfile);
        
        
        //2. Get the Response Functions
        ArrayList<String> responsefunctionfiles = findFiles("(.*)_xfer_model_(.*).gp");
        ResponseFunction responseFunction;
        for(String s :  responsefunctionfiles){
            responseFunction = new ResponseFunction(jobID, s);
            gateResponses.put(responseFunction.getName(), responseFunction);
        }
    }

    private String findFile(String pattern) {
        for(String s : filesList){
            if(s.matches(pattern)){
                return s;
            }
        }
        return null;
    }

    private ArrayList<String> findFiles(String pattern) {
        ArrayList<String> retList = new ArrayList<>();
        for(String s : filesList){
            if(s.matches(pattern)){
               retList.add(s);
            }
        }
        return retList;
    }

    public BioNetList getGateNetList() {
        return gateNetList;
    }
}
