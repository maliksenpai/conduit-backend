package com.example.conduit.modules.article.dto;

import com.example.conduit.modules.article.model.Comment;
import com.example.conduit.shared.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentsDTO extends BaseDTO {
    private List<Comment> comments;
}
