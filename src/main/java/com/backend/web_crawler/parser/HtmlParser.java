package com.backend.web_crawler.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

public class HtmlParser {

    public List<String> extractUrls(String html) {
        Document doc = Jsoup.parse(html);

//        List<String> temp = new ArrayList<>();
//        for (org.jsoup.nodes.Element link : doc.select("a[href]")) {
//            temp.add(link.attr("href"));
//        }


        return doc.select("href").stream()
                .map(link -> link.attr("href"))
                .toList();
    }
}
