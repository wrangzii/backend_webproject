package com.project.web.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "Comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId;
    @NotEmpty(message = "*Please provide content")
    private String content;
    private Date createDate;
    private Date lastModifyDate;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User userId;
    @ManyToOne
    @JoinColumn(name = "ideaId")
    private Idea ideaId;
    @ManyToOne
    @JoinColumn(name="parentCommentId", referencedColumnName="commentId")
    private Comment parentCommentId;
    @NotNull
    private Boolean isAnonymous;
}
