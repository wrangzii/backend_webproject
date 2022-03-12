package com.project.web.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "Submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long submissionId;
    private String submissionName;
    private String description;
    private Date closureDate;
    private Date finalClosureDate;
}
