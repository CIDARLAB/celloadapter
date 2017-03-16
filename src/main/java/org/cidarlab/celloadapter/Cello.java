/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.celloadapter;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.JSONParser;
import static org.cidarlab.celloadapter.CelloEndPoints.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;


/**
 *
 * @author krishna
 */
public class Cello {
    public static String username;
    public static String password;
    HttpResponse<JsonNode> response;
    
    public Cello(){
        //Create Cello Output Directory
    }
    
    public Cello(String usernameString, String passwordString){
        this();
        
        username = usernameString;
        password = passwordString;
        
    }
    
    public ArrayList<String> getJobsList() throws UnirestException{
        ArrayList<String> ret = new ArrayList<>();
        
        String requestURL = RESULTS;
        response = Unirest.get(requestURL).basicAuth(username, password).asJson();
        
        
        String responsestring = response.getBody().toString();
        
        JSONObject object = new JSONObject();
        try {
            object = (JSONObject)(new JSONParser().parse(responsestring));
        } catch (ParseException ex) {
            Logger.getLogger(Cello.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
        }
        
        JSONArray jSONArray = (JSONArray) object.get("folders");
        Iterator iter = jSONArray.iterator();
        
        while (iter.hasNext()) {
            String s = (String) iter.next();
            if(!s.equals(""))
                ret.add(s);
            
        }
            
        
        return ret;
    }
    
    public FluigiResults getFluigiResults(String jobID) throws UnirestException, IOException{
        String requestURL = RESULTS + SEP + jobID;
        try {
            response = Unirest.get(requestURL).basicAuth(username, password).asJson();
        } catch (UnirestException ex) {
            Logger.getLogger(Cello.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String responsestring = response.getBody().toString();
        
        JSONObject object = new JSONObject();
        try {
            object = (JSONObject)(new JSONParser().parse(responsestring));
        } catch (ParseException ex) {
            Logger.getLogger(Cello.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
        }
        
        JSONArray jSONArray = (JSONArray) object.get("files");
        Iterator iter = jSONArray.iterator();
        ArrayList<String> fileList = new ArrayList<>();
        while (iter.hasNext()) {
            fileList.add((String) iter.next());
        }
        
        FluigiResults fluigiResults = new FluigiResults(jobID, fileList);
        
        return fluigiResults;
    }
    
}
