package org.cidarlab.celloadapter;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.cidarlab.celloadapter.results.DNACompiler;
import org.cidarlab.celloadapter.results.OutputHistogram;
import org.cidarlab.celloadapter.results.ResponseFunction;
import org.cidarlab.celloadapter.results.BioNetList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by krishna on 3/16/17.
 */

public class GarudaResults extends Results{

    public BioNetList gateNetList;

    public HashMap<String, ResponseFunction> gateResponses;

    public HashMap<String, OutputHistogram> truth_histograms;

    public DNACompiler dnaCompilerOutput;

    public GarudaResults(){
        gateResponses = new HashMap<>();
        truth_histograms = new HashMap<>();

    }

    public GarudaResults(String jobidString, ArrayList<String> fileslist) throws UnirestException, IOException {
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

        //3. Get either the eugene or the design space files
        //TODO: Need to probably just give the file link or the text

        //4. Get the Truth Table Histograms
        ArrayList<String> histogramfiles = findFiles("(.*)_truth.txt");
        OutputHistogram histogram;
        for(String s : histogramfiles){
            histogram = new OutputHistogram(jobID,s);
            truth_histograms.put(histogram.getName(),histogram);
        }

        //5. Get the DNA  Compiler Output

        String compileroutputfile = findFile("(.*)_dnacompiler_output.txt");
        dnaCompilerOutput = new DNACompiler(jobID, compileroutputfile);
    }
}
