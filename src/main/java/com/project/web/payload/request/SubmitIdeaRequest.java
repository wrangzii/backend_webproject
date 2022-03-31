package com.project.web.payload.request;

import lombok.Data;

@Data
public class SubmitIdeaRequest {
    private Long ideaId;
    private String title;
    private String description;
    private Long userId;
    private Long cateId;
    private Long submissionId;
    private Boolean isAnonymous;
}
