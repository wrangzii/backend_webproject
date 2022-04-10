package com.project.web.repository;

import com.project.web.model.Idea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IdeaRepository extends JpaRepository<Idea, Long> {
    @Query(value = "SELECT * FROM ideas i, comments c, users u WHERE c.idea_id = i.idea_id and c.user_id = u.user_id ORDER BY c.create_date DESC", nativeQuery = true)
    Page<Idea> sortByLatestComment(Pageable page);
    @Query(value = "SELECT ideas.is_anonymous as \"isAnonymous\", ideas.title as \"Title\",\n" +
            "       submissions.submission_name as \"Submission\", categories.cate_name as \"Category\", count(*) as \"count\"\n" +
            "FROM ideas, comments, submissions, categories\n" +
            "WHERE ideas.idea_id = comments.idea_id and categories.cate_id = ideas.cate_id\n" +
            "  and ideas.submission_id = submissions.submission_id\n" +
            "group by ideas.is_anonymous, ideas.title, submissions.submission_name, categories.cate_name", nativeQuery = true)
    List<IdeaReport> getReport();
    interface IdeaReport {
        Boolean getIsAnonymous();
        String getTitle();
        String getSubmission();
        String getCategory();
        int getCount();
    }
}
