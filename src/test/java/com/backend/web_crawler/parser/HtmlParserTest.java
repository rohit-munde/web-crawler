package com.backend.web_crawler.parser;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.backend.web_crawler.data.Page;

class HtmlParserTest {

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
        List<String> urls = htmlParser.extractUrls(html);

        assertEquals(2, urls.size());
        assertTrue(urls.contains("https://example.com"));
        assertTrue(urls.contains("https://test.com"));

        assertThat(urls).containsExactlyInAnyOrder("https://example.com", "https://test.com");
    }

    @Test
    void shouldHaveContent() {
        HtmlParser htmlParser = new HtmlParser();
        Page url = htmlParser.extractURL("https://www.agileguru.org");

        assertThat(url.getContent()).isNotNull().isNotEmpty();
        assertThat(url.getLinks()).isNotEmpty();

    }
}
