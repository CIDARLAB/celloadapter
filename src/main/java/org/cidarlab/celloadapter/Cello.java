/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.celloadapter;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
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

    /**
     * Gets the result set
     * @param jobID
     * @return
     * @throws UnirestException
     * @throws IOException
     */
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

    public GarudaResults getGarudaResults(String jobID) throws UnirestException, IOException{
        String requestURL = RESULTS + SEP + jobID;

        response = Unirest.get(requestURL).basicAuth(username, password).asJson();

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

        GarudaResults garudaResults = new GarudaResults(jobID, fileList);

        return garudaResults;
    }

    /**
     * Submits a Job that uses the default UCF
     * @param job
     */
    public void submitJob(Job job) throws UnirestException {
        //response = Unirest.post(SUBMIT).basicAuth(username,password).fields(job.getRequestFields()).asJson();
        HttpResponse<JsonNode> response = Unirest.post(SUBMIT)
                //.header("accept", "application/json")
                .basicAuth(username,password).fields(job.getRequestFields())
                .asJson();

    }

    public void submitcurlJob(){
        /*
        curl -u "username:password" -X POST http://127.0.0.1:8080/submit --data-urlencode "id=demo001" --data-urlencode "verilog_text@demo_verilog.v" --data-urlencode "input_promoter_data@demo_inputs.txt" --data-urlencode "output_gene_data@demo_outputs.txt"
        */

        List<String> curlcommand = new ArrayList<>();
        curlcommand.add("curl");
        curlcommand.add("-u");

    }

    /**
     * Submits a Job with a specified UCF
     * @param job
     * @param ucf
     */
    public void submitJob(Job job, UCF ucf) throws UnirestException {
        /*
        TODO: Create the whole UCF and set it up
         */
        submitUCF(ucf);
        validateUCF(ucf);
        submitJob(job);
    }

    /**
     * Validates the UCF (Note, the UCF has to be uploaded first
     * @param ucf
     */
    public boolean validateUCF(UCF ucf) {
        //TODO: Add code to validate the UCF. For now it just return true.
        return true;
    }

    /**
     * Submits a UCF
     * @param ucf
     */
    public void submitUCF(UCF ucf) throws UnirestException {
        response = Unirest.post(UCF + SEP + ucf.getFilename()).basicAuth(username,password).asJson();

    }


    
}
