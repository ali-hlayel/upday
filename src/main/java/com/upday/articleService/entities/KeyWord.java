package com.upday.articleService.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class KeyWord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String keyword;

    @ManyToMany(mappedBy = "authors")
    @JsonIgnore
    private Set<KeyWord> keyWords = new HashSet<>();

}
