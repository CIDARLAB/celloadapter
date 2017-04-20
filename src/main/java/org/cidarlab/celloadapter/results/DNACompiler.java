/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.celloadapter.results;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.cidarlab.celloadapter.CelloEndPoints.SEP;

/**
 *
 * @author krishna
 */
public class DNACompiler extends Result {

    final String INPUT_PATTERN   =   "INPUT\\s+(?<vector>[01]+)\\s+(?<name>\\w+)\\s+[0-9]+\\s+[0-9|.]+";
    final String OUTPUT_PATTERN   =   "OUTPUT\\s+(?<vector>[01]+)\\s+(?<name>\\w+)\\s+[0-9]+\\s+\\([0-9|,]+\\)\\s+[0-9|.]+\\s+tox:[0-9|.]+";



    public OutputVector ouputVector;
    public List<InputVector> inputVectors;

    /**
     *
     * @param jobidString the value of jobidString
     * @param filenameString the value of filenameString
     * <p>
     * @throws UnirestException
     * @throws IOException
     */
    public DNACompiler(String jobidString, String filenameString) throws UnirestException, IOException {
        super(jobidString, filenameString);
        inputVectors = new ArrayList<>();
        downloadFile();
        parseFile();

    }



    @Override
    protected void parseFile() {
        File fo = new File(DOWNLOAD_DIRECTORY + SEP + jobID + SEP + fileName);
        try {
            String str = FileUtils.readFileToString(fo);


            //Extract Output
            //TODO: make this work for multiple inputs at some point
            Pattern regex = Pattern.compile(OUTPUT_PATTERN);
            Matcher matcher = regex.matcher(str);
            boolean sucess = matcher.find();
            if(sucess){
                String vector = matcher.group("vector");
                String name = matcher.group("name");
                ouputVector = new OutputVector(name, vector);
            }

            //Extract Inputs
            regex = Pattern.compile(INPUT_PATTERN);
            matcher = regex.matcher(str);
            while(matcher.find()){
                String vector = matcher.group("vector");
                String name = matcher.group("name");
                inputVectors.add(new InputVector(name, vector));
            }

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class InputVector{
        public String name;

        public String vector;

        public InputVector(String name, String vector) {
            this.name = name;
            this.vector = vector;
        }
    }

    public class OutputVector{
        public String name;
        public String vector;
        float tox;

        public OutputVector(String name, String vector) {
            this.name = name;
            this.vector = vector;
        }
    }

}
