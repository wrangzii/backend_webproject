package com.project.web.repository;

import com.project.web.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Boolean existsByCateName(String cateName);
    @Query(value = "Select cate_name as cateName, count(*) as count from categories, ideas\n" +
            "where categories.cate_id= ideas.cate_id\n" +
            "group by cate_name", nativeQuery = true)
    List<CategoryChart> getCateChart();
    interface CategoryChart {
        String getCateName();
        int getCount();
    }
}
