/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.fluigi.celloadapter.results;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import static org.cidarlab.fluigi.celloadapter.CelloEndPoints.SEP;

/**
 *
 * @author krishna
 */
public class ResponseFunction extends Result {
    final String NAME_PATTERN   =   "title\\s'(?<name>\\w+)'";
    final String YMIN_PATTERN   =   "ymin\\s*=\\s*(?<ymin>[-+]?[0-9]*\\.?[0-9]+.*)";
    final String YMAX_PATTERN   =   "ymax\\s*=\\s*(?<ymax>[-+]?[0-9]*\\.?[0-9]+.*)";
    final String K_PATTERN      =   "K\\s*=\\s*(?<K>[-+]?[0-9]*\\.?[0-9]+.*)";
    final String N_PATTERN      =   "n\\s*=\\s*(?<n>[-+]?[0-9]*\\.?[0-9]+.*)";
    
    String name;
    double ymin;
    double ymax;
    double x;
    double K;
    double n;

    /**
     *
     * @param jobidString the value of jobidString
     * @param filenameString the value of filenameString
     * <p>
     * @throws UnirestException
     * @throws IOException
     */
    public ResponseFunction(String jobidString, String filenameString) throws UnirestException, IOException {
        super(jobidString, filenameString);
        downloadFile();
        parseFile();
    }

    public String getName() {
        return name;
    }

    @Override
    protected void parseFile() {
        File fo = new File(DOWNLOAD_DIRECTORY + SEP + jobID + SEP + fileName);
        try {
            String str = FileUtils.readFileToString(fo);
            
            //Extract name
            Pattern regex = Pattern.compile(NAME_PATTERN);
            Matcher matcher = regex.matcher(str);
            boolean sucess = matcher.find();
            name = sucess ? matcher.group("name") : null;
            
            //Extract ymin
            regex = Pattern.compile(YMIN_PATTERN);
            matcher = regex.matcher(str);
            sucess = matcher.find();
            ymin = Double.parseDouble(sucess ? matcher.group("ymin") : null);
            
            //Extract ymax
            regex = Pattern.compile(YMAX_PATTERN);
            matcher = regex.matcher(str);
            sucess = matcher.find();
            ymax = Double.parseDouble(sucess ? matcher.group("ymax") : null);
            
            //Extract K
            regex = Pattern.compile(K_PATTERN);
            matcher = regex.matcher(str);
            sucess = matcher.find();
            K = Double.parseDouble(sucess ? matcher.group("K") : null);
            
            //Extract n
            regex = Pattern.compile(N_PATTERN);
            matcher = regex.matcher(str);
            sucess = matcher.find();
            n = Double.parseDouble(sucess ? matcher.group("n") : null);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
