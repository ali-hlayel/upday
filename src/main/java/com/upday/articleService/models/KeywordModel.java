package com.upday.articleService.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class KeywordModel {

    @NotBlank
    @Schema(example = "COVAID-19")
    private String keyword;
}
