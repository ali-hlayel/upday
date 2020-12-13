package com.upday.articleService.models;

import com.upday.articleService.entities.Article;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class AuthorModel {

    @NotBlank
    @Schema(example = "Ali")
    private String firstName;

    @NotBlank
    @Schema(example = "Hlayel")
    private String lastName;

}

