package org.cidarlab.celloadapter;


import com.sun.tools.javac.util.Context;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

/**
 * Created by krishna on 3/20/17.
 */
public class UCF {

    public class Keywords{
        public static final String GATE_COLLECTION_ID = "gate_parts";
        public static final String GATE_PART_NAME_KEY = "gate_name";
    }


    private final String filename;
    private String filetext;
    JSONArray ucfdatamodel;

    public UCF(String filename, String filetext) throws ParseException {
        this.filename = filename;
        this.filetext = filetext;
        JSONParser parser = new JSONParser();
        ucfdatamodel = (JSONArray) parser.parse(filetext);
    }

    public String getFilename() {
        return filename;
    }

    public String getFiletext() {
        return filetext;
    }


    public ArrayList<String> getGateNames(){
        JSONObject jsonObject;
        ArrayList<String> ret = new ArrayList<>();
        for(Object obj : ucfdatamodel){
            jsonObject = (JSONObject) obj;
            if(Keywords.GATE_COLLECTION_ID.equals((String)jsonObject.get("collection"))){
                ret.add((String)jsonObject.get(Keywords.GATE_PART_NAME_KEY));
            }
        }

        return ret;
    }
}
