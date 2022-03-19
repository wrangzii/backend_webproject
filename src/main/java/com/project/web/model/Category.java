package com.project.web.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cateId;
    @NotEmpty(message = "*Please provide category name")
    private String cateName;
    @NotEmpty(message = "*Please provide description name")
    private String description;
    private Date createDate;
    private Date lastModifyDate;

}
