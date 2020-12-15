package com.upday.articleService.services;

import com.upday.articleService.entities.Article;
import com.upday.articleService.entities.Author;
import com.upday.articleService.requests.AuthorQueryModel;
import com.upday.articleService.repositories.ArticleRepository;
import com.upday.articleService.repositories.AuthorRepository;
import com.upday.articleService.repositories.KeywordRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.NoResultException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
    public Set<Article> getAuthor(AuthorQueryModel model) throws NoResultException {

        Optional<Author> optionalAuthor = Optional.ofNullable(authorRepository.findByFirstNameAndLastName(model.getFirstName(), model.getLastName()));
        if (!optionalAuthor.isPresent()) {
            throw new NoResultException("Could not find any article ");
        }
        Author result = optionalAuthor.get();
        Set<Article> articleSet = new HashSet<>();

        result.getArticles().stream().forEach(articleHeader -> {
                Article article = articleRepository.findByHeader(articleHeader.getHeader());
                if (null == article) {
                    article = new Article();
                    article.setAuthors(new HashSet<>());
                }
                articleSet.add(article);
            });

        return articleSet;
    }
}
