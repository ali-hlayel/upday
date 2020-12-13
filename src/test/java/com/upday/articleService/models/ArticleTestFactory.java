package com.upday.articleService.models;

import com.upday.articleService.entities.Article;
import com.upday.articleService.entities.Author;
import com.upday.articleService.entities.Keyword;

import java.time.LocalDate;

public class ArticleTestFactory {
    public static Article createArticle() {
        Article article = new Article();
        article.setId(1L);
        article.setHeader("Covaid-19 will end soon");
        article.setShortDescription("This short description about the article");
        article.setPublishDate(LocalDate.of(2020,12,12));
        article.getAuthors().add(createdAuthor());
        article.getKeywords().add(createdKeyword());
        return article;
}

public static Author createdAuthor() {
        Author author = new Author();
        author.setId(1L);
        author.setFirstName("Ali");
        author.setLastName("Hlayel");
        return author;
}

public  static Keyword  createdKeyword() {
        Keyword keyword = new Keyword();
        keyword.setId(1L);
        keyword.setKeyword("Covaid-19");
        return keyword;
}
}
