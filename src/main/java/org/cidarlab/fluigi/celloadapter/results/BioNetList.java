/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.fluigi.celloadapter.results;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import static org.cidarlab.fluigi.celloadapter.CelloEndPoints.SEP;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

/**
 *
 * @author krishna
 */
public class BioNetList extends Result {

    final String pattern = "\\s+";
    SimpleDirectedGraph<String, DefaultEdge> directedGraph;

    /**
     *
     * @param jobidString the value of jobidString
     * @param filenameString the value of filenameString
     */
    public BioNetList(String jobidString, String filenameString) throws UnirestException, IOException {
        super(jobidString, filenameString);
        directedGraph = new SimpleDirectedGraph<>(DefaultEdge.class);
        downloadFile();
        parseFile();
    }

    @Override
    protected void parseFile() {
        File fo = new File(DOWNLOAD_DIRECTORY + SEP + jobID + SEP + fileName);
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
                    //Add vertex if it isn't already present in the graph
                    if (!token.matches("\\d+")) {
                        if (!directedGraph.containsVertex(token)) {
                            directedGraph.addVertex(token);
                        }
                        if (i > 0) {
                            directedGraph.addEdge(tokens[i], tokens[0]);
                        }
                    }

                }
            }

        } catch (IOException ex) {
            Logger.getLogger(BioNetList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
