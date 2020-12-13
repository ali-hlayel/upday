package com.upday.articleService.models;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class AuthorModel {

    private String firstName;

    private String lastName;

    private Set<ArticleModel> articles = new HashSet<>();
}

