package com.project.web.service;

import com.project.web.payload.request.CommentRequest;
import com.project.web.payload.response.CommentResponse;
import com.project.web.payload.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommentService {
    List<CommentResponse> getAllCommentByIdea(Long ideaId);
    ResponseEntity<ResponseObject> addCommentOfUser(CommentRequest comment, Long parentCommentId);
    ResponseEntity<ResponseObject> deleteComment(Long id);
    ResponseEntity<ResponseObject> editComment(CommentRequest comment, Long id);

}