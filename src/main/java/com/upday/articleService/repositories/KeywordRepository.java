package com.upday.articleService.repositories;

import com.upday.articleService.entities.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long>, JpaSpecificationExecutor<Keyword> {

    boolean existsByKeyword(String keyword);
    Keyword findByKeyword(String keyword);
}