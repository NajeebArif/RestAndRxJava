/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.affinity.samplerestapp.rxjavasol;

import com.affinity.samplerestapp.model.DummyLargeTable;
import com.affinity.samplerestapp.service.DummyLargeTableFacade;
import com.affinity.samplerestapp.utilites.GenericUtilities;
import java.util.List;
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
    
    public List<DummyLargeTable> getListInstance(int[] range){
        return facade.findRange(range);
    }
    
    public Observable<DummyLargeTable> getObservable(){
        return Observable.create(subscriber->{
            while(!subscriber.isUnsubscribed()){
                for(long i = 1; i<1500;i++){
                    DummyLargeTable d = getInstance(i);
                    subscriber.onNext(d);
                }
                GenericUtilities.sleep(1000);
            }
            subscriber.unsubscribe();
        });
    }
    
    public Observable<List<DummyLargeTable>> getListObservable(final int[] range){
        return Observable.create((subscriber) -> {
            while(!subscriber.isUnsubscribed()){
                List<DummyLargeTable> largeTables = getListInstance(range);
                subscriber.onNext(largeTables);
            }
            GenericUtilities.sleep(1000);
        });
    }
    
    public List<DummyLargeTable> getChunkedData(final int[] range){
        return facade.findRange(range);
    }
    
}
