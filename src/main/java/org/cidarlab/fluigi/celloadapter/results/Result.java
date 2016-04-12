/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.fluigi.celloadapter.results;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.cidarlab.fluigi.celloadapter.Cello;
import org.cidarlab.fluigi.celloadapter.CelloEndPoints;
import static org.cidarlab.fluigi.celloadapter.CelloEndPoints.SEP;

/**
 *
 * @author krishna
 */
public class Result {
    String fileName;
    String jobID;
    protected String DOWNLOAD_DIRECTORY = "./cello_data";
    
    /**
     *
     * @param jobIDString the value of jobIDString
     * @param filenameString the value of filenameString
     */
    public Result(String jobIDString, String filenameString){
        jobID = jobIDString;
        fileName = filenameString;
    }

    protected void downloadFile() throws UnirestException, FileNotFoundException, IOException {
        String url = CelloEndPoints.getResultFileURL(jobID, fileName);
        HttpResponse<InputStream> response = Unirest.get(url).basicAuth(Cello.username, Cello.password).asBinary();
        saveInputStreamToFile(response.getBody());
        
    }
    
    protected void saveInputStreamToFile(InputStream inputStream) throws FileNotFoundException, IOException{
        String directorypath = DOWNLOAD_DIRECTORY + SEP + jobID;
        File fo = new File(directorypath);
        if(!fo.exists()){
            fo.mkdirs();
        }
        
        fo = new File(directorypath + SEP + fileName);
        if(fo.exists()){
            fo.delete();
            fo.createNewFile();
        } else {
            fo.createNewFile();
        }
        
        OutputStream out = new FileOutputStream(fo);
        byte[] buf = new byte[1024];
        int len;
        while((len=inputStream.read(buf))>0){
            out.write(buf,0,len);
        }
        out.close();
        inputStream.close();
        
    }  
    
    protected void parseFile(){
        
    }
}
