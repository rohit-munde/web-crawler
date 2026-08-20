package com.backend.web_crawler.controller;

import com.backend.web_crawler.entity.FetchResponseEntity;
import com.backend.web_crawler.fetcher.HtmlFetcher;
import com.backend.web_crawler.parser.HtmlParser;
import com.backend.web_crawler.response.ApiSuccessResponse;

import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/crawler")
public class CrawlerController {

    private final HtmlFetcher htmlFetcher;
    private final HtmlParser htmlParser;

    public CrawlerController(HtmlFetcher htmlFetcher, HtmlParser htmlParser) {
        this.htmlFetcher = htmlFetcher;
        this.htmlParser = htmlParser;
    }

    @GetMapping
    public ResponseEntity<@NonNull ApiSuccessResponse<FetchResponseEntity>> parseUrl(@RequestParam String url) {
        FetchResponseEntity rootResponse = htmlFetcher.fetchResponse(url);


        if (rootResponse.isSuccess()) {
            List<String> extractedUrlList = new ArrayList<>();

            for(String childUrl: rootResponse.getExtractedUrls()) {
                FetchResponseEntity childResponse = htmlFetcher.fetchResponse(childUrl);
                if (childResponse.isSuccess()) {
                    extractedUrlList.addAll(childResponse.getExtractedUrls());
                }
            }

            rootResponse.getExtractedUrls().addAll(extractedUrlList);
        }

        return ResponseEntity.ok(new ApiSuccessResponse<>(rootResponse.isSuccess(), "URL parsed successfully", rootResponse));
    }
}
