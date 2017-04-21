/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.celloadapter.results;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import org.cidarlab.celloadapter.CelloEndPoints;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

/**
 *
 * @author krishna
 */
public class BioNetList extends Result {

    final String pattern = "\\s+";
    private SimpleDirectedGraph<String, DefaultEdge> directedGraph;
    List<String> gates;

    /**
     *
     * @param jobidString the value of jobidString
     * @param filenameString the value of filenameString
     */
    public BioNetList(String jobidString, String filenameString) throws UnirestException, IOException {
        super(jobidString, filenameString);
        directedGraph = new SimpleDirectedGraph<>(DefaultEdge.class);
        gates = new ArrayList<>();
        downloadFile();
        parseFile();
    }

    @Override
    protected void parseFile() {
        File fo = new File(DOWNLOAD_DIRECTORY + CelloEndPoints.SEP + jobID + CelloEndPoints.SEP + fileName);
        try {
            LineIterator iter = FileUtils.lineIterator(fo);
            while (iter.hasNext()) {
                String next = iter.next();
                String[] tokens = next.split(pattern);
                String token;
                //Add the edges
                //Find out the number of connecting nodes 
                for (int i = 0; i < tokens.length; i++) {
                    token = tokens[i];
                    token = cleanToken(token); // Clears out the ??_ and the p prefixes
                    //Add vertex if it isn't already present in the graph
                    if (!token.matches("\\d+")) {
                        if (!directedGraph.containsVertex(token)) {
                            directedGraph.addVertex(token);
                        }
                        if (i > 0) {
                            directedGraph.addEdge(cleanToken(tokens[i]), cleanToken(tokens[0]));
                        }
                    }

                }
            }


            //Make a gate list too
            //TODO: This is a shitty way to do things - Cello should have better output formats

            String filestring = FileUtils.readFileToString(fo);

            String GATE_REGEX = "\\w+_\\w+";

            Pattern regex = Pattern.compile(GATE_REGEX);
            Matcher matcher = regex.matcher(filestring);
            while(matcher.find()){
                gates.add(filestring.substring(matcher.start(),matcher.end()));

            }


        } catch (IOException ex) {
            Logger.getLogger(BioNetList.class.getName()).log(Level.SEVERE, null, ex);
        }



    }

    private String cleanToken(String token) {
        return token.replaceFirst("((\\w+_)|(p))", "");
    }

    public SimpleDirectedGraph<String, DefaultEdge> getDirectedGraph() {
        return directedGraph;
    }

    public List<String> getGates() {
        return gates;
    }
}
