package com.upday.articleService.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
@Getter
@Setter
public class AuthorQueryModel {

    @NotBlank
    @Schema(example = "Ali")
    private String firstName;

    @NotBlank
    @Schema(example = "Hlayel")
    private String lastName;

}