package com.backend.web_crawler.fetcher;

import com.backend.web_crawler.entity.FetchResponseEntity;
import com.backend.web_crawler.parser.HtmlParser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

@Service
public class HtmlFetcher {

    public final HttpClient httpClient;
    private final HtmlParser htmlParser;

    public HtmlFetcher() {
        this(
                HttpClient.newBuilder()
                        .version(HttpClient.Version.HTTP_2)
                        .connectTimeout(Duration.ofSeconds(10))
                        .followRedirects(HttpClient.Redirect.NORMAL)
                        .build(),
                new HtmlParser()
        );
    }

    public HtmlFetcher(HttpClient httpClient, HtmlParser htmlParser) {
        this.httpClient = httpClient;
        this.htmlParser = htmlParser;
    }

    public FetchResponseEntity fetchResponse(String url) {
        if (url == null || url.isBlank()) {
            return new FetchResponseEntity(url, false, 400, new String[0]);
        }

        try {
            URI uri = URI.create(url);
            if (uri.getScheme() == null || (!uri.getScheme().equalsIgnoreCase("http") && !uri.getScheme().equalsIgnoreCase("https"))) {
                return new FetchResponseEntity(url, false, 400, new String[0]);
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(Duration.ofSeconds(10))
                    .header("User-Agent", "Mozilla/5.0 (compatible; WebCrawlerBot/1.0)")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            boolean success = statusCode >= 200 && statusCode < 300;

            String[] extractedUrls = new String[0];
            if (success && response.body() != null && !response.body().isBlank()) {
                List<String> urls = htmlParser.extractUrls(response.body(), url);
                extractedUrls = urls.toArray(new String[0]);
            }

            return new FetchResponseEntity(url, success, statusCode, extractedUrls);

        } catch (IllegalArgumentException e) {
            return new FetchResponseEntity(url, false, 400, new String[0]);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new FetchResponseEntity(url, false, 500, new String[0]);
        } catch (IOException e) {
            return new FetchResponseEntity(url, false, 500, new String[0]);
        } catch (Exception e) {
            return new FetchResponseEntity(url, false, 500, new String[0]);
        }
    }
}
