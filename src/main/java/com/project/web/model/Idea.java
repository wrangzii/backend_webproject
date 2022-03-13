package com.project.web.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "ideas")
public class Idea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ideaId;
    private String title;
    private String description;
    private Date createDate;
    private Date lastModifyDate;
    private Integer viewCount;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User userId;
    @ManyToOne
    @JoinColumn(name = "cateId")
    private Category cateId;
    @ManyToOne
    @JoinColumn(name = "submissionId")
    private Submission submissionId;

    public Idea() {
    }
}
