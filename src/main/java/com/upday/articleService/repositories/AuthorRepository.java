package com.upday.articleService.repositories;

import com.upday.articleService.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {

 Author findByFirstNameAndLastName(String firstName, String lastName);

   List<Author>  findByLastNameAndFirstName(String lastName, String firstName);

}