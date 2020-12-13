package com.upday.articleService.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class KeyWordRequestUpdateModel {

    @Schema(example = "corona")
    private String keyword;
}
