package com.upday.articleService.services;

import com.upday.articleService.config.exceptions.EntityAlreadyExistsException;
import com.upday.articleService.config.exceptions.MissingPrerequisiteException;
import com.upday.articleService.entities.Article;
import com.upday.articleService.entities.Author;
import com.upday.articleService.entities.Keyword;
import com.upday.articleService.mappers.ArticleMapper;
import com.upday.articleService.requests.ArticleRequestCreateModel;
import com.upday.articleService.requests.ArticleRequestUpdateModel;
import com.upday.articleService.repositories.ArticleRepository;
import com.upday.articleService.repositories.AuthorRepository;
import com.upday.articleService.repositories.KeywordRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import java.beans.FeatureDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleRepository articleRepository;

    @Resource
    private AuthorRepository authorRepository;

    @Resource
    private KeywordRepository keyWordRepository;

    private ArticleMapper articleMapper;

    public ArticleServiceImpl(ArticleRepository articleRepository,
                              AuthorRepository authorRepository,
                              KeywordRepository keyWordRepository,
                              ArticleMapper articleMapper) {
        super();
        this.articleRepository = articleRepository;
        this.authorRepository = authorRepository;
        this.keyWordRepository = keyWordRepository;
        this.articleMapper = articleMapper;
    }

    @Override
    public List<Article> get(Specification<Article> specification) throws NoResultException {
        List<Article> articleList = articleRepository.findAll(specification);
        if (articleList.isEmpty()) {
            throw new NoResultException("Could not find Articles for the period from: ");
        }
        return articleRepository.findAll(specification);
    }

    @Override
    public List<Article> getAllArticles(int page, int limit) {
        if (page > 0) page = page - 1;
        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<Article> personsPage = articleRepository.findAll(pageableRequest);
        List<Article> personsList = personsPage.getContent();
        return personsList;
    }

    @Transactional
    @Override
    public Article create(ArticleRequestCreateModel createModel) throws MissingPrerequisiteException, EntityAlreadyExistsException {
        Article result;
        Article article = articleMapper.articleModelToArticleEntity(createModel);
        if (article.getHeader() == null) {
            throw new MissingPrerequisiteException("Can't create Article without  " + article.getHeader());
        }

        if (article.getAuthors() == null) {
            throw new MissingPrerequisiteException("Can't create Article:  " + article.getHeader() + "Please add the author name.");
        }

        if (articleRepository.existsByHeader(article.getHeader())) {
            throw new EntityAlreadyExistsException("Can't create article, name " + article.getHeader()
                    + " is already exists.");
        }
        result = articleRepository.save(article);
        return result;
    }

    @Transactional
    @Override
    public Article update(Long id, ArticleRequestUpdateModel updatedArticle) {
        Article existingArticle = articleRepository.findById(id).orElseThrow(
                () -> new NoResultException("Could not find Article with id " + id)
        );
        copyProperties(updatedArticle, existingArticle);
        handleArticleAuthorAndKeyWord(updatedArticle, existingArticle);
        Article result = articleRepository.save(existingArticle);
        return result;
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Article result = articleRepository.findById(id).orElseThrow(
                () -> new NoResultException("Could not find article with id " + id)
        );
        articleRepository.delete(result);
    }

    private void handleArticleAuthorAndKeyWord(ArticleRequestUpdateModel updateModel, Article article) {
        if (!updateModel.getAuthors().isEmpty()) {
            article.getAuthors().clear();
            updateModel.getAuthors().stream().forEach(authorName -> {
                Author author = authorRepository.findByFirstNameAndLastName(authorName.getFirstName(), authorName.getLastName());
                if (author == null) {
                    author = new Author();
                }
                copyProperties(authorName, author);
                article.getAuthors().add(author);

            });
        }
        if (!updateModel.getKeywords().isEmpty()) {
            article.getKeywords().clear();
            updateModel.getKeywords().stream().forEach(updatedKeyword -> {
                Keyword keyword = keyWordRepository.findByKeyword(updatedKeyword.getKeyword());
                if (keyword == null) {
                    keyword = new Keyword();
                }
                copyProperties(updatedKeyword, keyword);
                article.getKeywords().add(keyword);

            });
        }
    }

    protected void copyProperties(Object updatedEntity, Object existingEntity, String... excludedProperties) {
        List<String> excludedPropertyNames = getNullPropertyNames(updatedEntity);
        excludedPropertyNames.addAll(Arrays.asList(excludedProperties));
        BeanUtils.copyProperties(updatedEntity, existingEntity, excludedPropertyNames.toArray(new String[0]));
    }

    private List<String> getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .collect(Collectors.toList());
    }
}