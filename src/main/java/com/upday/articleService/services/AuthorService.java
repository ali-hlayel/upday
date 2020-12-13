package com.upday.articleService.services;

import com.upday.articleService.entities.Article;
import com.upday.articleService.requests.AuthorQueryModel;

import java.util.Set;

public interface AuthorService {

    Set<Article> getAuthor(AuthorQueryModel authorModel);
}
