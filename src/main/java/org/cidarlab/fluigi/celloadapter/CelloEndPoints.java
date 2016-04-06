/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.fluigi.celloadapter;

/**
 *
 * @author krishna
 */
public class CelloEndPoints {
    
    private static final String  SITE = "http://cellocad.org";
    
    public static final String SEP= "/";
    
    public static String IN_OUT     = SITE + SEP + "in_out";
    public static String NETSYNTH   = SITE + SEP + "netsynth";
    public static String SUBMIT     = SITE + SEP + "submit";
    public static String RESULTS    = SITE + SEP + "results";
    public static String UCF        = SITE + SEP + "ucf";
    
    public static String getResultFileURL(String jobIDString, String resultFileNameString){
        return RESULTS + SEP + jobIDString + SEP + resultFileNameString;
    }
    
}
