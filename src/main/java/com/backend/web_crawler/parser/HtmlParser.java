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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.backend.web_crawler.data.Page;

public class HtmlParser {

    public List<String> extractUrls(String html) {
        if (html == null || html.isBlank()) {
            return new ArrayList<>();
        }
        Document doc = Jsoup.parse(html);

        List<String> result = new ArrayList<>();
        if (doc.body() != null) {
            Elements links = doc.body().getElementsByTag("a");
            for (int i = 0; i < links.size(); i++) {
                Element element = links.get(i);
                result.add(element.attr("href"));
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
            page.setLinks(this.extractUrls(doc.html()));
            page.setContent(doc.body().text());
            return page;
        } catch (URISyntaxException | IOException e) {
            return page;
        }
    }

}
