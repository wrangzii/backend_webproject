package com.project.web.repository;

import com.project.web.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Boolean existsByCateName(String cateName);
}
