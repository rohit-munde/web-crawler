package com.backend.web_crawler.parser;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

public class HtmlParserTest {

    @Test
    void shouldExtractUrlFromHtml() {
        String html = """
            <html>
                <head>
                    <title>Test Page</title>
                </head>
                <body>
                    <a href="https://example.com">Example</a>
                    <a href="https://test.com">Test</a>
                </body>
            </html>""";

        HtmlParser htmlParser = new HtmlParser();
        List<String> urls = new ArrayList<>();

        urls = htmlParser.extractUrls(html);

        assertEquals(2, urls.size());
        assertTrue(urls.contains("https://example.com"));
        assertTrue(urls.contains("https://test.com"));

        assertThat(urls).containsExactlyInAnyOrder("https://example.com", "https://test.com");
    }
}
