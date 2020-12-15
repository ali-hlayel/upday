package com.upday.articleService.controllers;

import com.upday.articleService.config.exceptions.EntityAlreadyExistsException;
import com.upday.articleService.config.exceptions.GlobalExceptionHandler;
import com.upday.articleService.config.exceptions.MissingPrerequisiteException;
import com.upday.articleService.entities.Article;
import com.upday.articleService.models.ArticleTestFactory;
import com.upday.articleService.models.KeywordModel;
import com.upday.articleService.requests.*;
import com.upday.articleService.services.ArticleService;
import com.upday.articleService.services.AuthorService;
import com.upday.articleService.services.KeywordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ArticleControllerTest {

    @Mock
    private ArticleService articleService;

    @Mock
    private AuthorService authorService;

    @Mock
    private KeywordService keywordService;

    private MockMvc mockMvc;

    private final String LINK = "/article-service";

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new ArticleController(articleService, authorService, keywordService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        this.mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
    }

    @Test
    void testCreateArticle() throws Exception {
        ArticleRequestCreateModel articleRequestCreateModel = ArticleTestFactory.articleRequestCreate();
        Article article = ArticleTestFactory.createArticle();
        when(articleService.create(any(ArticleRequestCreateModel.class))).thenReturn(article);
        this.mockMvc.perform(post(LINK + "/article")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json(articleRequestCreateModel)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateArticleThrowsMissingPrerequisiteException() throws Exception {
        ArticleRequestCreateModel articleRequestCreateModel = ArticleTestFactory.articleRequestCreate();
        when(articleService.create(any(ArticleRequestCreateModel.class))).thenThrow(MissingPrerequisiteException.class);
        this.mockMvc.perform(post(LINK + "/article")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json(articleRequestCreateModel)))
                .andExpect(status().isConflict());
    }

    @Test
    void testCreateArticleForArticleAlreadyExists() throws Exception {
        ArticleRequestCreateModel articleRequestCreateModel = ArticleTestFactory.articleRequestCreate();
        when(articleService.create(any(ArticleRequestCreateModel.class))).thenThrow(EntityAlreadyExistsException.class);
        this.mockMvc.perform(post(LINK + "/article")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json(articleRequestCreateModel)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testGetAllAuthors() throws Exception {
        Article firstArticle = ArticleTestFactory.createArticle();
        Article secondArticle = ArticleTestFactory.createArticle();
        List<Article> articles = Arrays.asList(firstArticle, secondArticle);
        when(articleService.getAllArticles(0, 2)).thenReturn(articles);
        this.mockMvc.perform(get(LINK + "/articles"))
                .andExpect(status().isOk());
        assertEquals(2, articles.size());
    }

    @Test
    void testGetArticlesByDate() throws Exception {
        Article article = ArticleTestFactory.createArticle();
        List<Article> articles = Arrays.asList(article);
        ArticleQueryByDateModel articleQueryByDateModel = ArticleTestFactory.searchByDate();
        when(articleService.get(any(Specification.class))).thenReturn(articles);
        this.mockMvc.perform(get(LINK + "/articles/between")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json(articleQueryByDateModel)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetArticlesByDateThrowNoResultsException() throws Exception {
        ArticleQueryByDateModel articleQueryByDateModel = ArticleTestFactory.searchByDate();
        when(articleService.get(any(Specification.class))).thenThrow(NoResultException.class);
        this.mockMvc.perform(get(LINK + "/articles/between")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json(articleQueryByDateModel)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetArticleByAuthor() throws Exception {
        Article article = ArticleTestFactory.createArticle();
        Set<Article> articles = new HashSet<>();
        articles.add(article);
        AuthorQueryModel authorQueryModel = ArticleTestFactory.searchByAuthor();
        when(authorService.getAuthor(any(AuthorQueryModel.class))).thenReturn(articles);
        this.mockMvc.perform(get(LINK + "/articles/author")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json(authorQueryModel)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetArticlesByAuthorThrowNoResultsException() throws Exception {
        AuthorQueryModel authorQueryModel = ArticleTestFactory.searchByAuthor();
        when(authorService.getAuthor(any(AuthorQueryModel.class))).thenThrow(NoResultException.class);
        this.mockMvc.perform(get(LINK + "/articles/author")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json(authorQueryModel)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetArticleByKeyword() throws Exception {
        Article article = ArticleTestFactory.createArticle();
        Set<Article> articles = new HashSet<>();
        articles.add(article);
        KeywordQueryModel keywordQueryModel = ArticleTestFactory.searchByKeyword();
        when(keywordService.getKeyword(any(KeywordModel.class))).thenReturn(articles);
        this.mockMvc.perform(get(LINK + "/articles/keyword")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json(keywordQueryModel)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetArticleByKeywordThrowNoResultException() throws Exception {
        KeywordQueryModel keywordQueryModel = ArticleTestFactory.searchByKeyword();
        when(keywordService.getKeyword(any(KeywordModel.class))).thenThrow(NoResultException.class);
        this.mockMvc.perform(get(LINK + "/articles/keyword")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json(keywordQueryModel)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateArticle() throws Exception {
        Article article = ArticleTestFactory.createArticle();
        ArticleRequestUpdateModel updateModel = ArticleTestFactory.updateArticle();
        when(articleService.update(any(Long.class), any(ArticleRequestUpdateModel.class))).thenReturn(article);
        mockMvc.perform(patch(LINK + "/article/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json(updateModel)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateArticleThrowsNoResultException() throws Exception {
        ArticleRequestUpdateModel updateModel = ArticleTestFactory.updateArticle();
        when(articleService.update(any(Long.class), any(ArticleRequestUpdateModel.class))).thenThrow(NoResultException.class);
        mockMvc.perform(patch(LINK + "/article/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json(updateModel)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteArticle() throws Exception {
        this.mockMvc.perform(delete(LINK + "/article/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteArticleThrowsNoResultException() throws Exception {
        doThrow(NoResultException.class).when(articleService).delete(1L);
        this.mockMvc.perform(delete(LINK + "/article/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}