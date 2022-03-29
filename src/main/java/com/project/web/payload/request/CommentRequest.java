package com.project.web.payload.request;

import lombok.Data;

@Data
public class CommentRequest {
    String content;
    Long userId;
    Long ideaId;
    Long parentComment;
}