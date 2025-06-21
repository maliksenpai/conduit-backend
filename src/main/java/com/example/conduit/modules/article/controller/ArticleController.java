package com.example.conduit.modules.article.controller;

import com.example.conduit.modules.article.dto.ArticleDTO;
import com.example.conduit.modules.article.dto.ArticleListDTO;
import com.example.conduit.modules.article.model.Article;
import com.example.conduit.modules.article.service.ArticleService;
import com.example.conduit.shared.dto.ErrorObject;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("")
    public ResponseEntity<ArticleListDTO> getArticleList() {
        try {
            ArticleListDTO articleListDTO = new ArticleListDTO();
            List<Article> articleList = articleService.getArticleList();
            articleListDTO.setArticles(articleList);
            articleListDTO.setArticlesCount(articleList.size());
            return ResponseEntity.ok(articleListDTO);
        } catch (Exception e) {
            ArticleListDTO response = new ArticleListDTO();
            ErrorObject errorObject = new ErrorObject();
            errorObject.setBody(List.of(e.getMessage()));
            response.setErrors(errorObject);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("")
    public ResponseEntity<ArticleDTO> createArticlePost(@Valid @RequestBody ArticleDTO articleDTO) {
        try {
            ArticleDTO createdArticle = articleService.createArticle(articleDTO.getArticle());
            return ResponseEntity.ok(createdArticle);
        } catch (Exception e) {
            ArticleDTO response = new ArticleDTO();
            ErrorObject errorObject = new ErrorObject();
            errorObject.setBody(List.of(e.getMessage()));
            response.setErrors(errorObject);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{slug}")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable String slug, @Valid @RequestBody ArticleDTO articleDTO) {
        try {
            ArticleDTO updatedArticle = articleService.updateArticle(articleDTO.getArticle(), slug);
            return ResponseEntity.ok(updatedArticle);
        } catch (Exception e) {
            ArticleDTO response = new ArticleDTO();
            ErrorObject errorObject = new ErrorObject();
            errorObject.setBody(List.of(e.getMessage()));
            response.setErrors(errorObject);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
