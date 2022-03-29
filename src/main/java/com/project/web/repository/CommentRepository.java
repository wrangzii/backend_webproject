package com.project.web.repository;

import com.project.web.model.Comment;
import com.project.web.model.Idea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentByIdeaId(Idea ideaId);
    Comment findByIdeaId(Idea ideaId);
}
