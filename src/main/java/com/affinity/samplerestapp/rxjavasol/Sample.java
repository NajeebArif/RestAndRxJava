/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.affinity.samplerestapp.rxjavasol;

import java.util.Arrays;
import java.util.List;
import rx.Observable;
import rx.Subscriber;

/**
 *
 * @author Najeeb
 */
public class Sample {
      public static void main(String[] args) {

    List<String> symbols = Arrays.asList("AMZN", "GOOG", "ORCL");

    Observable<StockInfo> feed = StockServer.getFeed(symbols);

    feed.subscribe(new Subscriber<StockInfo>() {
      @Override
      public void onCompleted() {
        System.out.println("Feed has ended.");
      }

      @Override
      public void onError(Throwable throwable) {

      }

      @Override
      public void onNext(StockInfo stockInfo) {
        System.out.println(stockInfo);
        if(Math.random() > 0.9) {
          System.out.println("Client wants to stop...");
          unsubscribe();
        }
      }
    });
  }

}
