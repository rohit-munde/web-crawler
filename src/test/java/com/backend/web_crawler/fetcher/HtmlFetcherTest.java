package com.backend.web_crawler.fetcher;

import com.backend.web_crawler.entity.FetchResponseEntity;
import com.backend.web_crawler.parser.HtmlParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HtmlFetcherTest {

    private HtmlFetcher htmlFetcher;

    @BeforeEach
    void setUp() {
        htmlFetcher = new HtmlFetcher();
    }

    @Test
    void shouldReturnErrorForNullOrEmptyUrl() {
        FetchResponseEntity nullResponse = htmlFetcher.fetchResponse(null);
        assertThat(nullResponse.isSuccess()).isFalse();
        assertThat(nullResponse.getStatusCode()).isEqualTo(400);
        assertThat(nullResponse.getExtractedUrls()).isEmpty();

        FetchResponseEntity emptyResponse = htmlFetcher.fetchResponse("   ");
        assertThat(emptyResponse.isSuccess()).isFalse();
        assertThat(emptyResponse.getStatusCode()).isEqualTo(400);
        assertThat(emptyResponse.getExtractedUrls()).isEmpty();
    }

    @Test
    void shouldReturnErrorForInvalidUrlScheme() {
        FetchResponseEntity response = htmlFetcher.fetchResponse("ftp://example.com");
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getStatusCode()).isEqualTo(400);
        assertThat(response.getExtractedUrls()).isEmpty();
    }

    @Test
    void shouldReturnErrorForMalformedUrl() {
        FetchResponseEntity response = htmlFetcher.fetchResponse("http:// invalid url with spaces");
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getStatusCode()).isEqualTo(400);
        assertThat(response.getExtractedUrls()).isEmpty();
    }

    @Test
    void shouldHandleNonExistentHostGracefully() {
        FetchResponseEntity response = htmlFetcher.fetchResponse("https://non-existent-domain-xyz-123456789.org");
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getStatusCode()).isEqualTo(500);
        assertThat(response.getExtractedUrls()).isEmpty();
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldExtractUrlsOnSuccessResponse() throws IOException, InterruptedException {
        HttpClient mockClient = mock(HttpClient.class);
        HttpResponse<String> mockResponse = mock(HttpResponse.class);

        String sampleHtml = "<html><body><a href=\"https://google.com\">Google</a><a href=\"https://github.com\">GitHub</a></body></html>";

        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(sampleHtml);
        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        HtmlFetcher fetcherWithMock = new HtmlFetcher(mockClient, new HtmlParser());
        FetchResponseEntity response = fetcherWithMock.fetchResponse("https://example.com");

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getUrl()).isEqualTo("https://example.com");
        assertThat(response.getExtractedUrls()).containsExactly("https://google.com", "https://github.com");
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldHandleClientErrorResponse() throws IOException, InterruptedException {
        HttpClient mockClient = mock(HttpClient.class);
        HttpResponse<String> mockResponse = mock(HttpResponse.class);

        when(mockResponse.statusCode()).thenReturn(404);
        when(mockResponse.body()).thenReturn("Not Found");
        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        HtmlFetcher fetcherWithMock = new HtmlFetcher(mockClient, new HtmlParser());
        FetchResponseEntity response = fetcherWithMock.fetchResponse("https://example.com/notfound");

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getStatusCode()).isEqualTo(404);
        assertThat(response.getExtractedUrls()).isEmpty();
    }
}
