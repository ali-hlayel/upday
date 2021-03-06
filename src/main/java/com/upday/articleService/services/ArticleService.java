package com.upday.articleService.services;

import com.upday.articleService.config.exceptions.EntityAlreadyExistsException;
import com.upday.articleService.config.exceptions.MissingPrerequisiteException;
import com.upday.articleService.entities.Article;
import com.upday.articleService.requests.ArticleRequestCreateModel;
import com.upday.articleService.requests.ArticleRequestUpdateModel;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.NoResultException;
import java.util.List;


public interface ArticleService {

    Article create(ArticleRequestCreateModel article) throws MissingPrerequisiteException, EntityAlreadyExistsException;

    void delete(Long id);

    Article update(Long id, ArticleRequestUpdateModel updatedArticle);

    List<Article> getAllArticles(int page, int limit);

    List<Article> get(Specification<Article> specification) throws NoResultException ;
}
