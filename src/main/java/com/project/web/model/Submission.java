package com.project.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    @NotNull
    @JsonFormat(pattern="mm/dd/yyyy HH:mm:ss")
    private Date closureDate;
    @NotNull
    @JsonFormat(pattern="mm/dd/yyyy HH:mm:ss")
    private Date finalClosureDate;
}
