package com.upday.articleService.models;

import com.upday.articleService.entities.Author;
import com.upday.articleService.entities.Keyword;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class ArticleRequestUpdateModel {

    @Schema(example = "IT")
    private String header;

    @Schema(example = "IT")
    private String shortDescription;

    @Schema(example = "IT")
    private String text;

    @Schema( description = "Start of contract", example = "2021-01-01")
    private LocalDate publishDate;

    private Set<AuthorRequestUpdateModel> authors = new HashSet<>();

    private Set<KeyWordRequestUpdateModel> keywords = new HashSet<>();
}
