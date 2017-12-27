package pl.krystianzybala.rxexample;

import io.reactivex.Flowable;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.camel.rx.*;
import rx.Observable;

public class RxExampleTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void fileSystemChanges() {

        rxDir()
                .blockingSubscribe(this::print);;

        Assert.assertTrue(true);

    }

    @Test
    public void stream() throws Exception {
        this
                .rxHTTPStream()
                .map(exchange -> {
                    return exchange.getIn();
                })
                .map(Message::getBody)
                .toBlocking()
                .subscribe(this::print)
        ;
    }

    private Flowable<String> rxDir() {

        return Flowable
                .interval(1, TimeUnit.SECONDS)
                .flatMap(s-> Flowable.fromIterable(this.readDir()))
                .distinctUntilChanged()
                ;
    }

    private Observable<Exchange> rxHTTPStream() throws Exception {

        final DefaultCamelContext context = new DefaultCamelContext();

        return new ReactiveCamel(context).from("stream:url?url=http://127.0.0.1:8080/test");
    }

    private List<String> readDir() {
        final File file = new File("/var/logs/");
        final File[] files = file
                        .listFiles();
        final List<String> filesNamesList = Arrays
                .asList(files)
                .stream()
                .map(File::getName)
                .collect(Collectors.toList());


        return filesNamesList;
    }


    private void print(Object object) {
        System.out.println("INFO: " + object);
    }
}
