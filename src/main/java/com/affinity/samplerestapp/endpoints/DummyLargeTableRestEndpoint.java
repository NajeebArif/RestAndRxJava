/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.affinity.samplerestapp.endpoints;

import com.affinity.samplerestapp.model.DummyLargeTable;
import com.affinity.samplerestapp.rxjavasol.DummyDataProducer;
import com.affinity.samplerestapp.dao.DummyLargeTableFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;
import javax.ws.rs.container.ConnectionCallback;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import rx.Observable;
import rx.Subscriber;

/**
 *
 * @author Najeeb
 */
@Path("hugeVolume")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class DummyLargeTableRestEndpoint {

    @EJB
    private DummyLargeTableFacade facade;

    @EJB
    private DummyDataProducer producer;

    public DummyLargeTableRestEndpoint() {
    }

    @GET
    public Response getDummyLargeTableData(@QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("end") @DefaultValue("30") int end,
            @QueryParam("queryAllData") @DefaultValue("false") boolean queryAllData) {

        List<DummyLargeTable> allData = new ArrayList<>();

        if (!queryAllData) {
            allData = facade.findRange(new int[]{start, end});
        } else {
            allData = facade.findAll();
        }
        GenericEntity<List<DummyLargeTable>> ge = new GenericEntity<List<DummyLargeTable>>(allData) {
        };
        return Response.ok(ge).build();
    }

    @GET
    @Path("{id}")
    public Response getSingleRecordForId(@PathParam("id") long id) {
        DummyLargeTable data = facade.find(id);
        return Response.ok(data).build();
    }

    @GET
    @Path("singleResult/{username}")
    public Response getSingleRecordForUsername(@PathParam("username") String userName) {
        DummyLargeTable value = facade.getValueForUsername(userName);
        return Response.ok(value).build();
    }

    @GET
    @Path("reactive")
    public Response getReactiveData() {
        Observable<DummyLargeTable> data = producer.getObservable();

        data.subscribe(new Subscriber<DummyLargeTable>() {
            @Override
            public void onCompleted() {
                System.out.println("Feed has ended.");
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(DummyLargeTable d) {
                System.out.println(d);
                if (Math.random() > 0.9) {
                    System.out.println("Client wants to stop...");
                    unsubscribe();
                }
            }
        });
        return null;
    }

    @Resource
    ManagedExecutorService managedExecutorService;

    @GET
    @Path("async")
    public void asyncRestMethod(@Suspended final AsyncResponse asyncResponse) {

        final Observable<DummyLargeTable> data = producer.getObservable();

        asyncResponse.register((CompletionCallback) (Throwable throwable) -> {
            if (throwable == null) {
                System.out.println("RESPONSE SENT.");
            } else {
                //An error has occurred during request processing
            }
        }, (ConnectionCallback) (AsyncResponse disconnected) -> {
            data.subscribe().unsubscribe();
            System.out.println(data.subscribe().isUnsubscribed());
        });
        managedExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                List<DummyLargeTable> allData = new ArrayList<>();
                data.subscribe(new Subscriber<DummyLargeTable>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("Feed has ended.");
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(DummyLargeTable d) {
                        System.out.println(d);
                        allData.add(d);
                        if (Math.random() > 0.9) {
                            System.out.println("Client wants to stop...");
                            unsubscribe();
                        }
                    }
                });
                GenericEntity<List<DummyLargeTable>> ge = new GenericEntity<List<DummyLargeTable>>(allData) {
                };
                asyncResponse.resume(ge);
            }

        });
    }

}
