package com.backend.web_crawler.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Page {
    private List<String> links = new ArrayList<>();
    private List<Page> pages = new ArrayList<>();
    private String content;
}