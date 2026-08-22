package com.backend.web_crawler.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.backend.web_crawler.service.CrawlerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.web_crawler.entity.FetchResponseEntity;
import com.backend.web_crawler.response.ApiSuccessResponse;

import lombok.NonNull;

@RestController
@RequestMapping("/api/crawler")
public class CrawlerController {
    private final CrawlerService crawlerService;

    public CrawlerController(CrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @GetMapping
    public ResponseEntity<@NonNull ApiSuccessResponse<FetchResponseEntity>> parseUrl(@RequestParam String url) {
        if(!CrawlerService.isValidHttpUrl(url))
        {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiSuccessResponse<>(
                            false,
                            "Invalid URL",
                            null
                    ));
        }
        if(url.isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiSuccessResponse<>(
                            false,
                            "Invalid URL",
                            null
                    ));
        }

        FetchResponseEntity response = crawlerService.crawl(url);

        return ResponseEntity
                .ok(new ApiSuccessResponse<>(
                        response.isSuccess(),
                        "URL parsed successfully",
                        response
                ));
    }
}
