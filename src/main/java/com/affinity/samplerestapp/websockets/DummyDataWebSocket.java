/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.affinity.samplerestapp.websockets;

import com.affinity.samplerestapp.model.DummyLargeTable;
import com.affinity.samplerestapp.rxjavasol.DummyDataProducer;
import com.affinity.samplerestapp.utilites.GenericUtilities;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import rx.Observable;

/**
 *
 * @author narif
 */
@ServerEndpoint("/dummyDataWebsocket")
//@Singleton
public class DummyDataWebSocket {
    
    @Inject @EJB  private DummyDataProducer producer;
    
    @OnOpen
    public void onOpen(){
        System.out.println("WEBSOCKET Opened.");
    }
    
    
    @OnMessage
    public String onMessage(Session session, String message){
        if(message.equals("Arif@oo7"))
            return "Hello Admin!!";
        if(message.equals("startObserving")){
            processData(session);
            return "You will get your data in a sec!!";
        }
        return "";
    }
    
    private void processData(Session session){
        if(producer==null){
            System.err.println("NULL PRODUCER CDI DID NOT WORK....");
            return;
        }
        List<Observable<DummyLargeTable>> observableList = GenericUtilities.rangeList(0, 50000).stream().map(range->{
            Observable<List<DummyLargeTable>>  obList = producer.getObservable(range);
            return obList.flatMap(Observable::from);
        }).collect(Collectors.toList());
        
        Observable<DummyLargeTable> ob = Observable.from(()->observableList.stream().iterator()).flatMap(x->x);
        
        ob.subscribe(
            d-> session.getAsyncRemote().sendText(d.getJsonRep()),
            (t)->System.err.println("Error Occured"),
            ()->System.out.println("TASK COMPLETED.")
        );
    }
    
}
