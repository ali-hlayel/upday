package com.upday.articleService.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthorRequestUpdateModel {

    @Schema(example = "Ali")
    private String firstName;

    @Schema(example = "Hlayel")
    private String lastName;
}
