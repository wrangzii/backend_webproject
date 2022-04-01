package com.project.web.payload.response;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ExportDataResponse {
    String username;
    Long ideaId;
    Long submissionId;
    String categoryName;
    String title;
    @NotEmpty(message = "*Please provide description")
    String description;
    Integer viewCount;
    String createDate;
    String modifyDate;
    Boolean isAnonymous;
}
