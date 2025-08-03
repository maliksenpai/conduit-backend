package com.example.conduit.modules.article.controller;

import com.example.conduit.modules.article.dto.ArticleDTO;
import com.example.conduit.modules.article.dto.ArticleListDTO;
import com.example.conduit.modules.article.dto.CommentDTO;
import com.example.conduit.modules.article.dto.CommentsDTO;
import com.example.conduit.modules.article.model.Article;
import com.example.conduit.modules.article.service.ArticleService;
import com.example.conduit.modules.article.service.CommentService;
import com.example.conduit.shared.dto.ErrorObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
@Tag(name = "Articles")
public class ArticleController {

    private final ArticleService articleService;
    private final CommentService commentService;

    public ArticleController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @GetMapping("")
    public ResponseEntity<ArticleListDTO> getArticleList(Pageable pageable) {
        try {
            ArticleListDTO articleListDTO = new ArticleListDTO();
            List<Article> articleList = articleService.getArticleList(pageable);
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
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable String slug, @RequestBody ArticleDTO articleDTO) {
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

    @DeleteMapping("/{slug}")
    public ResponseEntity<Boolean> deleteArticle(@PathVariable String slug) {
        boolean response = articleService.deleteArticle(slug);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{slug}/comments")
    public ResponseEntity<CommentDTO> addComments(@PathVariable String slug, @Valid @RequestBody CommentDTO comment) {
        CommentDTO commentDTO = new CommentDTO(commentService.addComment(slug, comment.getComment()));
        return ResponseEntity.ok(commentDTO);
    }

    @DeleteMapping("/{slug}/comments/{commentId}")
    public ResponseEntity<Boolean> deleteComment(@PathVariable String slug, @PathVariable String commentId) {
        return ResponseEntity.ok(commentService.deleteComment(slug, commentId));
    }

    @GetMapping("/{slug}/comments")
    public ResponseEntity<CommentsDTO> getCommentOfArticle(@PathVariable String slug) {
        return ResponseEntity.ok(new CommentsDTO(commentService.getListCommentOfArticle(slug)));
    }
}
