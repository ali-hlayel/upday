package com.upday.articleService.repositories;

import com.upday.articleService.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {

    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    Author findByFirstNameAndLastName(String firstName, String lastName);
}