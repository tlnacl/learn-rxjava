package itrx.alearn;

import org.junit.Test;
import rx.Observable;
import rx.subjects.AsyncSubject;

/**
 * Created by tom.t on 22/07/16.
 */
public class Multicasting {

    @Test
    public void correctPublish() {
        Observable<String> observable = Observable.just("Event")
                .map(s -> {
                    System.out.println("Expensive operation for " + s);
                    return s;
                })
                .publish()
                .autoConnect(2);


        observable.subscribe(s -> System.out.println("Sub1 got: " + s));
        observable.subscribe(s -> System.out.println("Sub2 got: " + s));


        // 2
    }

    @Test
    public void wrongPublish() {
        Observable<String> observable = Observable.just("Event")
                .publish()
                .autoConnect(2)
                .map(s -> {
                    System.out.println("Expensive operation for " + s);
                    return s;
                });

        observable.subscribe(s -> System.out.println("Sub1 got: " + s));
        observable.subscribe(s -> System.out.println("Sub2 got: " + s));


        // 2
    }
}
