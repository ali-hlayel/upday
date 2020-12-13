package com.upday.articleService.models;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class KeywordModel {

    private String keyword;

    private Set<ArticleModel> articles = new HashSet<>();

}
