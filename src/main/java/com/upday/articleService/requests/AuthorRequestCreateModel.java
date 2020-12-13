package com.upday.articleService.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class AuthorRequestCreateModel {

    @NotBlank
    @Schema(example = "Ali")
    private String firstName;

    @NotBlank
    @Schema(example = "Hlayel")
    private String lastName;

}
