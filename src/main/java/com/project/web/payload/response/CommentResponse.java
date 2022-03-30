package com.project.web.payload.response;

import lombok.Data;

import java.util.Date;

@Data
public class CommentResponse {
    Long commentId;
    String content;
    Date createDate;
    Date modifyDate;
    String username;
    Long ideaId;
    Long submissionId;
    Date closureDate;
    Date finalClosureDate;
    Long parentCommentId;
    Boolean isAnonymous;

}
