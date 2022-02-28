package com.project.web.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "reactions")
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reactionId;
    private String reactionType;
    private Date createDate;
    private Date lastModifyDate;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User userId;
    @ManyToOne()
    @JoinColumn(name = "ideaId")
    private Idea ideaId;
}
