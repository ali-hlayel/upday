package com.upday.articleService.models;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ArticleModel {

    private String header;

    private String shortDescription;

    private String text;

    private LocalDate publishDate;

    private Set<AuthorModel> authors = new HashSet<>();

    private Set<KeywordModel> keywords = new HashSet<>();

    @Override
    public String toString() {
        return "ArticleRequestCreateModel{" +
                "header='" + header + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", text='" + text + '\'' +
                ", publishDate=" + publishDate +
                ", authors=" + authors +
                ", keywords=" + keywords +
                '}';
    }
}
