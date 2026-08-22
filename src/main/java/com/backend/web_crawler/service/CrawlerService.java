package com.backend.web_crawler.service;

import com.backend.web_crawler.data.Page;
import com.backend.web_crawler.entity.FetchResponseEntity;
import com.backend.web_crawler.parser.HtmlParser;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class CrawlerService {
    private final HtmlParser htmlParser;
    private static final int MAX_DEPTH = 10;

    public CrawlerService(HtmlParser htmlParser) {
        this.htmlParser = htmlParser;
    }

    public FetchResponseEntity crawl(String startUrl) {
        int depth = 0;
        startUrl = startUrl.trim();
        FetchResponseEntity result = new FetchResponseEntity();
        result.setUrl(startUrl);

        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();

        queue.add(startUrl);
        visited.add(startUrl);

        while(!queue.isEmpty() && depth < MAX_DEPTH) {
            String currentUrl = queue.poll();

            Page page = htmlParser.extractURL(currentUrl);
            if (page == null || page.getLinks() == null) continue;

            for(String link : page.getLinks()) {
               if(link != null && !link.isBlank() && !visited.contains(link)) {
                    queue.add(link);
                    visited.add(link);
                    result.getExtractedUrls().add(link);
                }
            }
            depth++;
        }

        result.setSuccess(true);
        result.setStatusCode(200);
        return result;
    }

    public static boolean isValidHttpUrl(String url) {
        if (url == null || url.isBlank()) {
            return false;
        }
        try {
            URI uri = new URI(url.trim());
            String scheme = uri.getScheme();
            return uri.isAbsolute()
                    && ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme))
                    && uri.getHost() != null
                    && !uri.getHost().isBlank();
        } catch (URISyntaxException | IllegalArgumentException e) {
            return false;
        }
    }
}
