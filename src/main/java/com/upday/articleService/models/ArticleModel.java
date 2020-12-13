package com.upday.articleService.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ArticleModel {

    @NotBlank
    @Schema(required = true, example = "Covaid-19 update")
    private String header;

    @NotBlank
    @Schema(required = true, example = "Covaid-19 status update")
    private String shortDescription;

    @NotBlank
    @Schema(required = true, example = "This is a sample text for an article")
    private String text;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(required = true, description = "date of article publishing", example = "2021-01-01")
    private LocalDate publishDate;

    @NotNull
    @Schema(required = true, description = "articles Authors")
    private Set<AuthorModel> authors = new HashSet<>();

    @NotNull
    @Schema(required = true, description = "articles keywords")
    private Set<KeywordModel> keywords = new HashSet<>();

    @Override
    public String toString() {
        return "ArticleRequestCreateModel{" +
                "header='" + header + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", text='" + text + '\'' +
                ", publishDate=" + publishDate +
                ", authors=" + authors +
                ", keywords=" + keywords +
                '}';
    }
}
