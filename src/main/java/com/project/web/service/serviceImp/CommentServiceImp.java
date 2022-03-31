package com.project.web.service.serviceImp;

import com.project.web.model.Comment;
import com.project.web.model.Idea;
import com.project.web.model.Submission;
import com.project.web.model.User;
import com.project.web.payload.request.CommentRequest;
import com.project.web.payload.response.CommentResponse;
import com.project.web.payload.response.ResponseObject;
import com.project.web.repository.CommentRepository;
import com.project.web.repository.IdeaRepository;
import com.project.web.repository.SubmissionRepository;
import com.project.web.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImp implements CommentService {
    private final CommentRepository commentRepo;
    private final IdeaRepository ideaRepo;
    private final SubmissionRepository submissionRepo;

    @Override
    public List<CommentResponse> getAllCommentByIdea(Long ideaId) {
        Idea idea = new Idea();
        idea.setIdeaId(ideaId);
        List<CommentResponse> response = new ArrayList<>();
        List<Comment> comments = commentRepo.findCommentByIdeaId(idea);
        for (Comment comment: comments) {
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setUsername(comment.getUserId().getUsername());
            commentResponse.setCommentId(comment.getCommentId());
            commentResponse.setIdeaId(comment.getIdeaId().getIdeaId());
            commentResponse.setContent(comment.getContent());
            commentResponse.setCreateDate(comment.getCreateDate());
            commentResponse.setModifyDate(comment.getLastModifyDate());
            commentResponse.setSubmissionId(comment.getIdeaId().getSubmissionId().getSubmissionId());
            commentResponse.setClosureDate(comment.getIdeaId().getSubmissionId().getClosureDate());
            commentResponse.setFinalClosureDate(comment.getIdeaId().getSubmissionId().getFinalClosureDate());
            commentResponse.setIsAnonymous(comment.getIsAnonymous());
            if (comment.getParentCommentId() != null) {
                commentResponse.setParentCommentId(comment.getParentCommentId().getCommentId());
            } else {
                commentResponse.setParentCommentId(null);
            }

            response.add(commentResponse);
        }
        return response;
    }

    @Override
    public ResponseEntity<ResponseObject> addCommentOfUser(CommentRequest comment) {
        Optional<Idea> checkIdeaExist = ideaRepo.findById(comment.getIdeaId());
        Comment addComment = new Comment();
        User user = new User();
        Idea idea = new Idea();
        Comment parentComment = new Comment();
        Date date = new Date();
        if (checkIdeaExist.isPresent()) {
            Optional<Submission> checkClosureDate = submissionRepo.findById(checkIdeaExist.get().getSubmissionId().getSubmissionId());
            if (checkClosureDate.isPresent() && checkClosureDate.get().getFinalClosureDate().after(date)) {
                addComment.setContent(comment.getContent());
                addComment.setCreateDate(date);
                addComment.setLastModifyDate(date);
                user.setUserId(comment.getUserId());
                idea.setIdeaId(comment.getIdeaId());
                addComment.setIdeaId(idea);
                addComment.setUserId(user);
                if (comment.getParentComment() != null) {
                    parentComment.setCommentId(comment.getParentComment());
                    addComment.setParentCommentId(parentComment);
                }
                addComment.setIsAnonymous(comment.getIsAnonymous());
                commentRepo.save(addComment);
                return ResponseEntity.ok().body(new ResponseObject(HttpStatus.OK));
            }
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString()));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteComment(Long id) {
        Optional<Comment> checkCommentExist = commentRepo.findById(id);
        if (checkCommentExist.isPresent()) {
            commentRepo.deleteById(id);
        }
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> editComment(CommentRequest comment, Long id) {
        Optional<Comment> editComment = commentRepo.findById(id);
        if (editComment.isPresent()) {
            editComment.get().setContent(comment.getContent());
            editComment.get().setLastModifyDate(new Date());
            editComment.get().setIsAnonymous(comment.getIsAnonymous());
            commentRepo.save(editComment.get());
        }
        return null;
    }
}
