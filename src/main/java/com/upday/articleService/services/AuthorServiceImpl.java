package com.upday.articleService.services;

import com.upday.articleService.entities.Article;
import com.upday.articleService.entities.Author;
import com.upday.articleService.models.AuthorQueryModel;
import com.upday.articleService.repositories.ArticleRepository;
import com.upday.articleService.repositories.AuthorRepository;
import com.upday.articleService.repositories.KeywordRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("authorService")
public class AuthorServiceImpl implements AuthorService {

    @Resource
    private AuthorRepository authorRepository;

    @Resource
    private ArticleRepository articleRepository;

    @Resource
    private KeywordRepository keywordRepository;

    @Override
    public Set<Article> getAuthor(AuthorQueryModel author) {
        Set<Article> articleSet = mapDtoToEntity(author);
        return articleSet;
    }

    private Set<Article> mapDtoToEntity(AuthorQueryModel courseDto) {
        List<Author> authorList = authorRepository.findByLastNameAndFirstName(courseDto.getLastName(), courseDto.getFirstName());
        Set<Article> articleSet = new HashSet<>();
        authorList.stream().forEach(result -> {
            result.getArticles().stream().forEach(articleHeader -> {
                Article article = articleRepository.findByHeader(articleHeader.getHeader());
                if (null == article) {
                    article = new Article();
                    article.setAuthors(new HashSet<>());
                }
                articleSet.add(article);
            });
        });
        return articleSet;
    }
}
