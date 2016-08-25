package itrx.alearn;

import org.junit.Test;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by tom.t on 11/08/16.
 */
public class ObserveOnAndSubscribeOn {
    @Test
    public void oneThread() {
        Observable<String> source = Observable.just("Alpha","Beta","Gamma");

        Observable<Integer> lengths = source.map(String::length);

        lengths.subscribe(l -> System.out.println("Received " + l +
                " on thread " + Thread.currentThread().getName()));
    }

    @Test
    public void oneThreadSubscribeOn() {
        Observable<String> source = Observable.just("Alpha", "Beta", "Gamma");
        Observable<String> sourceTwo = Observable.just("1", "2", "3");

        Observable<Integer> lengths = source
                .doOnNext(l -> {
                    sourceTwo.map(String::length)
                            .onErrorReturn(e -> {
                                System.out.println("Skip Error");
                                return null;
                            })
                            .subscribe(sum -> System.out.println("2Received " + sum +
                            " on thread " + Thread.currentThread().getName()));
                })
                .subscribeOn(Schedulers.computation())
                .map(String::length);

        lengths.subscribe(sum -> System.out.println("1Received " + sum +
                " on thread " + Thread.currentThread().getName()));

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void nestedSubscribeOn() {
        Observable<String> source = Observable.just("Alpha", "Beta", "Gamma");
        Observable<String> sourceTwo = Observable.just("1", "2", "3");

        Observable<Integer> lengths = source
                .doOnNext(l -> {
                    sourceTwo.map(String::length)
                            .onErrorReturn(e -> {
                                System.out.println("Skip Error");
                                return null;
                            })
                            .subscribeOn(Schedulers.io())
                            .subscribe(sum -> System.out.println("2Received " + sum +
                                    " on thread " + Thread.currentThread().getName()));
                })
                .subscribeOn(Schedulers.io())
                .map(String::length);

        lengths.subscribe(sum -> System.out.println("1Received " + sum +
                " on thread " + Thread.currentThread().getName()));

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
