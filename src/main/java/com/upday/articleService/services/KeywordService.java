package com.upday.articleService.services;

import com.upday.articleService.entities.Article;
import com.upday.articleService.models.KeywordModel;

import javax.persistence.NoResultException;
import java.util.Set;

public interface KeywordService {

    Set<Article> getKeyword(KeywordModel keywordModel) throws NoResultException;
}
