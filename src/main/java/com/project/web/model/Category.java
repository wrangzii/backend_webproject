package com.project.web.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cateId;
    private String cateName;
    private String description;
    private Date createDate;
    private Date lastModifyDate;

}
