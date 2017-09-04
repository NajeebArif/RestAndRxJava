/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.affinity.samplerestapp.endpoints;

import com.affinity.samplerestapp.model.DummyLargeTable;
import com.affinity.samplerestapp.model.ResponseWrapper;
import com.affinity.samplerestapp.rxjavasol.DummyDataProducer;
import com.affinity.samplerestapp.utilites.GenericUtilities;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
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
@RequestScoped
public class AsyncResourceForChunkedResponse {

    private static final Logger LOG = Logger.getLogger(AsyncResourceForChunkedResponse.class.getName());

    public AsyncResourceForChunkedResponse() {
    }

    @EJB
    private DummyDataProducer producer;

    @GET
    public ChunkedOutput<String> getChunkedStringResponse() {
        final ChunkedOutput<String> output = new ChunkedOutput<String>(String.class);
        new Thread(() -> {
            String chunk = "";
            try {
                long startTime = System.currentTimeMillis();
                while ((chunk = GenericUtilities.getStringChunk(startTime)) != null) {
                    output.write(chunk);
                    output.write("\n<br />");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
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
    @Path("real")
    @Produces("application/json")
    public ChunkedOutput<Response> getChunkedOutput(@QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("end") @DefaultValue("1000") int end) {
        final ChunkedOutput<Response> output = new ChunkedOutput<>(Response.class);

        new Thread(() -> {
            try {
                Response r = null;
                GenericUtilities.rangeList(start, end).stream().forEachOrdered(range -> {
                    try {
                        output.write(getChunkedResponse(range));
                    } catch (IOException ex) {
                        Logger.getLogger(AsyncResourceForChunkedResponse.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
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
            @QueryParam("end") @DefaultValue("1000") int end) {
        final ChunkedOutput<ResponseWrapper> output = new ChunkedOutput<>(ResponseWrapper.class);
        new Thread(() -> {
            try {
                Response r = null;
                GenericUtilities.rangeList(start, end).stream().forEach(range -> {
                    try {
                        ResponseWrapper response = getChunkedResponse2(range);
                        response.updateSize();
                        output.write(response);
                    } catch (IOException ex) {
                        Logger.getLogger(AsyncResourceForChunkedResponse.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
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
    @Path("properJsonArray")
    public ChunkedOutput<String> getChunkedOutputAsString(@QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("end") @DefaultValue("1000") int end) {
        final ChunkedOutput<String> output = new ChunkedOutput<>(ResponseWrapper.class);
        
        new Thread(() -> {
            try {
                Response r = null;
                output.write("[");
                int[] index={0};
                GenericUtilities.rangeList(start, end).stream().forEach(range -> {
                    ResponseWrapper response = getChunkedResponse2(range);
                    response.getValueList().stream().forEach(d -> {
                        try {
                            if(index[0]!=0)
                                output.write(",");
                            else
                                index[0]++;
                            output.write(d.getJsonRep());
                        } catch (IOException ex) {
                            Logger.getLogger(AsyncResourceForChunkedResponse.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });

                });
                output.write("]");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    output.close();
                } catch (IOException ex) {
                    Logger.getLogger(AsyncResourceForChunkedResponse.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();

        return output;
    }

    private Response getChunkedResponse(final int[] range) {
        List<DummyLargeTable> records = producer.getChunkedData(range);
        GenericEntity<List<DummyLargeTable>> ge = new GenericEntity<List<DummyLargeTable>>(records) {
        };
        return Response.ok(ge, MediaType.APPLICATION_JSON).header("chunked-size", records.size()).build();
    }

    private ResponseWrapper getChunkedResponse2(final int[] range) {
        List<DummyLargeTable> records = producer.getChunkedData(range);
        ResponseWrapper wr = new ResponseWrapper();
        wr.setValueList(records);
        return wr;
    }

    @PostConstruct
    private void init() {
        LOG.info("com.affinity.samplerestapp.endpoints.AsyncResourceForChunkedResponse class initiated.");
    }

    @PreDestroy
    private void cleanup() {
        LOG.info("com.affinity.samplerestapp.endpoints.AsyncResourceForChunkedResponse class destroyed.");
    }
    
    private boolean isCommaUsed(){
        return false;
    }
}
