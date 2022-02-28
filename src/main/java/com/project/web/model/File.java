package com.project.web.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "files")
public class File{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;
    private String filePath;
    private Date createDate;
    private Date lastModifyDate;
    @ManyToOne
    @JoinColumn(name = "ideaId")
    private Idea ideaId;
}
