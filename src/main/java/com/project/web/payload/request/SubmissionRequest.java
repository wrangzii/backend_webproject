package com.project.web.payload.request;

import lombok.Data;

import java.util.Date;

@Data
public class SubmissionRequest {
    private String submissionName;
    private String description;
    private Date closureDate;
    private Date finalClosureDate;
}
