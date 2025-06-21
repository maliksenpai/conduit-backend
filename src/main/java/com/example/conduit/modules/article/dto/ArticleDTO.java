package com.example.conduit.modules.article.dto;

import com.example.conduit.modules.article.model.Article;
import com.example.conduit.shared.dto.BaseDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO extends BaseDTO {
    @Valid
    private Article article;
}
