package pl.krystianzybala.blog.service;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RedisServer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String get(long key) throws InterruptedException {
        logger.info("Load data by key: {}", key);
        Thread.sleep(1000);
        return "Data: <" + key + ">";
    }

    public Flowable<String> rxGet(long key) {

        return Flowable
                .fromCallable(() -> get(key))
                .subscribeOn(Schedulers.computation());
    }
}


