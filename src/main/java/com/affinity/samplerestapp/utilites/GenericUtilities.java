/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.affinity.samplerestapp.utilites;

import com.affinity.samplerestapp.endpoints.AsyncResourceForChunkedResponse;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Najeeb
 */
public class GenericUtilities {

    private static final Logger LOG = Logger.getLogger(GenericUtilities.class.getName());
    private static final int CHUNKSIZE = 100;
    
    private static boolean isCommaUsed = false;

    public static void sleep(long i){
        try {
            Thread.sleep(i);
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
    public static String getStringChunk(long startTime){
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(AsyncResourceForChunkedResponse.class.getName()).log(Level.SEVERE, null, ex);
        }
        String value = "";
        long endTime = System.currentTimeMillis();
        if((endTime-startTime)/1000 > 5)
            return null;
        else
            return "Current Time is: "+ZonedDateTime.now();
    }
    
    
    public static int[] getRange(int start, int end){
        return new int[]{start, end};
    }
    
    public static int getChunksNumber(int end){
        return (int) Math.ceil((double)end/CHUNKSIZE);
    }
    
    public static List<int[]> rangeList(int originalStart, int originalEnd){
        int numberOfChunks = getChunksNumber(originalEnd);
        List<int[]> rangeList = new ArrayList<>();
        if(originalEnd<CHUNKSIZE){
            rangeList.add(getRange(originalStart, originalEnd));
            return rangeList;
        }
        System.out.println("com.affinity.samplerestapp.endpoints.AsyncResourceForChunkedResponse.rangeList()");
        for(int i = 1; i <= numberOfChunks; i++){
                int s = CHUNKSIZE*(i-1);
                int e = CHUNKSIZE*i;
                System.out.println("FOR ITERATION: #"+i);
                System.out.println("Start Value is: "+s+" and End Value is: "+e);
                rangeList.add(getRange(s, e));
        }
        return rangeList;
    }
    
    public static boolean isCommaUsed(){
        return isCommaUsed;
    }
    
    public static synchronized void commaUsed(boolean b){
        isCommaUsed = b;
    }
}
