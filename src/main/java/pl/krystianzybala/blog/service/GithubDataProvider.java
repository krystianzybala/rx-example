package pl.krystianzybala.blog.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import pl.krystianzybala.blog.Information;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

public class GithubDataProvider {
    private static final String RX_JAVA_GITHUB_REPOSITORY = "https://api.github.com/repos/ReactiveX/RxJava/commits";

    public Flowable<Information> allCommitsForRxJava(){

        return Flowable
                .interval(10, TimeUnit.SECONDS)
                .flatMap(x -> Flowable.fromArray(getAllCommitsForRxJavaRepository()))
                .subscribeOn(Schedulers.io())
                ;
    }

    private Information[] getAllCommitsForRxJavaRepository() throws IOException {
        final URL rxJavaUrl = new URL(RX_JAVA_GITHUB_REPOSITORY);

        final URLConnection urlConnection = rxJavaUrl.openConnection();

        urlConnection.setDoInput(true);

        final InputStream inputStream = urlConnection.getInputStream();

        final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        try {
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            final String jsonResult = bufferedReader.readLine();

            final Information[] informationArray = this.normalizeFromJson(jsonResult);

            return informationArray;

        } finally {
            inputStream.close();
        }
    }

    private Information[] normalizeFromJson(String jsonResult) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(jsonResult, Information[].class);
    }
}
