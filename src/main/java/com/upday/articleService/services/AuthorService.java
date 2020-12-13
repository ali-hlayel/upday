package com.upday.articleService.services;

import com.upday.articleService.entities.Article;
import com.upday.articleService.entities.Author;
import com.upday.articleService.models.AuthorModel;
import com.upday.articleService.models.AuthorQueryModel;

import java.util.Set;

public interface AuthorService {

    Set<Article> getAuthor(AuthorQueryModel authorModel);
}
