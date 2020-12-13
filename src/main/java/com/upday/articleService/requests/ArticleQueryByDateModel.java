package com.upday.articleService.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Setter
@Getter
public class ArticleQueryByDateModel {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(required = true, description = "date of article publishing", example = "2021-01-01")
    private LocalDate publishDateFrom;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(required = true, description = "date of article publishing", example = "2021-01-01")
    private LocalDate publishDateTo;
}
