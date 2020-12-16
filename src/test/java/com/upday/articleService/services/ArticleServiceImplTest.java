package com.upday.articleService.services;

import com.upday.articleService.config.exceptions.EntityAlreadyExistsException;
import com.upday.articleService.config.exceptions.MissingPrerequisiteException;
import com.upday.articleService.entities.Article;
import com.upday.articleService.mappers.ArticleMapper;
import com.upday.articleService.models.ArticleTestFactory;
import com.upday.articleService.repositories.ArticleRepository;
import com.upday.articleService.repositories.AuthorRepository;
import com.upday.articleService.repositories.KeywordRepository;
import com.upday.articleService.requests.ArticleRequestCreateModel;
import com.upday.articleService.requests.ArticleRequestUpdateModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.NoResultException;
import javax.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceImplTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private KeywordRepository keyWordRepository;

    @Mock
    private ArticleMapper articleMapper;

    @InjectMocks
    private ArticleServiceImpl articleService;

    @Test
    void testGet() {
        Article firstArticle = ArticleTestFactory.createArticle();
        Article secondArticle = ArticleTestFactory.createArticle();
        secondArticle.setId(2L);
        List<Article> articleList = new ArrayList<>();
        articleList.add(firstArticle);
        articleList.add(secondArticle);
        Specification<Article> specification = buildSpecification();
        when(articleRepository.findAll(specification)).thenReturn(articleList);
        List<Article> results = articleService.get(specification);
        assertEquals(2, results.size());
        assertEquals(firstArticle.getHeader(), results.get(0).getHeader());
        assertEquals(secondArticle.getHeader(), results.get(1).getHeader());
    }

    @Test
    void testGetThrowNoResultException() {
        Specification<Article> specification = buildSpecification();
        when(articleRepository.findAll(specification)).thenReturn(Collections.emptyList());
        assertThrows(NoResultException.class, () -> articleService.get(specification));
    }

    @Test
    void testGetAllArticles() {
        PageRequest pageRequest = PageRequest.of(0, 2);
        Article firstArticle = ArticleTestFactory.createArticle();
        Article secondArticle = ArticleTestFactory.createArticle();
        secondArticle.setId(2L);
        List<Article> articleList = new ArrayList<>();
        articleList.add(firstArticle);
        articleList.add(secondArticle);
        Page<Article> articlePage = new PageImpl<>(articleList, pageRequest, articleList.size());
        when(articleRepository.findAll(pageRequest)).thenReturn(articlePage);
        List<Article> results = articleService.getAllArticles(0, 2);
        assertEquals(2, results.size());
        assertEquals(firstArticle.getHeader(), results.get(0).getHeader());
        assertEquals(secondArticle.getHeader(), results.get(1).getHeader());
        verify(articleRepository).findAll(pageRequest);
    }

    @Test
    void testCreate() throws EntityAlreadyExistsException, MissingPrerequisiteException {
        Article article = ArticleTestFactory.createArticle();
        ArticleRequestCreateModel createdArticle = ArticleTestFactory.articleRequestCreate();
        when(articleRepository.save(any(Article.class))).thenReturn(article);
        when(articleMapper.articleModelToArticleEntity(any(ArticleRequestCreateModel.class))).thenReturn(article);
        Article result = articleService.create(createdArticle);

        assertEquals(createdArticle.getHeader(), result.getHeader());
        assertEquals(createdArticle.getPublishDate(), result.getPublishDate());
        assertEquals(1L, result.getId());
    }

    @Test
    void testCreateThrowsEntityAlreadyExistsException() {
        Article article = ArticleTestFactory.createArticle();
        ArticleRequestCreateModel createdArticle = ArticleTestFactory.articleRequestCreate();
        when(articleMapper.articleModelToArticleEntity(any(ArticleRequestCreateModel.class))).thenReturn(article);
        when(articleRepository.existsByHeader(article.getHeader())).thenReturn(true);
        assertThrows(EntityAlreadyExistsException.class, () -> articleService.create(createdArticle));
    }

    @Test
    void testUpdate() {
        Article article = ArticleTestFactory.createArticle();
        ArticleRequestUpdateModel updatedArticle = ArticleTestFactory.updateArticle();
        when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));
        when(articleRepository.save(any(Article.class))).thenReturn(article);

        Article result = articleService.update(1L, updatedArticle);
        assertEquals(updatedArticle.getHeader(), result.getHeader());
        verify(articleRepository).save(article);
    }

    @Test
    void testUpdateThrowsNoResultException() {
        ArticleRequestUpdateModel updatedArticle = ArticleTestFactory.updateArticle();
        when(articleRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(NoResultException.class, () -> articleService.update(1L, updatedArticle));
    }

    @Test
    void testDelete() {
        Article article = ArticleTestFactory.createArticle();
        when(articleRepository.findById(any(Long.class))).thenReturn(Optional.of(article));
        articleService.delete(1L);
        verify(articleRepository).delete(article);
    }

    @Test
    void testDeleteThrowsNoResultException() {
        when(articleRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(NoResultException.class, () -> articleService.delete(1L));
    }

    private Specification<Article> buildSpecification() {
        return (Specification<Article>) (root, q, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            return predicate;
        };
    }
}