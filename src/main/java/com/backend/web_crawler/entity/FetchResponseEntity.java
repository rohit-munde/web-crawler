package com.backend.web_crawler.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "fetch_responses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FetchResponseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String url;

    private boolean success;

    private int statusCode;

    @ElementCollection
    @CollectionTable(name = "extracted_urls", joinColumns = @JoinColumn(name = "fetch_response_id"))
    @Column(name = "extracted_url", length = 2048)
    @Builder.Default
    private List<String> extractedUrls = new ArrayList<>();

    public FetchResponseEntity(String url, boolean success, int statusCode, String[] extractedUrls) {
        this.url = url;
        this.success = success;
        this.statusCode = statusCode;
        this.extractedUrls = extractedUrls != null ? new ArrayList<>(Arrays.asList(extractedUrls)) : new ArrayList<>();
    }


    public void setExtractedUrls(String[] urls) {
        this.extractedUrls = urls != null ? new ArrayList<>(Arrays.asList(urls)) : new ArrayList<>();
    }
}
