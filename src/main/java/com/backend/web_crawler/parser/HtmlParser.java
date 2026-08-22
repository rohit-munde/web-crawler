package com.backend.web_crawler.parser;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.backend.web_crawler.data.Page;

@Component
public class HtmlParser {

    public List<String> extractUrls(String html) {
        return extractUrls(html, null);
    }

    public List<String> extractUrls(String html, String url) {
        if (html == null || html.isBlank()) {
            return new ArrayList<>();
        }

        Document doc = (url != null) ? Jsoup.parse(html, url) : Jsoup.parse(html);
        List<String> result = new ArrayList<>();
        if (doc.body() != null && doc.body().hasText()) {
            Elements links = doc.body().getElementsByTag("a");
            for (Element element : links) {
                // absUrl resolves relative links like "/wiki/Cricket" to full URLs
                String href = element.absUrl("href");
                if (href.isBlank()) {
                    href = element.attr("href");
                }
                if (!href.isBlank() && (href.startsWith("http://") || href.startsWith("https://"))) {
                    result.add(href);
                }
            }
        }
        return result;
    }

    public Page extractURL(String url) {
        Page page = new Page();
        try {
            URI uri = new URI(url);
            URL httpUrl = uri.toURL();
            Document doc = Jsoup.parse(httpUrl, 10000);
            page.setLinks(this.extractUrls(doc.html(), url));
            page.setContent(doc.body() != null ? doc.body().text() : "");
            return page;
        } catch (URISyntaxException | IOException e) {
            return page;
        }
    }

}
