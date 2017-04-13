package org.cidarlab.celloadapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by krishna on 3/17/17.
 */
public class Job {

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getJobID() {
        return jobID;
    }

    public class Input{

        private final String name;
        private double low;
        private double high;
        private String seq;

        public Input(String name, double low, double high, String seq){
            this.name = name;
            this.low = low;
            this.high = high;
            this.seq = seq;
        }

        public String getInputString(){
            return name + " " + low + " " + high + " " + seq;
        }
    }

    public class Output{

        private final String name;
        private final String seq;

        public Output(String name, String seq){
            this.name = name;
            this.seq = seq;
        }

        public  String getOutputString(){
            return name + " " + seq;
        }
    }

    public enum JobOptions{
        HILL_CLIMBING,
        BREADTH_FIRST,
        NO_PLASMID,
        NO_FIGURES
    }

    private String jobID;
    ArrayList<Input> inputs;
    ArrayList<Output> outputs;
    HashMap<String, String> user_flags;
    String verilog;

    public Job(){
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
        user_flags = new HashMap<>();
    }

    /**
     *Creates a Job instance with the given ID.
     * @param jobid
     */
    public Job(String jobid){
        this();
        this.jobID = jobid;
    }

    /**
     *Add an Input for the job
     * @param name
     * @param low
     * @param high
     * @param seq
     */
    public void addInput(String name, double low, double high, String seq){
        inputs.add(new Input(
                name,
                low,
                high,
                seq
        ));
    }

    /**
     *Add an Output for the job
     * @param name
     * @param seq
     */
    public void addOutput(String name, String seq){
        outputs.add(new Output(
                name,
                seq
        ));
    }

    /**
     * Sets the verilog that has to be processed in the job
     * @param verilog
     */
    public void setVerilog(String verilog){
        this.verilog = verilog;
    }

    /**
     * Adds a user flag to the job
     * @param options
     */
    public void addFlag(JobOptions options){
        switch (options){
            case BREADTH_FIRST:
                user_flags.put("-assignment_algorithm","breadth_first");
                break;
            case HILL_CLIMBING:
                user_flags.put("-assignment_algorithm","hill_climbing");
                break;
            case NO_FIGURES:
                user_flags.put("-figures", "false");
                break;
            case NO_PLASMID:
                user_flags.put("-plasmid", "false");
                break;
        }
    }

    /**
     * Returns the Request Fields
     * @return
     */
    public Map<String,Object> getRequestFields() {
        HashMap<String,Object> ret = new HashMap<>();
        ret.put("user_options", converFlagsToStrings());
        ret.put("inputs", convertInputsToStrings());
        ret.put("outputs", converOutputsToStrings());
        ret.put("verilog", verilog);
        ret.put("jobid", jobID);
        return ret;
    }

    /*
    Helper Methods
     */

    private String convertInputsToStrings() {
        String ret = "";
        for(Input i : this.inputs){
            ret+= i.getInputString() + "\n";
        }
        return ret;
    }

    private String converOutputsToStrings() {
        String ret = "";
        for(Output o : this.outputs){
            ret+= o.getOutputString() + "\n";
        }
        return ret;
    }

    private String converFlagsToStrings(){
        String ret ="";
        for(String key : user_flags.keySet()){
            ret+=key + " " + user_flags.get(key)+ " ";
        }
        return ret;
    }
}
