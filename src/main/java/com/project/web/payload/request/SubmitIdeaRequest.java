package com.project.web.payload.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class SubmitIdeaRequest {
    private Long ideaId;
    private String title;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lastModifyDate;
    private Integer viewCount;
    private Long userId;
    private Long cateId;
    private Long submissionId;
    private Boolean isAnonymous;
}
