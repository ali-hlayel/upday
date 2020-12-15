package com.upday.articleService.controllers;

import com.upday.articleService.config.exceptions.EntityAlreadyExistsException;
import com.upday.articleService.config.exceptions.GlobalExceptionHandler;
import com.upday.articleService.config.exceptions.MissingPrerequisiteException;
import com.upday.articleService.entities.Article;
import com.upday.articleService.mappers.ArticleMapper;
import com.upday.articleService.models.ArticleTestFactory;
import com.upday.articleService.requests.ArticleRequestCreateModel;
import com.upday.articleService.requests.KeywordRequestCreateModel;
import com.upday.articleService.services.ArticleService;
import com.upday.articleService.services.AuthorService;
import com.upday.articleService.services.KeywordService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ArticleControllerTest {

    @Mock
    private  ArticleService articleService;

    @Mock
    private  AuthorService authorService;

    @Mock
    private  KeywordService keywordService;

    private MockMvc mockMvc;

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
        this.mockMvc.perform(post("/article-service" + "/article")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json(articleRequestCreateModel)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateArticleThrowsMissingPrerequisiteException() throws Exception {
        ArticleRequestCreateModel articleRequestCreateModel = ArticleTestFactory.articleRequestCreate();
        when(articleService.create(any(ArticleRequestCreateModel.class))).thenThrow(MissingPrerequisiteException.class);
        this.mockMvc.perform(post("/article-service" + "/article")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json(articleRequestCreateModel)))
                .andExpect(status().isConflict());
    }

    @Test
    void testCreateArticleForArticleAlreadyExists() throws Exception {
        ArticleRequestCreateModel articleRequestCreateModel = ArticleTestFactory.articleRequestCreate();
        when(articleService.create(any(ArticleRequestCreateModel.class))).thenThrow(EntityAlreadyExistsException.class);
        this.mockMvc.perform(post("/article-service" + "/article")
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
        this.mockMvc.perform(get("/article-service" + "/articles"))
                .andExpect(status().isOk());

        assertEquals(2, articles.size());
    }

    @Test
    void testGetArticlesByDate() {
    }

    @Test
    void testGetArticleByAuthor() {
    }

    @Test
    void testGetArticleByKeyword() {
    }

    @Test
    void testUpdateArticle() {
    }

    @Test
    void testDeleteArticle() {
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}