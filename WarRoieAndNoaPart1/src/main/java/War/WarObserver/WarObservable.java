package War.WarObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class WarObservable {
    private final int MAX_NUM_OF_THREADS = 2;

    private List<WarObserver> subscribers = Collections.synchronizedList(new ArrayList<>());
    private ExecutorService executor = Executors.newFixedThreadPool(MAX_NUM_OF_THREADS);

    protected void publish(Function<WarObserver, Void> function){
        for(WarObserver subscriber : subscribers){
            executor.execute(() -> function.apply(subscriber));
        }
    }

    public void subscribe(WarObserver subscriber){
        subscribers.add(subscriber);
    }

    public void unsubscribe(WarObserver subscriber){
        subscribers.remove(subscriber);
    }
}
