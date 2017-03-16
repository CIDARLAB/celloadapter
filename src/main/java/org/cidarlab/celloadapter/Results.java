package org.cidarlab.celloadapter;

import java.util.ArrayList;

/**
 * Created by krishna on 3/16/17.
 */
public class Results {

    protected ArrayList<String> filesList;
    protected String jobID;


    protected String findFile(String pattern) {
        for(String s : filesList){
            if(s.matches(pattern)){
                return s;
            }
        }
        return null;
    }

    protected ArrayList<String> findFiles(String pattern) {
        ArrayList<String> retList = new ArrayList<>();
        for(String s : filesList){
            if(s.matches(pattern)){
               retList.add(s);
            }
        }
        return retList;
    }
}
