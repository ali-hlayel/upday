package com.upday.articleService.controllers;

import com.upday.articleService.config.exceptions.*;
import com.upday.articleService.entities.Article;
import com.upday.articleService.mappers.ArticleMapper;
import com.upday.articleService.models.*;
import com.upday.articleService.requests.*;
import com.upday.articleService.services.ArticleService;
import com.upday.articleService.services.AuthorService;
import com.upday.articleService.services.KeywordService;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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

    private final ArticleService articleService;

    private final AuthorService authorService;

    private final KeywordService keywordService;

    @Autowired
    public ArticleController(ArticleService articleService, AuthorService authorService, KeywordService keywordService) {
        this.articleService = articleService;
        this.authorService = authorService;
        this.keywordService = keywordService;
    }

    @Operation(summary = "Get all Articles")
    @GetMapping(value = "/articles")
    public ResponseEntity<List<Article>> getAllAuthors(@RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<Article> results = articleService.getAllArticles(page, limit);
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
            throw new NotFoundException(message, e);
        }
    }

    @Operation(summary = "Get list of articles for a certain author")
    @GetMapping("/articles/author")
    public Set<Article> getArticleByAuthor(AuthorQueryModel requestModel) throws ServiceResponseException {
        Set<Article> result;
        try {
            result = authorService.getAuthor(requestModel);
        } catch (NoResultException e) {
            String message = "Could not find articles for author " + requestModel.getFirstName() + " " +
                    requestModel.getLastName() + ": " + e.getMessage();
            LOGGER.error(message, e);
            throw new NotFoundException(message, e);
        }
        return result;
    }

    @Operation(summary = "Get list of articles for a certain keyWord")
    @GetMapping("/articles/keyword")
    public Set<Article> getArticleByKeyword(KeywordQueryModel requestModel) throws ServiceResponseException {
        Set<Article> result;
        KeywordModel model = new KeywordModel();
        BeanUtils.copyProperties(requestModel, model);
        try {
        result = keywordService.getKeyword(model);
        } catch (NoResultException e) {
            String message = "Could not find articles for keyword " + model.getKeyword() + " " +
                    ": " + e.getMessage();
            LOGGER.error(message, e);
            throw new NotFoundException(message, e);
        }
        return result;
    }

    @Operation(summary = "Creates new article")
    @PostMapping("/article")
    public ResponseEntity<Article> createArticle(@Valid @RequestBody ArticleRequestCreateModel model) throws ServiceResponseException {
        Article result;
        try {
            result = articleService.create(model);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (EntityAlreadyExistsException e) {
            String message = "Could not create article: " + e.getMessage();
            LOGGER.error(message, e);
            throw new UnprocessableEntityException(message, e);
        } catch (MissingPrerequisiteException e) {
            String message = "Could not create article: " + e.getMessage();
            LOGGER.error(message, e);
            throw new ConflictException(message, e);
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
    public ResponseEntity<Article> deleteArticle(@Min(value = 1) @PathVariable Long id) throws ServiceResponseException {

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
