package com.project.web.payload.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CategoryRequest {
    private Long cateId;
    @NotEmpty(message = "*Please provide category name")
    private String cateName;
    @NotEmpty(message = "*Please provide description name")
    private String description;
}
