/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.affinity.samplerestapp.endpoints;

import com.affinity.samplerestapp.model.DummyLargeTable;
import com.affinity.samplerestapp.model.ResponseWrapper;
import com.affinity.samplerestapp.rxjavasol.DummyDataProducer;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.server.ChunkedOutput;

/**
 *
 * @author Najeeb
 */
@Path("chunkedResponse")
public class AsyncResourceForChunkedResponse {
    
    private static final int CHUNKSIZE = 100;

    public AsyncResourceForChunkedResponse() {
    }
    
    @EJB private DummyDataProducer producer;
    
    @GET
    public ChunkedOutput<String> getChunkedStringResponse(){
        final ChunkedOutput<String> output = new ChunkedOutput<String>(String.class);
        new Thread(()->{
            String chunk="";
            try{
                long startTime = System.currentTimeMillis();
                while((chunk = getStringChunk(startTime))!=null){
                    output.write(chunk);
                    output.write("\n<br />");
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                try {
                    output.close();
                } catch (IOException ex) {
                    Logger.getLogger(AsyncResourceForChunkedResponse.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
        return output;
    }
    
    private String getStringChunk(long startTime){
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
    
    @GET
    @Path("real")
    @Produces("application/json")
    public ChunkedOutput<Response> getChunkedOutput(@QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("end") @DefaultValue("1000") int end){
        final ChunkedOutput<Response> output = new ChunkedOutput<>(Response.class);
        
        new Thread(()->{
            try{
                Response r = null;
                rangeList(start,end).stream().forEachOrdered(range->{
                    try {
                        output.write(getChunkedResponse(range));
                    } catch (IOException ex) {
                        Logger.getLogger(AsyncResourceForChunkedResponse.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                try {
                    output.close();
                } catch (IOException ex) {
                    Logger.getLogger(AsyncResourceForChunkedResponse.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
        
        return output;
    }
    
    @GET
    @Path("real2")
    @Produces("application/json")
    public ChunkedOutput<ResponseWrapper> getChunkedOutput2(@QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("end") @DefaultValue("1000") int end){
        final ChunkedOutput<ResponseWrapper> output = new ChunkedOutput<>(ResponseWrapper.class);
        
        new Thread(()->{
            try{
                Response r = null;
                rangeList(start,end).stream().forEachOrdered(range->{
                    try {
                        output.write(getChunkedResponse2(range));
                    } catch (IOException ex) {
                        Logger.getLogger(AsyncResourceForChunkedResponse.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                try {
                    output.close();
                } catch (IOException ex) {
                    Logger.getLogger(AsyncResourceForChunkedResponse.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
        
        return output;
    }
    
    private Response getChunkedResponse(final int[] range){
        List<DummyLargeTable> records = producer.getChunkedData(range);
        GenericEntity<List<DummyLargeTable>> ge = new GenericEntity<List<DummyLargeTable>>(records){};
        return Response.ok(ge, MediaType.APPLICATION_JSON).header("chunked-size", records.size()).build();
    }
    
    private ResponseWrapper getChunkedResponse2(final int[] range){
        List<DummyLargeTable> records = producer.getChunkedData(range);
        ResponseWrapper wr = new ResponseWrapper();
        wr.setValueList(records);
        return wr;
    }
    
    
    
    private int[] getRange(int start, int end){
        return new int[]{start, end};
    }
    
    private int getChunksNumber(int end){
        return (int) Math.ceil((double)end/CHUNKSIZE);
    }
    
    private List<int[]> rangeList(int originalStart, int originalEnd){
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
}
