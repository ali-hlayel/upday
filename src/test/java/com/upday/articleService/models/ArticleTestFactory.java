package com.upday.articleService.models;

import com.upday.articleService.entities.Article;
import com.upday.articleService.entities.Author;
import com.upday.articleService.entities.Keyword;
import com.upday.articleService.requests.ArticleRequestCreateModel;
import com.upday.articleService.requests.AuthorRequestCreateModel;
import com.upday.articleService.requests.KeywordRequestCreateModel;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ArticleTestFactory {
    public static Article createArticle() {
        Article article = new Article();
        article.setId(1L);
        article.setHeader("Covaid-19 will end soon");
        article.setShortDescription("This short description about the article");
        article.setPublishDate(LocalDate.of(2020, 12, 12));
        article.setText("text heere is a text");
        article.getAuthors().add(createdAuthor());
        article.getKeywords().add(createdKeyword());
        return article;
    }

    public static ArticleRequestCreateModel articleRequestCreate() {
        ArticleRequestCreateModel article = new ArticleRequestCreateModel();
        article.setHeader("Covaid-19 will end soon");
        article.setShortDescription("This short description about the article");
        article.setPublishDate(LocalDate.of(2020, 12, 12));
        article.setText("text heere is a text");


        AuthorRequestCreateModel author = new AuthorRequestCreateModel();
        author.setFirstName("Ali");
        author.setLastName("Hlayel");
        Set<AuthorRequestCreateModel> authorSet = new HashSet<>();
        authorSet.add(author);

        KeywordRequestCreateModel keyword = new KeywordRequestCreateModel();
        keyword.setKeyword("Covaid-19");
        Set<KeywordRequestCreateModel> keywordSet = new HashSet<>();
        keywordSet.add(keyword);

        article.setAuthors(authorSet);
        article.setKeywords(keywordSet);

        return article;
    }

    public static Author createdAuthor() {
        Author author = new Author();
        author.setId(1L);
        author.setFirstName("Ali");
        author.setLastName("Hlayel");
        return author;
    }

    public static Keyword createdKeyword() {
        Keyword keyword = new Keyword();
        keyword.setId(1L);
        keyword.setKeyword("Covaid-19");
        return keyword;
    }
}
