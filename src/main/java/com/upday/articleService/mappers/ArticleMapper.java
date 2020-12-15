package com.upday.articleService.mappers;

import com.upday.articleService.entities.Article;
import com.upday.articleService.entities.Author;
import com.upday.articleService.entities.Keyword;
import com.upday.articleService.models.ArticleModel;
import com.upday.articleService.models.AuthorModel;
import com.upday.articleService.models.KeywordModel;
import com.upday.articleService.repositories.AuthorRepository;
import com.upday.articleService.repositories.KeywordRepository;
import com.upday.articleService.requests.ArticleRequestCreateModel;
import com.upday.articleService.requests.AuthorRequestCreateModel;
import com.upday.articleService.requests.KeywordRequestCreateModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@Component
public class ArticleMapper {

    @Resource
    private AuthorRepository authorRepository;

    @Resource
    private KeywordRepository keywordRepository;

    public Article articleModelToArticleEntity(ArticleRequestCreateModel articleModel) {
        Article article = new Article();
        BeanUtils.copyProperties(articleModel, article);
        Set<Author> authorSet = new HashSet<>();
        for (AuthorRequestCreateModel authorModel : articleModel.getAuthors()) {
            if (authorRepository.existsByFirstNameAndLastName(authorModel.getFirstName(), authorModel.getLastName())) {
                Author author;
                author = authorRepository.findByFirstNameAndLastName(authorModel.getFirstName(), authorModel.getLastName());
                authorSet.add(author);
            } else {
                Author author = new Author();
                BeanUtils.copyProperties(authorModel, author);
                authorSet.add(author);
            }
        }

        Set<Keyword> keywordSet = new HashSet<>();
        for (KeywordRequestCreateModel keyWordModel : articleModel.getKeywords()) {

            if (keywordRepository.existsByKeyword(keyWordModel.getKeyword())) {
                Keyword keyword;
                keyword = keywordRepository.findByKeyword(keyWordModel.getKeyword());
                keywordSet.add(keyword);
            } else {
                Keyword keyWord = new Keyword();
                BeanUtils.copyProperties(keyWordModel, keyWord);
                keywordSet.add(keyWord);
            }
        }
        article.setAuthors(authorSet);
        article.setKeywords(keywordSet);

        return article;
    }

}
