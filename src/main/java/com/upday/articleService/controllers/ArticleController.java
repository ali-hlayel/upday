package com.upday.articleService.controllers;

import com.upday.articleService.config.exceptions.*;
import com.upday.articleService.entities.Article;
import com.upday.articleService.models.*;
import com.upday.articleService.services.ArticleService;
import com.upday.articleService.services.AuthorService;
import com.upday.articleService.services.KeywordService;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/article-service", produces = MediaType.APPLICATION_JSON_VALUE)
public class ArticleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(Article.class);

    private final ArticleMapper articleMapper;

    private final ArticleService articleService;

    private final AuthorService authorService;

    private final KeywordService keywordService;

    @Autowired
    public ArticleController(ArticleMapper articleMapper, ArticleService articleService, AuthorService authorService, KeywordService keywordService) {
        this.articleMapper = articleMapper;
        this.articleService = articleService;
        this.authorService = authorService;
        this.keywordService = keywordService;
    }

    @Operation(summary = "Get all Articles")
    @GetMapping(value = "/articles")
    public ResponseEntity<List<ArticleModel>> getAllBooks(@RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "limit", defaultValue = "10") int limit) {

        List<ArticleModel> results = new ArrayList<>();
        List<Article> articleList = articleService.getAllArticles(page, limit);
        ModelMapper modelMapper = new ModelMapper();
        for (Article article : articleList) {
            results.add(modelMapper.map(article, ArticleModel.class));
        }
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @Operation(summary = "Get list of articles for a certain period")
    @GetMapping("/articles/between")
    public List<Article> getArticlesByDate(ArticleQueryByDateModel params) throws ServiceResponseException {
        List<Article> articles;
        try {
            articles = articleService.get(buildSpecification(params));
            return articles;
        } catch (NoResultException e) {
            String message = e.getMessage() + params.getPublishDateFrom() + " To: " + params.getPublishDateTo();
            LOGGER.error(message, e);
            throw new BadRequestException(message, e);
        }
    }

    @Operation(summary = "Get list of articles for a certain author")
    @GetMapping("/author")
    public Set<Article> getArticleByAuthor(AuthorQueryModel requestModel) {
        Set<Article> result;
        result = authorService.getAuthor(requestModel);
        return result;
    }

    @Operation(summary = "Get list of articles for a certain keyWord")
    @GetMapping("/keyword")
    public Set<Article> getArticleByAuthor(KeywordModel requestModel) {
        Set<Article> result;
        result = keywordService.getKeyword(requestModel);
        return result;
    }

    @Operation(summary = "Creates new article")
    @PostMapping("/article")
    public ResponseEntity<Article> createArticle(@Valid @RequestBody ArticleModel createModel) throws ServiceResponseException {
        Article result;
        Article article = articleMapper.articleModelToArticleEntity(createModel);
        try {
            result = articleService.create(article);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (EntityAlreadyExistsException | MissingPrerequisiteException e) {
            String message = "Could not create a Book: " + e.getMessage();
            LOGGER.error(message, e);
            throw new UnprocessableEntityException(message, e);
        }
    }

    @Operation(summary = "Update a single article")
    @PatchMapping("/article/{id}")
    public ResponseEntity<Article> updateArticle(
            @Min(value = 1) @PathVariable Long id,
            @Valid @RequestBody ArticleRequestUpdateModel articleRequestUpdateModel
    ) throws ServiceResponseException {


        Article result;
        try {
            result = articleService.update(id, articleRequestUpdateModel);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NoResultException e) {
            String message = "Could not update a channel: " + e.getMessage();
            LOGGER.error(message, e);
            throw new BadRequestException(message, e);

        }
    }

    @Operation(summary = "Deletes a single article by Id")
    @DeleteMapping("/article/{id}")
    public ResponseEntity<?> deleteArticle(@Min(value = 1) @PathVariable Long id) throws ServiceResponseException {
        try {
            articleService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoResultException e) {
            String message = "Could not delete article with id " + id + ": " + e.getMessage();
            LOGGER.error(message, e);
            throw new BadRequestException(message, e);
        }

    }

    private Specification<Article> buildSpecification(ArticleQueryByDateModel queryParameters) {
        return (Specification<Article>) (root, q, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (queryParameters == null) {
                return predicate;
            }
            predicate = greaterThanOrEqual(predicate, "publishDate", queryParameters.getPublishDateFrom(), root, criteriaBuilder);
            predicate = lessThanOrEqual(predicate, "publishDate", queryParameters.getPublishDateTo(), root, criteriaBuilder);
            return predicate;
        };
    }

    private Predicate greaterThanOrEqual(Predicate originalPredicate, String key, LocalDate value, Root<?> root, CriteriaBuilder criteriaBuilder) {
        if (value == null) {
            return originalPredicate;
        }
        return criteriaBuilder.and(originalPredicate, criteriaBuilder
                .greaterThanOrEqualTo(root.get(key).as(LocalDate.class), value));
    }

    private Predicate lessThanOrEqual(Predicate originalPredicate, String key, LocalDate value, Root<?> root, CriteriaBuilder criteriaBuilder) {
        if (value == null) {
            return originalPredicate;
        }
        return criteriaBuilder.and(originalPredicate, criteriaBuilder
                .lessThanOrEqualTo(root.get(key).as(LocalDate.class), value));
    }
}
