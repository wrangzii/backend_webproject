package com.project.web.service;

import com.project.web.model.Comment;
import com.project.web.model.Idea;
import com.project.web.model.Submission;
import com.project.web.model.User;
import com.project.web.payload.response.CommentResponse;
import com.project.web.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class CommentServiceTest {
    @Autowired
    CommentService commentSer;
    @MockBean
    CommentRepository commentRepo;

    @Test
    void testGetAllCommentByIdeaService_thenReturnEmptyList() {
        List<Comment> expected = new ArrayList<>();
        when(commentRepo.findCommentByIdeaId(null)).thenReturn(expected);
        List<CommentResponse> actual = commentSer.getAllCommentByIdea(1L);
        assertEquals(new ArrayList<>(), actual);
        assertEquals(0, actual.size());
    }

    @Test
    void testGetAllDepartmentService_thenReturnAnObj() {
        Comment comment = new Comment();
        Comment parentComment = new Comment();
        Idea idea = new Idea();
        User user = new User();
        Date date = new Date();
        Submission submission = new Submission();
        user.setUsername("test");
        comment.setUserId(user);
        idea.setIdeaId(1L);
        submission.setSubmissionId(1L);
        comment.setContent("test");
        comment.setParentCommentId(parentComment);
        comment.setCreateDate(date);
        comment.setLastModifyDate(date);
        comment.setIsAnonymous(true);
        comment.setIdeaId(idea);
        comment.setUserId(user);
        comment.setCommentId(1L);
        comment.setContent("test");
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        comments.add(comment);
        when(commentRepo.findCommentByIdeaId(idea)).thenReturn(comments);
        submission.setFinalClosureDate(date);
        idea.setSubmissionId(submission);
        List<CommentResponse> actual = commentSer.getAllCommentByIdea(1L);
        assertEquals(0, actual.size());
    }

}
