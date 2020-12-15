package com.upday.articleService.services;

import com.upday.articleService.entities.Article;
import com.upday.articleService.entities.Author;
import com.upday.articleService.entities.Keyword;
import com.upday.articleService.models.KeywordModel;
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

@Service("keywordService")
public class KeywordServiceImpl implements KeywordService{

    @Resource
    private AuthorRepository authorRepository;

    @Resource
    private ArticleRepository articleRepository;

    @Resource
    private KeywordRepository keywordRepository;

    @Override
    public Set<Article> getKeyword(KeywordModel keywordModel)  throws NoResultException {
        Optional<Keyword> optionalKeyword = Optional.ofNullable(keywordRepository.findByKeyword(keywordModel.getKeyword()));
        if (!optionalKeyword.isPresent()) {
            throw new NoResultException("Could not find any article ");
        }
        Keyword keyword = optionalKeyword.get();
        Set<Article> articleSet = new HashSet<>();
            keyword.getArticles().stream().forEach(articleHeader -> {
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
