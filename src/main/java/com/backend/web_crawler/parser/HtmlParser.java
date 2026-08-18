package com.backend.web_crawler.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {

    public List<String> extractUrls(String html) {
        Document doc = Jsoup.parse(html);

        List<String> result = new ArrayList<>();
        Elements links = doc.body().getElementsByTag("a");
        for (int i = 0; i < links.size(); i++) {
            Element element = links.get(i);
            result.add(element.attr("href"));
        }
        return result;
    }
}
