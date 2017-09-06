/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.affinity.samplerestapp.endpoints;

import com.affinity.samplerestapp.dao.DummyLargeTableFacade;
import com.affinity.samplerestapp.model.DummyLargeTable;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.glassfish.jersey.server.ChunkedOutput;

/**
 *
 * @author narif
 */
@Path("hugeDataSets")
@Stateless
public class SynchronousHugeDataEndpoint {

    public SynchronousHugeDataEndpoint() {
    }
    
    @EJB private DummyLargeTableFacade facade;
    
    @GET
    @Path("streamingOutput")
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public Response getAllTheStreamedResult(){
        StreamingOutput output = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                int maxRecordsPerFetch = 10000;
                int startIndexOfRecords = 0;
                int totalNumberOfRecords = facade.totalNumberOfRecords();
                try(PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(output)))){
                    writer.print("{\"items\": [");
                    while(totalNumberOfRecords>0){
                        List<DummyLargeTable> records = facade.getRecordsInRange(startIndexOfRecords, maxRecordsPerFetch);
                        for(DummyLargeTable d: records){
                            if(startIndexOfRecords>0)
                                writer.print(",");
                            writer.print(d.getJsonRep());
                            startIndexOfRecords++;
                        }
                        totalNumberOfRecords-=maxRecordsPerFetch;
                    }
                    writer.print("]}");
                }
            }
        };
        return getNoCacheResponseBuilder(Response.Status.OK).entity(output).build();
    }
    
    @GET
    @Path("inChunks")
    public ChunkedOutput<String> getTheChunkedOutput(){
        final ChunkedOutput<String> output = new ChunkedOutput<>(String.class);
        
        new Thread(()->{
            try {
                int maxRecordsPerFetch = 10000;
                int startIndexOfRecords = 0;
                int totalNumberOfRecords = facade.totalNumberOfRecords();
                output.write("{\"items\": [");
                while(totalNumberOfRecords>0){
                    System.out.println("Start Index : "+startIndexOfRecords);
                    List<DummyLargeTable> records = facade.getRecordsInRange(startIndexOfRecords, maxRecordsPerFetch);
                    System.out.println("Total Number of Records fetched: "+records.size());
                    for(DummyLargeTable d: records){
                        if(startIndexOfRecords>0)
                            output.write(",");
                        output.write(d.getJsonRep());
                        startIndexOfRecords++;
                    }
                    totalNumberOfRecords-=maxRecordsPerFetch;
                }
                output.write("]}");
                output.close();
            } catch (IOException ex) {
                Logger.getLogger(SynchronousHugeDataEndpoint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
        
        return output;
    }
    
    protected Response.ResponseBuilder getNoCacheResponseBuilder(Response.Status status){
        CacheControl cc = new CacheControl();
        cc.setNoCache(true);
        cc.setMaxAge(-1);
        cc.setMustRevalidate(true);
        return Response.status(status).cacheControl(cc);
    }
}
