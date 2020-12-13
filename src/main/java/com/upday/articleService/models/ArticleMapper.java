package com.upday.articleService.models;

import com.upday.articleService.entities.Article;
import com.upday.articleService.entities.Author;
import com.upday.articleService.entities.Keyword;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;

@Component
public class ArticleMapper {

    public Article articleModelToArticleEntity(ArticleModel articleModel) {
        Article article = new Article();
        BeanUtils.copyProperties(articleModel, article);
        Set<Author> authorSet = new HashSet<>();
        for (AuthorModel authorModel : articleModel.getAuthors()) {
            Author author = new Author();
            BeanUtils.copyProperties(authorModel, author);
            authorSet.add(author);
        }

        Set<Keyword> keywordSet = new HashSet<>();
        for (KeywordModel keyWordModel : articleModel.getKeywords()) {
            Keyword keyWord = new Keyword();
            BeanUtils.copyProperties(keyWordModel, keyWord);
            keywordSet.add(keyWord);
        }
        article.setAuthors(authorSet);
        article.setKeywords(keywordSet);

        return article;
    }

}
