package com.project.web.payload.response;

import lombok.Data;

@Data
public class IdeaReport {
    private Boolean isAnonymous;
    private String idea;
    private String submission;
    private String category;
    private int totalComment;
}
