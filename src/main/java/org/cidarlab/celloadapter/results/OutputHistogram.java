package org.cidarlab.celloadapter.results;

import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.cidarlab.celloadapter.CelloEndPoints.SEP;

/**
 * Created by krishna on 3/20/17.
 */
public class OutputHistogram extends Result {

    HashMap<String, ArrayList<Float>> histogramdata;
    private String name;

    /**
     * @param jobIDString    the value of jobIDString
     * @param filenameString the value of filenameString
     */
    public OutputHistogram(String jobIDString, String filenameString) throws IOException, UnirestException {
        super(jobIDString, filenameString);
        this.name = filenameString;
        downloadFile();
        parseFile();
    }

    @Override
    protected void parseFile() {

        ArrayList<String> labels = new ArrayList<>();
        File filetoparse = new File(DOWNLOAD_DIRECTORY + SEP + jobID + SEP + fileName);

        // First figure out how many truth table outputs are there (scale + n)
        boolean firstline_flag = true;

        // Make as many Array Lists (n) for the output

        // Do the csv with tab separators and add them to

        try (BufferedReader br = new BufferedReader(new FileReader(filetoparse))) {
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    // Setup the data set by processing the first line.
                    if(firstline_flag){
                        // First figure out how many truth table outputs are there (scale + n)
                        //Create n +1 labels;
                        labels.add("Scale");
                        String[] subs = line.split("\t");
                        for(int count = 1 ; count<subs.length ; count++ ){
                            //Figure out the number of histograms are stored by going through this
                            //labels with the associated binary value will be stored.
                            labels.add(Integer.toBinaryString(count-1));
                            count++;
                        }
                        firstline_flag = false;
                        // Now this block will never get called
                        continue;
                    }

                    //Actually read the histogram data now
                    //Each row has data that has to go into the following labels: "Scale" "00" "01" "10" "11" ...
                    String[] rowdata = line.split("\t");
                    addtoArray(labels.get(0), rowdata[0]);
                    for(int rowindex =1; rowindex < rowdata.length ; rowindex++ ){
                        addtoArray(labels.get(rowindex), rowdata[rowindex]);
                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addtoArray(String label, String rowdatum) {
        ArrayList list = histogramdata.get(label);
        list.add(Float.parseFloat(rowdatum));
    }


    public String getName() {
        return name;
    }
}
