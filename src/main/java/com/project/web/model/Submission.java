package com.project.web.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@Entity
@Table(name = "Submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long submissionId;
    @NotEmpty(message = "*Please provide submission name")
    private String submissionName;
    @NotEmpty(message = "*Please provide description name")
    private String description;
    @NotEmpty(message = "*Please provide closure date")
    private Date closureDate;
    @NotEmpty(message = "*Please provide final closure date")
    private Date finalClosureDate;
}
