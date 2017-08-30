/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.affinity.samplerestapp.rxjavasol;

import com.affinity.samplerestapp.model.DummyLargeTable;
import com.affinity.samplerestapp.service.DummyLargeTableFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import rx.Observable;

/**
 *
 * @author Najeeb
 */
@Stateless
public class DummyDataProducer {
    
    @EJB
    private DummyLargeTableFacade facade;
    
    public DummyLargeTable getInstance(long id){
        return facade.find(id);
    }
    
    public Observable<DummyLargeTable> getObservable(){
        return Observable.create(subscriber->{
            while(!subscriber.isUnsubscribed()){
                for(long i = 1; i<150;i++){
                    DummyLargeTable d = getInstance(i);
                    subscriber.onNext(d);
                }
                sleep(1000);
            }
            subscriber.unsubscribe();
        });
    }

    private void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException ex) {
            Logger.getLogger(DummyDataProducer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
