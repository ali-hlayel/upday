package com.upday.articleService.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class KeywordRequestCreateModel {

    @NotBlank
    @Schema(example = "COVAID-19")
    private String keyword;
}
