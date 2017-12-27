package pl.krystianzybala.blog;


import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;

import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.krystianzybala.blog.commit.Commit;
import pl.krystianzybala.blog.service.GithubDataProvider;
import pl.krystianzybala.blog.service.RedisServer;


import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RxJavaIntroductionTest {
    private final Logger logger = LoggerFactory.getLogger(RxJavaIntroductionTest.class);

    @Test
    public void simpleCase() {
        final Observable<String> observable = Observable
                .just("Hello world!");


        Assert.assertEquals("Hello world!", observable.blockingFirst());
    }


    @Test
    public void notSubscribe() {

        List<Integer> list = new ArrayList<>();

        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);


        Observable
                .fromIterable(list)
                .filter(i -> i > 1)
                .map(i -> {
                    logger.info("Number: {}", i);
                    return i;
                })
        ;
    }


    @Test
    public void monitorDirectory() {

        rxReadFilesFromCurrentDirectory()
                .subscribe(this::print);

    }

    private Observable<File> rxReadFilesFromCurrentDirectory() {
        final File[] listFiles = readFilesInCurrentDirectory();

        return Observable
                .fromArray(listFiles);
    }

    private List<File> filesList() {
        final File[] files = new File(".").listFiles();

        final List<File> fileList = Arrays.asList(files);

        return fileList;
    }

    @Test
    public void rxReadFilesFromCurrentDirectoryWithInterval() throws InterruptedException {

        Observable
                .interval(1, TimeUnit.SECONDS)
                .map(x -> filesList())
                .subscribeOn(Schedulers.io())
                .subscribe(this::print);


        TimeUnit.SECONDS.sleep(5); // or  replace subscribe  to blockingSubscribe(this::print);

    }

    @Test
    public void rxReadFilesFromCurrentDirectoryWithIntervalDistinctResult() {

        final Observable<File> files = Observable
                .interval(1, TimeUnit.SECONDS)
                .flatMap(x -> Observable.fromIterable(filesList()))
                .distinct();

        files

                .map(File::getName)
                .blockingSubscribe(this::print);
    }

    private File[] readFilesInCurrentDirectory() {
        final File[] files = new File(".").listFiles();

        return files;
    }

    @Test
    public void subscribeOnOtherThread() throws InterruptedException {
        final RedisServer r1 = new RedisServer();
        final RedisServer r2 = new RedisServer();


        final Flowable<String> rr1 = r1.rxGet(1);
        final Flowable<String> rr2 = r2.rxGet(2);

        Flowable
                .merge(rr1, rr2)
                .firstElement()
                .subscribe(this::print);

        TimeUnit.SECONDS.sleep(3);
    }

    @Test
    public void handleVerySlowAPI() {

        final Observable<BigDecimal> response = verySlowApi()
                .timeout(1, TimeUnit.SECONDS)
                .doOnError(x -> logger.info("Error " + x))
                .retry(4)
                .onErrorReturn(x -> BigDecimal.ONE.negate());


        response
                .blockingSubscribe(this::print);
    }


    @Test
    public void handleVerySlowAPIWithTestCode() {

        final BigDecimal fallback = BigDecimal.ONE.negate();
        final TestScheduler testScheduler = new TestScheduler();

        final Observable<BigDecimal> response = verySlowApi()
                .timeout(1, TimeUnit.SECONDS, testScheduler)
                .doOnError(x -> logger.info("Error " + x)) // I use this for test, careful because you lose stacktrace, only for test!
                .retry(4)
                .onErrorReturn(x -> fallback);


        final TestObserver<BigDecimal> test = response.test();

        test.assertNoErrors();
        test.assertNoValues();


        testScheduler.advanceTimeBy(4999, TimeUnit.MILLISECONDS);


        test.assertNoErrors();
        test.assertNoValues();

        testScheduler.advanceTimeBy(1, TimeUnit.MILLISECONDS);


        test.assertNoErrors();
        test.assertValue(fallback);
    }


    private Observable<BigDecimal> verySlowApi() {
        final Observable<BigDecimal> response = Observable
                .timer(1, TimeUnit.MINUTES)
                .map(x -> BigDecimal.ONE);


        return response;
    }

    @Test
    public void handleVerySlowAPIWithTestCodeWithFlowable() {

        final BigDecimal fallback = BigDecimal.ONE.negate();
        final TestScheduler testScheduler = new TestScheduler();

        final Flowable<BigDecimal> response = verySlowApi()
                .timeout(1, TimeUnit.SECONDS, testScheduler)
                .doOnError(x -> logger.info("Error " + x)) // I use this for test, careful because you lose stacktrace, only for test!
                .retry(4)
                .onErrorReturn(x -> fallback)
                .toFlowable(BackpressureStrategy.BUFFER);


        final TestSubscriber<BigDecimal> test = response.test();

        test.assertNoErrors();
        test.assertNoValues();

        testScheduler.advanceTimeBy(4999, TimeUnit.MILLISECONDS);

        test.assertNoErrors();
        test.assertNoValues();

        testScheduler.advanceTimeBy(1, TimeUnit.MILLISECONDS);


        test.assertNoErrors();
        test.assertValue(fallback);
    }

    @Test
    public void readCommits() throws IOException {

        final Flowable<String> authorsLogin = this.loadAllCommitsForRxJava()
                .distinct()
                .map(Information::getAuthor)
                .map(Author::getLogin)
                .subscribeOn(Schedulers.io());


        final Flowable<Commit> commits = this.loadAllCommitsForRxJava()
                .distinct()
                .map(Information::getCommit)
                .subscribeOn(Schedulers.io());


        final Flowable<String> information = Flowable.zip(authorsLogin, commits, (a, c) -> {
            if (c.getAuthor().getName().equals(a)) {

                return "Author: " + a + " add commit: " + c.getComment_count();
            }

            return "";
        }).subscribeOn(Schedulers.computation());


        information
                .filter(a -> !a.isEmpty())
                .blockingSubscribe(this::print);
    }

    private Flowable<Information> loadAllCommitsForRxJava() throws IOException {

        final GithubDataProvider githubDataProvider = new GithubDataProvider();

        return githubDataProvider
                .allCommitsForRxJava()
                ;
    }

    /**
     * This method responsible for print log in console
     *
     * @param object will be print into console log
     */
    private void print(Object object) {
        logger.info("Got:  {}", object);
    }
}

