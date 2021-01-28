package io.barogo.push.test;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

public class Test01 {

  public static void main(String[] args) {


//    Flux<Integer> seq = Flux.just(1,2,3);
//
////    seq.subscribe( value -> System.out.println("데이터 : " + value) );
//
//    seq.subscribe(new Subscriber<Integer>() {
//
//      private Subscription subscription;
//
//      @Override
//      public void onSubscribe(Subscription s) {
//        System.out.println("Subscriber.onSubscribe");
//        this.subscription = s;
//        this.subscription.request(1); // Publisher에 데이터 요청
//      }
//
//      @Override
//      public void onNext(Integer integer) {
//        System.out.println("Subscriber.onNext: " + integer);
//
//        this.subscription.request(1); // Publisher에 데이터 요청
//      }
//
//      @Override
//      public void onError(Throwable t) {
//        System.out.println("Subscriber.onError: " + t.getMessage());
//      }
//
//      @Override
//      public void onComplete() {
//        System.out.println("Subscriber.onComplete");
//      }
//    });
  }
}
