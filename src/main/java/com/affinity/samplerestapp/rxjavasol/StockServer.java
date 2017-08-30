/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.affinity.samplerestapp.rxjavasol;

import rx.Observable;
import java.util.List;

/**
 *
 * @author Najeeb
 */
public class StockServer {

    public static Observable<StockInfo> getFeed(List<String> symbols) {
        return Observable.create(subscriber -> {
            while (!subscriber.isUnsubscribed()) {
                symbols.stream()
                        .map(StockInfo::fetch)
                        .forEach(subscriber::onNext);

                sleep(1000);
            }
        });
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ex) {
        }
    }
}
