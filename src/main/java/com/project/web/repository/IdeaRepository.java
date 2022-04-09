package com.project.web.repository;

import com.project.web.model.Idea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IdeaRepository extends JpaRepository<Idea, Long> {
    @Query(value = "SELECT * FROM ideas i, comments c, users u Where c.idea_id = i.idea_id and c.user_id = u.user_id ORDER BY c.create_date DESC", nativeQuery = true)
    Page<Idea> sortByLatestComment(Pageable page);
}
