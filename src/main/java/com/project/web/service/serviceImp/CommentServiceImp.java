package com.project.web.service.serviceImp;

import com.project.web.model.*;
import com.project.web.payload.request.CommentRequest;
import com.project.web.payload.response.CommentResponse;
import com.project.web.payload.response.ResponseObject;
import com.project.web.repository.CommentRepository;
import com.project.web.repository.IdeaRepository;
import com.project.web.repository.SubmissionRepository;
import com.project.web.service.CommentService;
import com.project.web.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
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
    private final EmailSenderService emailSenderService;

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
    public ResponseEntity<ResponseObject> addCommentOfUser(CommentRequest comment, Long parentCommentId) {
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
                if (parentCommentId != null) {
                    parentComment.setCommentId(parentCommentId);
                    addComment.setParentCommentId(parentComment);
                }
                addComment.setIsAnonymous(comment.getIsAnonymous());
                commentRepo.save(addComment);
                sendMailToTheAuthor(checkIdeaExist.get().getUserId().getEmail());
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
    public ResponseEntity<ResponseObject> editComment(CommentRequest comment, Long id, Long parentCommentId) {
        Optional<Comment> editComment = commentRepo.findById(id);
        Comment parentComment = new Comment();
        if (editComment.isPresent()) {
            editComment.get().setContent(comment.getContent());
            editComment.get().setLastModifyDate(new Date());
            if (parentCommentId != null) {
                parentComment.setCommentId(parentCommentId);
                editComment.get().setParentCommentId(parentComment);
            }
            editComment.get().setIsAnonymous(comment.getIsAnonymous());
            commentRepo.save(editComment.get());
            return ResponseEntity.ok().body(new ResponseObject(HttpStatus.OK));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString()));
    }

    private void sendMailToTheAuthor(String email) {
        // Create the email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("A new comment on your idea");
        mailMessage.setFrom("test-email@gmail.com");
        mailMessage.setText("Someone just commented on your idea. Let check it now!");

        // Send the email
        emailSenderService.sendEmail(mailMessage);
    }

}
